package org.jbrond.logpunisher.config;

import java.util.Map;
import java.util.regex.Pattern;

public class LogFiltersConfig {

  private Pattern pattern;
  private Map<String, Integer> matches;

  public String getPattern() {
    return null != pattern ? pattern.toString() : null;
  }

  public void setPattern(String pattern) {
    this.pattern = null != pattern ? Pattern.compile(pattern) : null;
  }

  public Pattern getComiledPattern() {
    return pattern;
  }

  public Map<String, Integer> getMatches() {
    return matches;
  }

  public void setMatches(Map<String, Integer> matches) {
    this.matches = matches;
  }
}

// ~@:-]