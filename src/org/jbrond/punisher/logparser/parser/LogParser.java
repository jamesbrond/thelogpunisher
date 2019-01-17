package org.jbrond.punisher.logparser.parser;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.naming.ConfigurationException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jbrond.punisher.config.LogFiltersConfig;
import org.jbrond.punisher.config.LogOptionsConfig;
import org.jbrond.punisher.logparser.LogObject;

public class LogParser implements LogParserInterface {

  private static final Logger L = LogManager.getLogger(LogParser.class.getName());

  public static final List<String> LOG_TYPES = Arrays.asList(new String[]{"log", "csv"});
  public static final int LOG_TYPE_LOG = 0;
  public static final int LOG_TYPE_CSV = 1;

  protected final LogOptionsConfig m_options;
  protected final String m_filename;
  protected final Pattern m_rowPattern;
  protected final Map<String, Integer> m_rowMatches;
  protected final List<LogFiltersConfig> m_filters;
  protected final List<LogFiltersConfig> m_details;
  protected final SimpleDateFormat m_dateformat;

  public LogParser(LogOptionsConfig options, String filename) throws ConfigurationException {
    m_options = options;
    m_filename = filename;
    m_rowPattern = options.getCompiledPattern();
    m_dateformat = options.getSimpleDateformat();
    if (null == m_dateformat) {
      throw new ConfigurationException("Dateformat cannot be null");
    }
    m_rowMatches = m_options.getMatches();
    if (null == m_rowMatches || m_rowMatches.isEmpty()) {
      throw new ConfigurationException("Global matches cannot be null");
    }
    m_details = options.getDetails();
    m_filters = options.getFilters();
  }

  /**
   *
   * @param line
   * @return
   */
  @Override
  public LogObject match(String line) {
    Matcher rowMatch = m_rowPattern.matcher(line);
    if (rowMatch.find()) {
      Map<String, String> item = new HashMap<>();
      m_rowMatches.entrySet().forEach((Entry<String, Integer> rowEntry) -> {
        item.put(rowEntry.getKey(), rowMatch.group(rowEntry.getValue()));
      });

      if (null != m_filters && !m_filters.isEmpty() && item.containsKey(LogObject.MATCHER_KEY_MESSAGE)) {
        String message = item.get(LogObject.MATCHER_KEY_MESSAGE);
        for (LogFiltersConfig filter : m_filters) {
          Matcher filterMatch = filter.getComiledPattern().matcher(message);
          if (filterMatch.find()) {
            Map<String, Integer> filterMatches = filter.getMatches();
            if (null != filterMatches && !filterMatches.isEmpty()) {
              filterMatches.entrySet().forEach((Entry<String, Integer> filterEntry) -> {
                item.put(filterEntry.getKey(), filterMatch.group(filterEntry.getValue()));
              });
            }
          } else {
            return null;
          }
        }
      } else if (null != m_details && !m_details.isEmpty() && item.containsKey(LogObject.MATCHER_KEY_MESSAGE)) {
        String message = item.get(LogObject.MATCHER_KEY_MESSAGE);
        m_details.forEach((detail) -> {
          Matcher detailMatch = detail.getComiledPattern().matcher(message);
          if (detailMatch.find()) {
            Map<String, Integer> detailMatches = detail.getMatches();
            if (null != detailMatches && !detailMatches.isEmpty()) {
              detailMatches.entrySet().forEach((Entry<String, Integer> detailEntry) -> {
                item.put(detailEntry.getKey(), detailMatch.group(detailEntry.getValue()));
              });
            }
          }
        });
      }

      L.trace(item);

      try {
        return buildLogObject(item);
      } catch (ParseException e) {
        L.warn(e);
      }
    }
    return null;
  }

  protected LogObject buildLogObject(Map<String, String> item) throws ParseException {
    return new LogObject.Builder()
            .setFilename(m_filename)
            .setDate(item.containsKey(LogObject.MATCHER_KEY_DATE) ? m_dateformat.parse(item.get(LogObject.MATCHER_KEY_DATE)) : null)
            .setIp(item.containsKey(LogObject.MATCHER_KEY_IP) ? item.get(LogObject.MATCHER_KEY_IP) : null)
            .setMessage(item.containsKey(LogObject.MATCHER_KEY_MESSAGE) ? item.get(LogObject.MATCHER_KEY_MESSAGE) : null)
            .setSession(item.containsKey(LogObject.MATCHER_KEY_SESSION) ? item.get(LogObject.MATCHER_KEY_SESSION) : null)
            .setUser(item.containsKey(LogObject.MATCHER_KEY_USER) ? item.get(LogObject.MATCHER_KEY_USER) : null)
            .build();
  }
}
