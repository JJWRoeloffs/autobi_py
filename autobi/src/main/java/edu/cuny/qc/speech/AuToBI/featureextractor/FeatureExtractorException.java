/*  FeatureExtractorException.java

    Copyright (c) 2009-2014 Andrew Rosenberg

  This file is part of the AuToBI prosodic analysis package.

  AuToBI is free software: you can redistribute it and/or modify
  it under the terms of the Apache License (see boilerplate below)

 ***********************************************************************************************************************
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 in compliance with
 * the License. You should have received a copy of the Apache 2.0 License along with AuToBI.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See
 the License for the
 * specific language governing permissions and limitations under the License.
 *
 ***********************************************************************************************************************
 */
package edu.cuny.qc.speech.AuToBI.featureextractor;

/**
 * FeatureExtractorException is a wrapper around Throwable to differentiate Exceptions that come
 * from FeatureExtractor objects.
 */
public class FeatureExtractorException extends Throwable {
  /**
   * Constructs a new FeatureExtractorException with a message.
   *
   * @param s the message.
   */
  public FeatureExtractorException(String s) {
    super(s);
  }
}
