package edu.cuny.qc.speech.AuToBI;

import java.io.File;
import java.net.*;
import java.nio.file.Paths;

/**
 * A hackfix to get the tests to run. Originally, this project was compiled with
 * Apache Ant, and used an envoirement variable to find the test resources
 * directory With this code, the original systems can remain largely unmodified
 * while still using the proper sbt resources management.
 */
public class ResourcePath {
  public static String getResourcePath(String relative) {
    URL res = ResourcePath.class.getClassLoader().getResource(relative);
    try {
      File file = Paths.get(res.toURI()).toFile();
      return file.getAbsolutePath();
    } catch (URISyntaxException e) {
      throw new RuntimeException("Cannot find file: " + relative);
    }
  }
}
