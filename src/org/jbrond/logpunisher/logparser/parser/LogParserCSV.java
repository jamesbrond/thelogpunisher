package org.jbrond.logpunisher.logparser.parser;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import javax.naming.ConfigurationException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jbrond.logpunisher.config.LogOptionsConfig;
import org.jbrond.logpunisher.logparser.LogObject;

public class LogParserCSV extends LogParser implements LogParserInterface {

  private static final Logger L = LogManager.getLogger(LogParserCSV.class.getName());

  private static final char DEFAULT_SEPARATOR = ',';
  private static final char DEFAULT_QUOTE = '"';

  private final char separator;
  private final char quote;

  public LogParserCSV(LogOptionsConfig options, String filename) throws ConfigurationException {
    super(options, filename);
    String sep = options.getSeparator();
    this.separator = sep == null || sep.isEmpty() ? DEFAULT_SEPARATOR : sep.charAt(0);
    String q = options.getQuote();
    this.quote = q == null || q.isEmpty() ? DEFAULT_QUOTE : q.charAt(0);
  }

  @Override
  public LogObject match(String line) {
    List<String> csvItems = parseLine(line, separator, quote);
    if (!csvItems.isEmpty()) {
      Map<String, String> collectedItem = new HashMap<>();
      rowMatches.entrySet().forEach((Entry<String, Integer> rowEntry) -> collectedItem.put(rowEntry.getKey(), csvItems.get(rowEntry.getValue())));
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
          if ('"' != chars[0] && '\"' == customQuote) {
            curVal.append('"');
          }
          //double quotes in column will hit this!
          if (startCollectChar) {
            curVal.append('"');
          }
        } else if (separators == ch) {
          result.add(curVal.toString());
          curVal = new StringBuilder();
          startCollectChar = false;
        } else if ('\r' == ch) {
          //ignore LF characters
        } else if ('\n' == ch) {
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
