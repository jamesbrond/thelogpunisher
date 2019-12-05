package org.jbrond.punisher.logparser.parser;

import org.jbrond.punisher.logparser.LogObject;

public interface LogParserInterface {
  public LogObject match(String line);
}
