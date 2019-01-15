package org.jbrond.punisher.logparser.parser;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import javax.naming.ConfigurationException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jbrond.punisher.config.LogOptionsConfig;
import org.jbrond.punisher.logparser.LogObject;

public class LogParserCSV extends LogParser implements LogParserInterface {

  private static final Logger L = LogManager.getLogger(LogParserCSV.class.getName());

  private static final char DEFAULT_SEPARATOR = ',';
  private static final char DEFAULT_QUOTE = '"';

  private final char m_separator;
  private final char m_quote;

  public LogParserCSV(LogOptionsConfig options) throws ConfigurationException {
    super(options);
    String separator = options.getSeparator();
    m_separator = separator == null || separator.isEmpty() ? DEFAULT_SEPARATOR : separator.charAt(0);
    String quote = options.getQuote();
    m_quote = quote == null || quote.isEmpty() ? DEFAULT_QUOTE : quote.charAt(0);
  }

  @Override
  public LogObject match(String line) {
    List<String> csvItems = parseLine(line, m_separator, m_quote);
    if (null != csvItems && !csvItems.isEmpty()) {
      Map<String, String> collectedItem = new HashMap<>();
      m_rowMatches.entrySet().forEach((Entry<String, Integer> rowEntry) -> {
        collectedItem.put(rowEntry.getKey(), csvItems.get(rowEntry.getValue()));
      });
      try {
        return buildLogObject(collectedItem);
      } catch (ParseException e) {
        L.warn(e);
      }
    }
    return null;
  }

  private List<String> parseLine(String cvsLine, char separators, char customQuote) {
    List<String> result = new ArrayList<>();

    //if empty, return!
    if (null == cvsLine || cvsLine.isEmpty()) {
      return result;
    }

    StringBuilder curVal = new StringBuilder();
    boolean inQuotes = false;
    boolean startCollectChar = false;
    boolean doubleQuotesInColumn = false;

    char[] chars = cvsLine.toCharArray();

    for (char ch : chars) {
      if (inQuotes) {
        startCollectChar = true;
        if (ch == customQuote) {
          inQuotes = false;
          doubleQuotesInColumn = false;
        } else {
          //Fixed : allow "" in custom quote enclosed
          if (ch == '\"') {
            if (!doubleQuotesInColumn) {
              curVal.append(ch);
              doubleQuotesInColumn = true;
            }
          } else {
            curVal.append(ch);
          }
        }
      } else {
        if (ch == customQuote) {
          inQuotes = true;
          //Fixed : allow "" in empty quote enclosed
          if (chars[0] != '"' && customQuote == '\"') {
            curVal.append('"');
          }
          //double quotes in column will hit this!
          if (startCollectChar) {
            curVal.append('"');
          }
        } else if (ch == separators) {

          result.add(curVal.toString());

          curVal = new StringBuilder();
          startCollectChar = false;

        } else if (ch == '\r') {
          //ignore LF characters
        } else if (ch == '\n') {
          //the end, break!
          break;
        } else {
          curVal.append(ch);
        }
      }

    }

    result.add(curVal.toString());

    return result;
  }
}

// ~@:-]
