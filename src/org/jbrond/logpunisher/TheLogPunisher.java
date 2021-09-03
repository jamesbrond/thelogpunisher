package org.jbrond.logpunisher;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jbrond.logpunisher.config.ConfigurationFactory;
import org.jbrond.logpunisher.config.GlobalConfig;
import org.jbrond.logpunisher.config.LogConfig;
import org.jbrond.logpunisher.logparser.LogAnalyzer;
import org.jbrond.logpunisher.writer.OutputWriterFactory;

import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;

@Command(name = "thepunisher",
    mixinStandardHelpOptions = true,
    version = "1.2.4",
    description = "Program created with the aim of finding the guilty and prosecuting the innocent")
public class TheLogPunisher implements Callable<Integer> {

  private static final Logger L = LogManager.getLogger(TheLogPunisher.class.getName());

  @Option(names = {"-c", "--configuration"},
      required = true,
      arity = "1",
      paramLabel = "FILE",
      description = "Mandatory configuration file with instructions.")
  private File configFile;

  @Option(names = {"-f", "--filter"},
      paramLabel = "KEY=VALUE",
      description = "Filter the input log(s) to extract only matching lines.")
  private Map<String, String> filters;

  @Option(names = {"-o", "--output"},
      paramLabel = "filename",
      description = "Redirect output stream (default: System.out).")
  private File output;

  public static void main(String... args) {
    int exitCode;
    try {
      exitCode = new CommandLine(new TheLogPunisher()).execute(args);
    } catch (Exception e) {
      L.fatal(e);
      L.catching(e);
      exitCode = 1;
    }
    System.exit(exitCode);
  }

  @Override
  public Integer call() throws Exception {
    int exitStatus = 0;
    L.info("The search for the guilties and the persecution of the innocents...");
    L.debug("configuration file: {}", configFile.getAbsolutePath());

    try {
      String basePath = configFile.getParent();
      GlobalConfig conf = ConfigurationFactory.read(configFile);
      L.info("Run log parsing '{}'", conf.getName());

      LogAnalyzer anal = new LogAnalyzer();
      List<LogConfig> sessions = conf.getLogs();
      L.debug("Configuration file contains {} sections", sessions.size());
      sessions.stream().parallel().forEach((LogConfig l) -> {
        Path path = new File(basePath, l.getFile()).toPath();
        L.debug("Section {} (file: {})", l.getType(), path.toString());
        anal.add(path, l.getType(), l.getOptions());
      });

      if (null == filters || filters.isEmpty()) {
        L.debug("filter by: no filter");
        anal.sort().stream();
      } else {
        // apply filters
        L.debug("filter by: {}", filters);
        anal.sort().stream().filter(x -> x.filter(filters));
      }

      OutputWriterFactory.build(output).write(anal);
      exitStatus = 0;
    } catch (IOException e) {
      L.error(e);
      L.catching(e);
    }
    return exitStatus;
  }

}

// ~@:-]
