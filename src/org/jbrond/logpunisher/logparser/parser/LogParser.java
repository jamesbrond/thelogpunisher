package org.jbrond.logpunisher.logparser.parser;

import java.text.ParseException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.naming.ConfigurationException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jbrond.logpunisher.config.LogFiltersConfig;
import org.jbrond.logpunisher.config.LogOptionsConfig;
import org.jbrond.logpunisher.logparser.LogObject;

public class LogParser implements LogParserInterface {

  private static final Logger L = LogManager.getLogger(LogParser.class.getName());

  public static final List<String> LOG_TYPES = Collections.unmodifiableList(new ArrayList<>(Arrays.asList("log", "csv")));
  public static final int LOG_TYPE_LOG = 0;
  public static final int LOG_TYPE_CSV = 1;

  protected final LogOptionsConfig options;
  protected final String filename;
  protected final Pattern rowPattern;
  protected final Map<String, Integer> rowMatches;
  protected final List<LogFiltersConfig> filters;
  protected final List<LogFiltersConfig> details;
  protected final DateTimeFormatter dateFormat;

  public LogParser(LogOptionsConfig options, String filename) throws ConfigurationException {
    this.options = options;
    this.filename = filename;
    rowPattern = options.getCompiledPattern();
    dateFormat = options.getDateTimeFormat();
    if (null == dateFormat) {
      throw new ConfigurationException("Dateformat cannot be null");
    }
    rowMatches = this.options.getMatches();
    if (null == rowMatches || rowMatches.isEmpty()) {
      throw new ConfigurationException("Global matches cannot be null");
    }
    details = options.getDetails();
    filters = options.getFilters();
  }

  /**
   *
   * @param line
   * @return
   */
  @Override
  public LogObject match(String line) {
    Matcher rowMatch = rowPattern.matcher(line);
    if (!rowMatch.find()) {
      return null;
    }

    Map<String, String> item = new HashMap<>();
    rowMatches.entrySet().forEach((Entry<String, Integer> rowEntry) -> item.put(rowEntry.getKey(), rowMatch.group(rowEntry.getValue())));

    if (null != filters && !filters.isEmpty()) {
      Map<String, String> filteredItem = matchFilters(item.get(LogObject.MATCHER_KEY_MESSAGE));
      if (filteredItem.size() > 0) {
        item.putAll(filteredItem);
      } else {
        return null;
      }
    } else if (null != details && !details.isEmpty()) {
      item.putAll(matchDetails(item.get(LogObject.MATCHER_KEY_MESSAGE)));
    }

    try {
      return buildLogObject(item);
    } catch (ParseException e) {
      L.warn(e);
    }
    return null;
  }

  protected LogObject buildLogObject(Map<String, String> item) throws ParseException {
    LogObject.LogObjectBuilder objBuilder = new LogObject.LogObjectBuilder()
        .setFilename(filename);
    item.entrySet().stream().forEach(x -> objBuilder.set(x.getKey(), x.getValue()));
    objBuilder.setDate(item.containsKey(LogObject.MATCHER_KEY_DATE) ? toDateTime(item.get(LogObject.MATCHER_KEY_DATE)) : null);

    return objBuilder.build();
  }

  private LocalDateTime toDateTime(String date) {
    try {
      return LocalDateTime.parse(date, dateFormat);
    } catch (DateTimeParseException e) {
      L.warn(e);
    }
    return null;
  }

  private Map<String, String> matchFilters(String message) {
    Map<String, String> item = new HashMap<>();
    filters.forEach(filter -> {
      Matcher filterMatch = filter.getComiledPattern().matcher(message);
      if (filterMatch.find()) {
        Map<String, Integer> filterMatches = filter.getMatches();
        if (null != filterMatches && !filterMatches.isEmpty()) {
          filterMatches.entrySet().forEach((Entry<String, Integer> filterEntry) -> item.put(filterEntry.getKey(), filterMatch.group(filterEntry.getValue())));
        }
      }
    });
    return item;
  }

  private Map<String, String> matchDetails(String message) {
    Map<String, String> item = new HashMap<>();
    details.forEach(detail -> {
      Matcher detailMatch = detail.getComiledPattern().matcher(message);
      if (detailMatch.find()) {
        Map<String, Integer> detailMatches = detail.getMatches();
        if (null != detailMatches && !detailMatches.isEmpty()) {
          detailMatches.entrySet().forEach((Entry<String, Integer> detailEntry) -> item.put(detailEntry.getKey(), detailMatch.group(detailEntry.getValue())));
        }
      }
    });
    return item;
  }
}

// ~@:-]
