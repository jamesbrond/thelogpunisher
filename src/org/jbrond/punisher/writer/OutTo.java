package org.jbrond.punisher.writer;

import java.io.IOException;
import java.util.stream.Stream;
import org.jbrond.punisher.logparser.LogObject;

public interface OutTo {

  public void write(Stream<LogObject> stream) throws IOException;
}
