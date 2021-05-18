package org.jbrond.punisher;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.stream.Stream;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jbrond.punisher.config.ConfigurationFactory;
import org.jbrond.punisher.config.GlobalConfig;
import org.jbrond.punisher.config.LogConfig;
import org.jbrond.punisher.logparser.LogAnalyzer;
import org.jbrond.punisher.logparser.LogObject;
import org.jbrond.punisher.writer.OutputWriterFactory;

import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;

@Command(name = "thepunisher",
    mixinStandardHelpOptions = true,
    version = "1.2.3",
    description = "The search for the guilties and the persecution of the innocents")
public class ThePunisher implements Callable<Integer> {

  private static final Logger L = LogManager.getLogger(ThePunisher.class.getName());

  @Option(names = {"-c", "--configuration"},
      required = true,
      arity = "1",
      paramLabel = "FILE",
      description = "Mandatory configuration file with instructions.")
  private File o_confFile;

  @Option(names = {"-f", "--filter"},
      paramLabel = "KEY=VALUE",
      description = "Filter the input log(s) to extract only matching lines.")
  private Map<String, String> o_filters;

  @Option(names = {"-o", "--output"},
      paramLabel = "filename",
      description = "Redirect output stream (default: System.out).")
  private File o_output;

  public static void main(String... args) {
    int exitCode;
    try {
      exitCode = new CommandLine(new ThePunisher()).execute(args);
    } catch (Exception e) {
      L.fatal(e);
      L.catching(e);
      exitCode = 1;
    }
    System.exit(exitCode);
  }

  @Override
  public Integer call() throws Exception {
    L.info("The search for the guilties and the persecution of the innocents...");
    L.trace("configuration file: {}", o_confFile.getAbsolutePath());

    try {
      String basePath = o_confFile.getParent();
      GlobalConfig conf = ConfigurationFactory.read(o_confFile);
      L.info("Run log parsing '{}'", conf.getName());

      LogAnalyzer anal = new LogAnalyzer();
      List<LogConfig> sessions = conf.getLogs();
      L.debug("Configuration file contains {} sections", sessions.size());
      sessions.stream().parallel().forEach((LogConfig l) -> {
        Path path = new File(basePath, l.getFile()).toPath();
        L.debug("Section {} (file: {})", l.getType(), path.toString());
        anal.add(path, l.getType(), l.getOptions());
      });

      Stream<LogObject> stream;
      if (null == o_filters || o_filters.isEmpty()) {
        L.debug("filter by: no filter");
        stream = anal.sort().stream();
      } else {
        // apply filters
        L.debug("filter by: {}", o_filters);
        stream = anal.sort().stream().filter(x -> x.filter(o_filters));
      }

      OutputWriterFactory.build(o_output).write(stream);
      return 0;
    } catch (IOException e) {
      L.error(e);
      L.catching(e);
    }
    return 1;
  }

}

// ~@:-]
