package org.jbrond.logpunisher.config;

import java.io.IOException;
import java.io.InputStream;
import org.yaml.snakeyaml.TypeDescription;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;

/**
 * Parse YAML file to get configuration
 */
public class ConfigurationYaml implements Configuration {

  @Override
  public GlobalConfig parse(InputStream inputStream, String basePath) throws IOException {
    Constructor yamlConstructor = new Constructor(GlobalConfig.class);
    TypeDescription customTypeDescription = new TypeDescription(GlobalConfig.class);
    customTypeDescription.addPropertyParameters("options", LogOptionsConfig.class);
    customTypeDescription.addPropertyParameters("filters", LogFiltersConfig.class);
    yamlConstructor.addTypeDescription(customTypeDescription);
    Yaml yaml = new Yaml(yamlConstructor);
    return yaml.load(inputStream);
  }
}

// ~@:-]