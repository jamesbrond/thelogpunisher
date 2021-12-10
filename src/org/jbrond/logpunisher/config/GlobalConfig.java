package org.jbrond.logpunisher.config;

import java.util.List;

public class GlobalConfig {

  private String name;
  private List<LogConfig> logs;
  private String format = "${date} ${user} ${session} ${ip} ${message} [${filename}]";

  public void setName(String name) {
    this.name = name;
  }

  public String getName() {
    return name;
  }

  public List<LogConfig> getLogs() {
    return logs;
  }

  public void setLogs(List<LogConfig> logs) {
    this.logs = logs;
  }

  public String getFormat() {
    return format;
  }

  public void setFormat(String format) {
    this.format = format;
  }
}

// ~@:-]