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

  private static final String OUTPUT_DATE_FORMAT = "yyyy-MM-dd hh:mm:ss";

  private final DateTimeFormatter dateFormatter;

  private final String filename;
  private final LocalDateTime date;
  private final String message;
  private final Map<String, String> logRecord;

  private LogObject(LogObjectBuilder builder) {
    dateFormatter = DateTimeFormatter.ofPattern(OUTPUT_DATE_FORMAT);
    logRecord = builder.logRecord;

    date = builder.date;
    message = builder.message;
    filename = builder.filename;
  }

  public LocalDateTime getDate() {
    return date;
  }

  public String getFormatDate() {
    return date != null ? date.format(dateFormatter) : null;
  }

  public String getMessage() {
    return message;
  }

  public String getFilename() {
    return filename;
  }

  public String get(String key) {
    return logRecord.get(key);
  }

  public int size() {
    return logRecord.size();
  }

  public Set<String> keySet() {
    return logRecord.keySet();
  }

  @Override
  public String toString() {
    StringBuilder str = new StringBuilder()
        .append(getFormatDate())
        .append(' ').append(getMessage())
        .append("; ");
    logRecord.entrySet().stream().forEach(x -> str.append(x.getKey()).append(": ").append(x.getValue()).append("; "));
    str.append(' ').append('[').append(getFilename()).append(']');

    return str.toString();
  }

  public String toJSON() {
    Map<String, String> m = new HashMap<>();
    m.put(MATCHER_KEY_DATE, getFormatDate());
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

    private final Map<String, String> logRecord = new HashMap<>();

    private LocalDateTime date;
    private String message;
    private String filename;

    public final LogObjectBuilder setDate(final LocalDateTime date) {
      this.date = date;
      return this;
    }

    public final LogObjectBuilder setMessage(final String message) {
      this.message = message;
      return this;
    }

    public final LogObjectBuilder setFilename(final String filename) {
      this.filename = filename;
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
