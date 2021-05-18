package org.jbrond.punisher.logparser.parser;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LogMatcher {

  private final Pattern m_pattern;

  public LogMatcher(String pattern, Map<String, Integer> matches) {
    m_pattern = Pattern.compile(pattern);
  }

  public Matcher match(String str) {
    return null != m_pattern ? m_pattern.matcher(str) : null;
  }

}

// ~@:-]