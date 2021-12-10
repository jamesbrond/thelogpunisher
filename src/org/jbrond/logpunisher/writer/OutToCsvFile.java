package org.jbrond.logpunisher.writer;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Set;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jbrond.logpunisher.logparser.LogAnalyzer;
import org.jbrond.logpunisher.logparser.LogObject;

public class OutToCsvFile extends OutToTextFile implements OutTo {

  private static final String CSV_SEP = ",";
  private static final String CSV_DEL = "'";

  private static final Logger L = LogManager.getLogger(OutToCsvFile.class.getName());

  public OutToCsvFile(File file, String format) throws IOException {
    super(file, format);
  }

  @Override
  public void write(LogAnalyzer analayzer) throws IOException {
    if (null == writer) {
      throw new IOException();
    }
    List<LogObject> collection = analayzer.get();
    if (collection.size() > 0) {
      try ( BufferedWriter out = new BufferedWriter(writer)) {
        StringBuilder buffer = new StringBuilder();
        Set<String> keys = collection.get(0).keySet();

        buffer.append(CSV_DEL).append("Date").append(CSV_DEL)
            .append(CSV_SEP)
            .append(CSV_DEL).append("Message").append(CSV_DEL);
        keys.stream().forEach(k -> buffer.append(CSV_SEP).append(CSV_DEL).append(k).append(CSV_DEL));
        buffer.append(CSV_SEP)
            .append(CSV_DEL).append("Log file").append(CSV_DEL);
        out.append(buffer);
        out.newLine();

        collection.stream().forEach((LogObject x) -> {
          try {
            StringBuilder b = new StringBuilder();
            b.append(CSV_DEL).append(x.getFormatDate(x.getDate())).append(CSV_DEL)
                .append(CSV_SEP)
                .append(CSV_DEL).append(x.getMessage()).append(CSV_DEL);
            keys.stream().forEach(k -> buffer.append(CSV_SEP).append(CSV_DEL).append(x.get(k)).append(CSV_DEL));
            b.append(CSV_SEP)
                .append(CSV_DEL).append(x.getFilename()).append(CSV_DEL);
            out.append(b);
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

}

// ~@:-]
