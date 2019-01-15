/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jbrond.punisher.logparser.parser;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LogMatcher {

  private final Pattern m_pattern;
  private Map<String, Integer> m_matches;
  private Matcher m_matcher;

  public LogMatcher(String pattern, Map<String, Integer> matches) {
    m_pattern = Pattern.compile(pattern);
  }

  public Matcher match(String str) {
    return null != m_pattern ? m_pattern.matcher(str) : null;
  }
  
}
