package org.jbrond.logpunisher.writer;

import java.io.IOException;
import org.jbrond.logpunisher.logparser.LogAnalyzer;

public interface OutTo {

  public void write(LogAnalyzer analayzer) throws IOException;
}

// ~@:-]