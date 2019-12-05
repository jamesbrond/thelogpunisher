package org.jbrond.punisher.writer;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.logging.Level;
import java.util.stream.Stream;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jbrond.punisher.logparser.LogObject;

public class OutToTextFile implements OutTo {

  private static final Logger L = LogManager.getLogger(OutToTextFile.class.getName());
  private Writer m_writer;

  public OutToTextFile(File file) {
    try {
      m_writer = new FileWriter(file);
    } catch (IOException e) {
      L.error(e);
      L.catching(e);
      m_writer = null;
    }
  }

  @Override
  public void write(Stream<LogObject> stream) throws IOException {
    if (null == m_writer) {
      throw new IOException();
    }
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
