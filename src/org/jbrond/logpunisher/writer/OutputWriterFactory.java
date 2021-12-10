package org.jbrond.logpunisher.writer;

import java.io.File;
import java.io.IOException;

public class OutputWriterFactory {

  private OutputWriterFactory() {
    throw new IllegalStateException();
  }

  public static OutTo build(final String filename, String format) throws IOException {
    return OutputWriterFactory.build(null == filename || filename.isEmpty() ? null : new File(filename), format);
  }

  public static OutTo build(final File file, String format) throws IOException {
    if (null == file) {
      // if output is not specified redirect to standard output
      return build(format);
    }
    // set other types of writer
    String filename = file.getName();
    String type = filename.substring(filename.lastIndexOf('.') + 1);
    if (null == type) {
      return new OutToTextFile(file, format);
    }
    type = type.toLowerCase();
    switch (type) {
      case "csv":
        return new OutToCsvFile(file, format);
      case "txt":
      case "log":
      default:
        return new OutToTextFile(file, format);
    }
  }

  public static OutTo build(String format) {
    return new OutToStdOut(format);
  }
}

// ~@:-]
