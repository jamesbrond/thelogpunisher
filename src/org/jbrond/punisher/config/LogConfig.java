/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jbrond.punisher.config;

public class LogConfig {

  private String m_type;
  private String m_file;
  private LogOptionsConfig m_options;

  public void setType(String type) {
    m_type = type;
  }

  public String getType() {
    return m_type;
  }

  public void setFile(String file) {
    m_file = file;
  }

  public String getFile() {
    return m_file;
  }

  public LogOptionsConfig getOptions() {
    return m_options;
  }

  public void setOptions(LogOptionsConfig options) {
    m_options = options;
  }

}
