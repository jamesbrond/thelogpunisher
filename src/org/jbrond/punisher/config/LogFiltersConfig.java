package org.jbrond.punisher.config;

import java.util.Map;
import java.util.regex.Pattern;

public class LogFiltersConfig {

  private Pattern m_pattern;
  private Map<String, Integer> m_matches;

  public String getPattern() {
    return null != m_pattern ? m_pattern.toString() : null;
  }

  public void setPattern(String pattern) {
    m_pattern = null != pattern ? Pattern.compile(pattern) : null;
  }

  public Pattern getComiledPattern() {
    return m_pattern;
  }

  public Map<String, Integer> getMatches() {
    return m_matches;
  }

  public void setMatches(Map<String, Integer> matches) {
    m_matches = matches;
  }
}
