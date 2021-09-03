package org.jbrond.logpunisher.config;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

public class ConfigurationFactory {

  private ConfigurationFactory() {
    throw new IllegalStateException("Cannot call ConfigurationFactory constructor");
  }

  public static GlobalConfig read(String filename) throws IOException {
    return read(new File(filename));
  }

  public static GlobalConfig read(File file) throws IOException {
    try ( InputStream is = new FileInputStream(file)) {
      Configuration parser = new ConfigurationYaml();
      return parser.parse(is, file.getParent());
    }
  }
}

// ~@:-]
