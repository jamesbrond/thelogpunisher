package org.jbrond.punisher;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.Writer;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jbrond.punisher.config.GlobalConfig;
import org.jbrond.punisher.config.LogConfig;
import org.jbrond.punisher.config.LogFiltersConfig;
import org.jbrond.punisher.config.LogOptionsConfig;
import org.jbrond.punisher.logparser.LogAnalyzer;
import org.jbrond.punisher.logparser.LogObject;
import org.yaml.snakeyaml.TypeDescription;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;

import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;

@Command(name = "thepunisher", mixinStandardHelpOptions = true, version = "The Punisher v0.0.1")
public class ThePunisher implements Runnable {

  @Option(names = {"-c",
    "--configuration"}, required = true, arity = "1", paramLabel = "FILE", description = "Configuration file with instruction.")
  private File o_confFile;

  @Option(names = {"-f", "--filter"}, paramLabel = "KEY=VALUE", description = "Filter the input log(s) to extract only matching lines.")
  private Map<String, String> o_filters;

  @Option(names = {"-o", "--output"}, paramLabel = "FILE", description = "Redirect output stream (default: System.out).")
  private final Writer o_output = new OutputStreamWriter(System.out);

  private static final Logger L = LogManager.getLogger(ThePunisher.class.getName());

  public static void main(String[] args) {
    try {
      Runnable app = new ThePunisher();

      CommandLine cl = new CommandLine(app).registerConverter(java.io.Writer.class, o -> new PrintWriter(o));
      cl.parse(args);
      app.run();
      System.exit(0);
    } catch (Exception e) {
      L.fatal(e);
      L.catching(e);
      System.exit(1);
    }
  }

  @Override
  public void run() {
    L.info("The search for the guilty is itself guilty...");
    L.trace("configuration file: {}", o_confFile.getAbsolutePath());
    Constructor yamlConstructor = new Constructor(GlobalConfig.class);
    TypeDescription customTypeDescription = new TypeDescription(GlobalConfig.class);
    customTypeDescription.addPropertyParameters("options", LogOptionsConfig.class);
    customTypeDescription.addPropertyParameters("filters", LogFiltersConfig.class);
    yamlConstructor.addTypeDescription(customTypeDescription);
    Yaml yaml = new Yaml(yamlConstructor);

    try ( InputStream inputStream = new FileInputStream(o_confFile)) {
      GlobalConfig conf = yaml.load(inputStream);
      L.info("Run log parsing '{}'", conf.getName());
      String basePath = o_confFile.getParent();

      List<LogConfig> sessions = conf.getLogs();
      L.debug("Configuration file contains {} sections", sessions.size());
      LogAnalyzer anal = new LogAnalyzer();
      sessions.forEach((LogConfig l) -> {
        String file = new StringBuilder().append(basePath).append(File.separatorChar).append(l.getFile()).toString();
        Path path = new File(file).toPath();
        L.debug("Section {} (file: {})", l.getType(), path.toString());
        anal.add(path, l.getType(), l.getOptions());
      });

//    m_conf = new INIConfiguration();
//
//    try {
//      m_conf.read(new FileReader(o_confFile));
//      m_conf.addProperty("basepath", basePath);
//      Set<String> sections = m_conf.getSections();
//
//      sections.forEach((s) -> {
//        SubnodeConfiguration sectionConf = m_conf.getSection(s);
//        String file = new StringBuilder().append(basePath).append(File.separatorChar).append(sectionConf.getString("file")).toString();
//      });
//
      Stream<LogObject> stream;
      if (null == o_filters || o_filters.isEmpty()) {
        L.debug("filter by: no filter");
        stream = anal.sort().stream();
      } else {
        // apply filters
        L.debug("filter by: {}", o_filters);
        stream = anal.sort().stream().filter(x -> x.filter(o_filters));
      }
      try ( BufferedWriter out = new BufferedWriter(o_output)) {
        stream.forEach((LogObject x) -> {
          try {
            out.write(x.toString());
            out.newLine();
          } catch (IOException e) {
            L.warn(e);
          }
        });
      }
////      anal.stream().filter(x -> "COLAMUSSIP".equals(x.get("user"))).forEach(System.out::println);
//
    } catch (IOException e) {
      L.error(e);
      L.catching(e);
    }
  }
}

// ~@:-]
