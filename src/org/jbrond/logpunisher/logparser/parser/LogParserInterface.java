package org.jbrond.logpunisher.logparser.parser;

import org.jbrond.logpunisher.logparser.LogObject;

public interface LogParserInterface {
  public LogObject match(String line);
}

// ~@:-]