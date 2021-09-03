package org.jbrond.logpunisher.config;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

public interface Configuration {

  public default GlobalConfig parse(String filename) throws IOException {
    return parse(new File(filename));
  }

  public default GlobalConfig parse(File file) throws IOException {
    try (InputStream inputStream = new FileInputStream(file)) {
      return parse(inputStream, file.getParent());
    }
  }

  public GlobalConfig parse(InputStream inputStream, String basePath) throws IOException;
}

// ~@:-]