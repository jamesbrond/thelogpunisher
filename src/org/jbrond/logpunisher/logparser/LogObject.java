package org.jbrond.logpunisher.logparser;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import org.json.simple.JSONValue;

public class LogObject implements Comparable<LogObject> {

  public static final String MATCHER_KEY_DATE = "date";
  public static final String MATCHER_KEY_MESSAGE = "message";
  public static final String MATCHER_KEY_FILENAME = "filename";

  private static final String OUTPUT_DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";

  private final DateTimeFormatter dateFormatter;

  private final Map<String, Object> logRecord;

  private LogObject(LogObjectBuilder builder) {
    dateFormatter = DateTimeFormatter.ofPattern(OUTPUT_DATE_FORMAT);
    logRecord = builder.logRecord;
  }

  public LocalDateTime getDate() {
    return (LocalDateTime)logRecord.get(MATCHER_KEY_DATE);
  }

  public String getFormatDate(LocalDateTime date) {
    return date != null ? date.format(dateFormatter) : null;
  }

  public String getMessage() {
    return (String)logRecord.get(MATCHER_KEY_MESSAGE);
  }

  public String getFilename() {
    return (String)logRecord.get(MATCHER_KEY_FILENAME);
  }

  public String get(String key) {
    return (String)logRecord.get(key);
  }

  public int size() {
    return logRecord.size();
  }

  public Set<String> keySet() {
    return logRecord.keySet();
  }

  public String format(String format) {
    return replace(format, logRecord);
  }

  private String replace(String str, Map<String, Object> map) {
    StringBuilder sb = new StringBuilder();
    char[] strArray = str.toCharArray();
    int i = 0;
    while (i < strArray.length - 1) {
      if (strArray[i] == '$' && strArray[i + 1] == '{') {
        i = i + 2;
        int begin = i;
        while (strArray[i] != '}') {
          ++i;
        }
        Object obj = map.get(str.substring(begin, i++));
        if (null == obj) {
          sb.append('\t');
        } else if (obj instanceof LocalDateTime) {
          sb.append(getFormatDate((LocalDateTime)obj));
        } else {
          sb.append(obj);
        }
      } else {
        sb.append(strArray[i]);
        ++i;
      }
    }
    if (i < strArray.length) {
      sb.append(strArray[i]);
    }
    return sb.toString();
  }

  @Override
  public String toString() {
    StringBuilder str = new StringBuilder()
        .append(getFormatDate(getDate()))
        .append(' ').append(getMessage())
        .append("; ");
    logRecord.entrySet().stream().forEach(x -> str.append(x.getKey()).append(": ").append(x.getValue()).append("; "));
    str.append(' ').append('[').append(getFilename()).append(']');

    return str.toString();
  }

  public String toJSON() {
    Map<String, String> m = new HashMap<>();
    m.put(MATCHER_KEY_DATE, getFormatDate(getDate()));
    m.put(MATCHER_KEY_MESSAGE, getMessage());
    logRecord.entrySet().stream().forEach(x -> m.put(x.getKey(), get(x.getKey())));
    return JSONValue.toJSONString(m);
  }

  public boolean filter(Map<String, String> filters) {
    return filters.entrySet().stream().noneMatch(f -> (!f.getValue().equals(get(f.getKey().toLowerCase()))));
  }

  @Override
  public int compareTo(LogObject t) {
    LocalDateTime d1 = getDate();
    LocalDateTime d2 = t.getDate();
    if (null == d1) {
      return d2 == null ? 0 : -1;
    }
    if (null == d2) {
      return 1;
    }
    return d1.compareTo(d2);
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == null) {
      return false;
    }

    if (this.getClass() != obj.getClass()) {
      return false;
    }

    LocalDateTime d1 = getDate();
    LocalDateTime d2 = ((LogObject) obj).getDate();
    return null != d1 && null != d2 && d1.isEqual(d2);
  }

  @Override
  public int hashCode() {
    LocalDateTime d1 = getDate();
    return d1.hashCode();
  }

  /**
   * LogObject Builder
   */
  public static class LogObjectBuilder {

    private final Map<String, Object> logRecord = new HashMap<>();

    public final LogObjectBuilder setDate(final LocalDateTime date) {
      this.logRecord.put(MATCHER_KEY_DATE, date);
      return this;
    }

    public final LogObjectBuilder setMessage(final String message) {
      this.logRecord.put(MATCHER_KEY_MESSAGE, message);
      return this;
    }

    public final LogObjectBuilder setFilename(final String filename) {
      this.logRecord.put(MATCHER_KEY_FILENAME, filename);
      return this;
    }

    public LogObjectBuilder set(final String key, final String value) {
      if (null != key) {
        logRecord.put(key.toLowerCase(), value);
      }
      return this;
    }

    public LogObject build() {
      return new LogObject(this);
    }
  }
}

// ~@:-]
