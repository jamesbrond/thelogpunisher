/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jbrond.punisher.config;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ConfigurationFactory {

  private static final Logger L = LogManager.getLogger(ConfigurationFactory.class.getName());

  public static GlobalConfig read(String filename) throws IOException {
    return read(new File(filename));
  }

  public static GlobalConfig read(File file) throws IOException {
    try (InputStream is = new FileInputStream(file)) {
      Configuration parser = getParser(file);
      return null == parser ? null : parser.parse(is, file.getParent());
    }
  }

  private static Configuration getParser(final File file) throws IOException {
    String type = Files.probeContentType(file.toPath());
    if (null == type) {
      String filename = file.getName();
      String ext = filename.substring(filename.lastIndexOf('.') + 1).toLowerCase();
      if ("yaml".equals(ext) || "yml".equals(ext)) {
        type = "test/yaml";
      } else {
        return null;
      } 
    }
    L.trace("guess configuration mimetype: {}", type);
    switch (type) {
      case "test/yaml":
        return new ConfigurationYaml();
      default:
        return null;
    }
  }

}
