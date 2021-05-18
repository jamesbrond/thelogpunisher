package org.jbrond.punisher.writer;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.stream.Stream;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jbrond.punisher.logparser.LogObject;

public class OutToTextFile implements OutTo {

  private static final Logger L = LogManager.getLogger(OutToTextFile.class.getName());
  protected final Writer m_writer;

  public OutToTextFile() throws IOException {
    this(File.createTempFile("temp", null));
  }

  public OutToTextFile(File file) throws IOException {
    this(new FileWriter(file));
    L.info("Output file {}", file.getAbsolutePath());
  }

  public OutToTextFile(OutputStreamWriter os) {
    m_writer = os;
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

// ~@:-]