package org.jbrond.logpunisher.config;

public class LogConfig {

  private String type;
  private String file;
  private LogOptionsConfig options;

  public void setType(String type) {
    this.type = type;
  }

  public String getType() {
    return type;
  }

  public void setFile(String file) {
    this.file = file;
  }

  public String getFile() {
    return file;
  }

  public LogOptionsConfig getOptions() {
    return options;
  }

  public void setOptions(LogOptionsConfig options) {
    this.options = options;
  }

}

// ~@:-]