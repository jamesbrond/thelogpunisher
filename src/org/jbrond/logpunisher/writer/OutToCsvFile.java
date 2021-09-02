package org.jbrond.punisher.writer;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.util.stream.Stream;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jbrond.punisher.logparser.LogObject;

public class OutToCsvFile extends OutToTextFile implements OutTo {

  private static final String CSV_SEP = ",";
  private static final String CSV_DEL = "'";

  private static final Logger L = LogManager.getLogger(OutToCsvFile.class.getName());

  public OutToCsvFile(File file) throws IOException {
    super(file);
  }

  @Override
  public void write(Stream<LogObject> stream) throws IOException {
    if (null == m_writer) {
      throw new IOException();
    }
    try (BufferedWriter out = new BufferedWriter(m_writer)) {
      out.append(CSV_DEL).append("Date").append(CSV_DEL)
              .append(CSV_SEP)
              .append(CSV_DEL).append("Message").append(CSV_DEL)
              .append(CSV_SEP)
              .append(CSV_DEL).append("IP").append(CSV_DEL)
              .append(CSV_SEP)
              .append(CSV_DEL).append("User").append(CSV_DEL)
              .append(CSV_SEP)
              .append(CSV_DEL).append("Session").append(CSV_DEL)
              .append(CSV_SEP)
              .append(CSV_DEL).append("Log file").append(CSV_DEL);
      out.newLine();

      stream.forEach((LogObject x) -> {
        try {
          out.append(CSV_DEL).append(x.getFormatDate()).append(CSV_DEL)
                  .append(CSV_SEP)
                  .append(CSV_DEL).append(x.getMessage()).append(CSV_DEL)
                  .append(CSV_SEP)
                  .append(CSV_DEL).append(x.getIp()).append(CSV_DEL)
                  .append(CSV_SEP)
                  .append(CSV_DEL).append(x.getUser()).append(CSV_DEL)
                  .append(CSV_SEP)
                  .append(CSV_DEL).append(x.getSession()).append(CSV_DEL)
                  .append(CSV_SEP)
                  .append(CSV_DEL).append(x.getFilename()).append(CSV_DEL);
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
