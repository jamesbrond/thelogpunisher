package org.jbrond.logpunisher.writer;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jbrond.logpunisher.logparser.LogAnalyzer;
import org.jbrond.logpunisher.logparser.LogObject;

public class OutToTextFile implements OutTo {

  private static final Logger L = LogManager.getLogger(OutToTextFile.class.getName());
  protected final Writer writer;
  protected final String formatString;

  public OutToTextFile() throws IOException {
    this(File.createTempFile("temp", null), null);
  }

  public OutToTextFile(File file, String format) throws IOException {
    this(new FileWriter(file), format);
    L.info("Output file {}", file.getAbsolutePath());
  }

  public OutToTextFile(OutputStreamWriter os, String format) {
    writer = os;
    formatString = format;
  }

  @Override
  public void write(LogAnalyzer analayzer) throws IOException {
    if (null == writer) {
      throw new IOException();
    }
    try ( BufferedWriter out = new BufferedWriter(writer)) {
      analayzer.get().stream().forEach((LogObject x) -> {
        try {
          out.write(x.format(formatString));
          out.newLine();
        } catch (IOException e) {
          L.warn(e);
        }
      });
      out.flush();
    } finally {
      writer.close();
    }
  }

}

// ~@:-]
