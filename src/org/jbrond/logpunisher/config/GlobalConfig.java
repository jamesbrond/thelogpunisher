package org.jbrond.logpunisher.config;

import java.util.List;

public class GlobalConfig {

  private String name;
  private List<LogConfig> logs;

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
}

// ~@:-]