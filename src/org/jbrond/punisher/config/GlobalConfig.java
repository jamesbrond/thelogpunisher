package org.jbrond.punisher.config;

import java.util.List;

public class GlobalConfig {

  private String m_name;
  private List<LogConfig> m_logs;

  public void setName(String name) {
    m_name = name;
  }

  public String getName() {
    return m_name;
  }

  public List<LogConfig> getLogs() {
    return m_logs;
  }

  public void setLogs(List<LogConfig> logs) {
    m_logs = logs;
  }
}

// ~@:-]