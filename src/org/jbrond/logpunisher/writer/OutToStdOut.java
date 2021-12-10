package org.jbrond.logpunisher.writer;

import java.io.OutputStreamWriter;

public class OutToStdOut extends OutToTextFile implements OutTo {

  public OutToStdOut(String format)  {
    super(new OutputStreamWriter(System.out), format);
  }
}

// ~@:-]