package org.jbrond.logpunisher.config;

import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.temporal.ChronoField;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Pattern;

public class LogOptionsConfig {

  private String dateFormat;
  private Locale locale;
  private Pattern pattern;
  private String separator;
  private String quote;
  private List<LogFiltersConfig> filters;
  private Map<String, Integer> matches;
  private List<LogFiltersConfig> details;

  public LogOptionsConfig() {
    locale = Locale.ENGLISH;
  }

  public String getDateformat() {
    return dateFormat;
  }

  public DateTimeFormatter getDateTimeFormat() {
    return new DateTimeFormatterBuilder().appendPattern(dateFormat)
        .parseDefaulting(ChronoField.HOUR_OF_DAY, 0)
        .parseDefaulting(ChronoField.MINUTE_OF_HOUR, 0)
        .parseDefaulting(ChronoField.SECOND_OF_MINUTE, 0)
        .toFormatter(locale);
  }

  public void setDateformat(String dateformat) {
    dateFormat = dateformat;
  }

  public String getLocale() {
    return null != locale ? locale.getLanguage() : null;
  }

  public void setLocale(String locale) {
    this.locale = new Locale(locale);
  }

  public String getPattern() {
    return null != pattern ? pattern.toString() : null;
  }

  public void setPattern(String pattern) {
    this.pattern = null != pattern ? Pattern.compile(pattern) : null;
  }

  public Pattern getCompiledPattern() {
    return pattern;
  }

  public String getSeparator() {
    return separator;
  }

  public void setSeparator(String separator) {
    this.separator = separator;
  }

  public String getQuote() {
    return quote;
  }

  public void setQuote(String quote) {
    this.quote = quote;
  }

  public List<LogFiltersConfig> getFilters() {
    return filters;
  }

  public void setFilters(List<LogFiltersConfig> filters) {
    this.filters = filters;
  }

  public List<LogFiltersConfig> getDetails() {
    return details;
  }

  public void setDetails(List<LogFiltersConfig> details) {
    this.details = details;
  }

  public Map<String, Integer> getMatches() {
    return matches;
  }

  public void setMatches(Map<String, Integer> matches) {
    this.matches = matches;
  }
}

// ~@:-]
