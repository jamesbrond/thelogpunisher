package org.jbrond.logpunisher.writer;

import java.io.File;
import java.io.IOException;

public class OutputWriterFactory {

  private OutputWriterFactory() {
    throw new IllegalStateException();
  }

  public static OutTo build(final String filename) throws IOException {
    return OutputWriterFactory.build(null == filename || filename.isEmpty() ? null : new File(filename));
  }

  public static OutTo build(final File file) throws IOException {
    if (null == file) {
      // if output is not specified redirect to standard output
      return build();
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
      case "log":
      default:
        return new OutToTextFile(file);
    }
  }

  public static OutTo build() {
    return new OutToStdOut();
  }
}

// ~@:-]
