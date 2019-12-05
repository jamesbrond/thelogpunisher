package org.jbrond.punisher.writer;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.stream.Stream;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jbrond.punisher.logparser.LogObject;

public class OutToStandardOutput implements OutTo {

  private static final Logger L = LogManager.getLogger(OutToStandardOutput.class.getName());
  private final Writer m_writer;

  public OutToStandardOutput() {
    m_writer = new OutputStreamWriter(System.out);
  }

  @Override
  public void write(Stream<LogObject> stream) throws IOException {
    try (BufferedWriter out = new BufferedWriter(m_writer)) {
      stream.forEach((LogObject x) -> {
        try {
          out.write(x.toString());
          out.newLine();
        } catch (IOException e) {
          L.warn(e);
        }
      });
      out.flush();
    } finally {
      m_writer.close();
    }
  }
}
