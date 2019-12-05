package org.jbrond.punisher.writer;

import java.io.File;
import java.io.IOException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class OutputWriterFactory {

  private static final Logger L = LogManager.getLogger(OutputWriterFactory.class.getName());

  public static OutTo build(final String filename) throws IOException {
    return build(null == filename || filename.isEmpty() ? null : new File(filename));
  }

  public static OutTo build(final File file) throws IOException {
    if (null == file) {
      // if output is not specified redirect to standard output
      return buildDefault();
    }
    // set other types of writer
    String filename = file.getName();
    String type = filename.substring(filename.lastIndexOf('.') + 1);
    if (null == type) {
      return new OutToTextFile(file);
    }
    type = type.toLowerCase();
    switch (type) {
      case "csv":
        return new OutToCsvFile(file);
      case "txt":
      default:
        return new OutToTextFile(file);
    }
  }

  public static OutTo buildDefault() {
    return new OutToStandardOutput();
  }
}
