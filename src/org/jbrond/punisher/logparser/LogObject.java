package org.jbrond.punisher.logparser;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

import org.json.simple.JSONValue;

public class LogObject {

  public final static String MATCHER_KEY_DATE = "date";
  public final static String MATCHER_KEY_IP = "ip";
  public final static String MATCHER_KEY_MESSAGE = "message";
  public final static String MATCHER_KEY_SESSION = "session";
  public final static String MATCHER_KEY_USER = "user";

  private final static String OUTPUT_DATE_FORMAT = "yyyy-MM-dd hh:mm:ss";

  private final DateTimeFormatter m_dateformat;

  private final String m_filename;
  private final LocalDateTime m_date;
  private final String m_ip;
  private final String m_message;
  private final String m_user;
  private final String m_session;

  private LogObject(Builder builder) {
    m_dateformat = DateTimeFormatter.ofPattern(OUTPUT_DATE_FORMAT);

    m_date = builder.m_date;
    m_ip = builder.m_ip;
    m_message = builder.m_message;
    m_user = builder.m_user;
    m_session = builder.m_session;
    m_filename = builder.m_filename;
  }

  public LocalDateTime getDate() {
    return m_date;
  }

  public String getFormatDate() {
    return m_date != null ? m_date.format(m_dateformat) : null;
  }

  public String getMessage() {
    return m_message;
  }

  public String getIp() {
    return m_ip;
  }

  public String getUser() {
    return null != m_user ? m_user.toUpperCase() : null;
  }

  public String getSession() {
    return m_session;
  }
  
  public String getFilename() {
    return m_filename;
  }

  public String get(String key) {
    switch (key) {
      case MATCHER_KEY_DATE:
        return getFormatDate();
      case MATCHER_KEY_MESSAGE:
        return getMessage();
      case MATCHER_KEY_IP:
        return getIp();
      case MATCHER_KEY_USER:
        return getUser();
      case MATCHER_KEY_SESSION:
        return getSession();
      
      default:
        return null;
    }
  }

  @Override
  public String toString() {
    StringBuilder str = new StringBuilder()
            .append(getFormatDate())
            .append(' ').append(getMessage());
    if (null != m_ip) {
      str.append(' ').append("from ").append(getIp());
    }
    if (null != m_user) {
      str.append(' ').append("by ").append(getUser());
    }
    if (null != m_session) {
      str.append(' ').append("JSESSIONID: ").append(getSession());
    }
    str.append(' ').append('[').append(getFilename()).append(']');

    return str.toString();
  }

  public String toJSON() {
    Map<String, String> m = new HashMap<>();
    m.put("date", getFormatDate());
    m.put("ip", getIp());
    m.put("message", getMessage());
    m.put("user", getUser());
    m.put("session", getSession());
    m.put("filename", getFilename());
    return JSONValue.toJSONString(m);
  }

  public boolean filter(Map<String, String> filters) {
    return !filters.entrySet().stream().noneMatch((f) -> (!f.getValue().equals(get(f.getKey().toLowerCase()))));
  }

  /**
   * LogObject Builder
   */
  public static class Builder {

    private static final Pattern VALID_IP_PATTERN = Pattern.compile("^(([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\.){3}([01]?\\d\\d?|2[0-4]\\d|25[0-5])$");

    private LocalDateTime m_date;
    private String m_ip;
    private String m_message;
    private String m_user;
    private String m_session;
    private String m_filename;

    public final Builder setDate(final Date date) {
      m_date = null != date ? date.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime() : null;
      return this;
    }

    public final Builder setIp(final String ip) {
      if (null == ip) {
        m_ip = null;
      } else {
        m_ip = VALID_IP_PATTERN.matcher(ip).matches() ? ip : null;
      }
      return this;
    }

    public final Builder setMessage(final String message) {
      m_message = message;
      return this;
    }

    public final Builder setUser(final String user) {
      m_user = user;
      return this;
    }

    public final Builder setSession(final String session) {
      m_session = session;
      return this;
    }
    
    public final Builder setFilename(final String filename) {
      m_filename = filename;
      return this;
    }

    public Builder set(final String key, final Object value) {
      switch (key) {
        case MATCHER_KEY_DATE:
          setDate((Date) value);
          break;
        case MATCHER_KEY_MESSAGE:
          setMessage((String) value);
          break;
        case MATCHER_KEY_IP:
          setIp((String) value);
          break;
        case MATCHER_KEY_USER:
          setUser((String) value);
          break;
        case MATCHER_KEY_SESSION:
          setSession((String) value);
          break;
      }
      return this;
    }

    public LogObject build() {
      return new LogObject(this);
    }
  }
}

// ~@:-]
