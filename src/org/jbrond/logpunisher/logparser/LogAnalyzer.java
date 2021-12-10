package org.jbrond.logpunisher.logparser;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.naming.ConfigurationException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jbrond.logpunisher.config.LogOptionsConfig;
import org.jbrond.logpunisher.logparser.parser.LogParser;
import org.jbrond.logpunisher.logparser.parser.LogParserCSV;
import org.jbrond.logpunisher.logparser.parser.LogParserInterface;

public class LogAnalyzer {

  public class LogObjectComparator implements Comparator<LogObject> {

    @Override
    public int compare(LogObject o1, LogObject o2) {
      return o1.compareTo(o2);
    }
  }

  private static final Logger L = LogManager.getLogger(LogAnalyzer.class.getName());

  private List<LogObject> logCollection;

  public LogAnalyzer() {
    logCollection = new ArrayList<>();
  }

  public List<LogObject> parse(Path file, String type, LogOptionsConfig options) throws IOException, ConfigurationException {
    LogParserInterface parser;
    int t = LogParser.LOG_TYPES.indexOf(type);
    if (t == -1) {
      throw new ConfigurationException("Unknown log type");
    }
    String filename = file.getFileName().toString();
    switch (t) {
      case LogParser.LOG_TYPE_LOG:
        L.debug("{} is LOG file", filename);
        parser = new LogParser(options, filename);
        break;
      case LogParser.LOG_TYPE_CSV:
        L.debug("{} is CSV file", filename);
        parser = new LogParserCSV(options, filename);
        break;
      default:
        throw new ConfigurationException("Unknown log type");
    }

    try (Stream<String> input = Files.lines(file))  {
      return input.map(parser::match).filter(Objects::nonNull).collect(Collectors.toList());
    }
  }

  public LogAnalyzer add(String fileName, String type, LogOptionsConfig options) {
    return add(Paths.get(fileName), type, options);
  }

  public LogAnalyzer add(Path file, String type, LogOptionsConfig options) {
    try {
      List<LogObject> items = parse(file, type, options);
      L.debug("found {} items", items.size());
      logCollection.addAll(items);
    } catch (NullPointerException | IOException | ConfigurationException e) {
      L.error(e);
      L.catching(e);
    }
    return this;
  }

  public LogAnalyzer sort() {
    Collections.sort(logCollection, new LogObjectComparator());
    return this;
  }

  public LogAnalyzer filter(Map<String, String> filters) {
    logCollection = logCollection.stream().filter(x -> x.filter(filters)).collect(Collectors.toList());
    return this;
  }

  public Stream<LogObject> stream() {
    return logCollection.stream();
  }

  public List<LogObject> get() {
    return logCollection;
  }
}

// ~@:-]
