/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jbrond.punisher.config;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Pattern;

public class LogOptionsConfig {

  private String m_dateformat;
  private Locale m_locale;
  private Pattern m_pattern;
  private String m_separator;
  private String m_quote;
  private List<LogFiltersConfig> m_filters;
  private Map<String, Integer> m_matches;
    private List<LogFiltersConfig> m_details;
  

  public LogOptionsConfig() {
    m_locale = Locale.ENGLISH;
  }
  
  public String getDateformat() {
    return m_dateformat;
  }
  
  public SimpleDateFormat getSimpleDateformat() {
    return new SimpleDateFormat(m_dateformat, m_locale);
  }

  public void setDateformat(String dateformat) {
    m_dateformat = dateformat;
  }

  public String getLocale() {
    return null != m_locale ? m_locale.getLanguage(): null;
  }

  public void setLocale(String locale) {
    m_locale = new Locale(locale);
  }

  public String getPattern() {
    return null != m_pattern ? m_pattern.toString() : null;
  }

  public void setPattern(String pattern) {
    m_pattern = null != pattern ? Pattern.compile(pattern) : null;
  }
  
  public Pattern getCompiledPattern() {
    return m_pattern;
  }

  public String getSeparator() {
    return m_separator;
  }

  public void setSeparator(String separator) {
    m_separator = separator;
  }

  public String getQuote() {
    return m_quote;
  }

  public void setQuote(String quote) {
    m_quote = quote;
  }

  public List<LogFiltersConfig> getFilters() {
    return m_filters;
  }

  public void setFilters(List<LogFiltersConfig> filters) {
    m_filters = filters;
  }
  
   public List<LogFiltersConfig> getDetails() {
    return m_details;
  }

  public void setDetails(List<LogFiltersConfig> details) {
    m_details = details;
  }

  public Map<String, Integer> getMatches() {
    return m_matches;
  }

  public void setMatches(Map<String, Integer> matches) {
    m_matches = matches;
  }
}
