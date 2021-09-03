package org.jbrond.logpunisher.writer;

import java.io.OutputStreamWriter;

public class OutToStdOut extends OutToTextFile implements OutTo {

  public OutToStdOut()  {
    super(new OutputStreamWriter(System.out));
  }
}

// ~@:-]