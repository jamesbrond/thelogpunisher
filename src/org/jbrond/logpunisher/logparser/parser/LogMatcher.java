package org.jbrond.logpunisher.logparser.parser;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LogMatcher {

  private final Pattern pattern;

  public LogMatcher(String pattern) {
    this.pattern = Pattern.compile(pattern);
  }

  public Matcher match(String str) {
    return null != pattern ? pattern.matcher(str) : null;
  }

}

// ~@:-]