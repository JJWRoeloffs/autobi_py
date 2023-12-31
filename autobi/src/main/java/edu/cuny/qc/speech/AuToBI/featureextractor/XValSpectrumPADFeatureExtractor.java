/*  XValSpectrumPADFeatureExtractor.java

    Copyright 2009-2014 Andrew Rosenberg

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

import edu.cuny.qc.speech.AuToBI.classifier.AuToBIClassifier;
import edu.cuny.qc.speech.AuToBI.classifier.WekaClassifier;
import edu.cuny.qc.speech.AuToBI.core.*;
import edu.cuny.qc.speech.AuToBI.util.PartitionUtils;
import java.util.ArrayList;
import java.util.List;
import weka.classifiers.trees.J48;

/**
 * The XValSpectrumPADFeatureExtractor is used to generate cross vaidated predictions of spectral
 * pitch accent detection (PAD) hypotheses.  These xval hypotheses are used in training correction
 * classfiers. <p/> This has been deprecated because it does not conform to the AuToBI v1.4 feature
 * naming conventions
 */
@Deprecated
public class XValSpectrumPADFeatureExtractor extends FeatureExtractor {
  public static final String moniker = "barkPred,barkPredConf,barkPredConfAcc";

  private final String ACCENTED_VALUE = "ACCENTED"; // the accented value
  private final String FOLD_ASSIGNMENT_FEATURE = "FOLD_ASSIGNMENT";
  // an feature to store fold assignment information on
  private final int low; // the bottom of the frequency region
  private final int high; // the top of the frequency region
  private final int num_folds; // the number of folds used in the hypothesis generation.
  private final FeatureSet fs; // a description of the featureset used in the prediction

  /**
   * Constructs a new XValSpectrumFeatureExtractor for a specific spectral region.
   *
   * @param low       the bottom of the spectral region.
   * @param high      the top of the spectral region.
   * @param num_folds the number of folds to be used in the generation
   * @param fs        a feature set used in the xval experiments
   */
  public XValSpectrumPADFeatureExtractor(int low, int high, int num_folds, FeatureSet fs) {
    this.low = low;
    this.high = high;
    this.num_folds = num_folds;
    this.fs = fs;

    extracted_features.add("nominal_bark_" + low + "_" + high + "__prediction");
    extracted_features.add("bark_" + low + "_" + high + "__prediction_confidence");
    extracted_features.add("bark_" + low + "_" + high + "__prediction_confidence_accented");

    required_features.addAll(fs.getRequiredFeatures());
    required_features.add(fs.getClassAttribute());
  }
  public void extractFeatures(List<Region> regions) throws FeatureExtractorException {
    throw new UnsupportedOperationException();
  }
  /**
   * Generates cross validated pitch accent detection hypotheses using energy information drawn from
   * the assigned spectral region.
   *
   * @param regions The regions to extract features from.
   * @throws FeatureExtractorException if something goes wrong.
   */
  public void extractFeaturesWord(List<Word> words) throws FeatureExtractorException {
    // Construct a feature set.
    FeatureSet feature_set = fs.newInstance();

    // Extract spectrum features.
    feature_set.setDataPoints(words);
    try {
      PartitionUtils.assignStratifiedFoldNum(
          words, FOLD_ASSIGNMENT_FEATURE, num_folds, feature_set.getClassAttribute());
    } catch (AuToBIException e) {
      throw new FeatureExtractorException(e.getMessage());
    }

    // Train n-fold cross validated prediction features.
    for (int fold_num = 0; fold_num < num_folds; ++fold_num) {
      AuToBIClassifier classifier = new WekaClassifier(new J48());

      FeatureSet training_fs = fs.newInstance();

      List<Word> training_regions = new ArrayList<>();
      List<Word> testing_regions = new ArrayList<>();
      try {
        PartitionUtils.splitData(
            words, training_regions, testing_regions, fold_num, FOLD_ASSIGNMENT_FEATURE);
      } catch (AuToBIException e) {
        // This should never happen.  If there is a problem with splitData it would have thrown
        // an exception during the fold assignment method assignStratifiedFoldNum
        throw new FeatureExtractorException(e.getMessage());
      }
      training_fs.setDataPoints(training_regions);
      training_fs.constructFeatures();
      try {
        classifier.train(training_fs);
      } catch (Exception e) {
        throw new FeatureExtractorException(e.getMessage());
      }

      for (Word w : testing_regions) {
        try {
          Distribution result = classifier.distributionForInstance(w);

          w.setAttribute(
              "nominal_bark_" + low + "_" + high + "__prediction", result.getKeyWithMaximumValue());
          w.setAttribute("bark_" + low + "_" + high + "__prediction_confidence",
              result.get(result.getKeyWithMaximumValue()));
          w.setAttribute("bark_" + low + "_" + high + "__prediction_confidence_accented",
              result.get(ACCENTED_VALUE));
        } catch (Exception e) {
          throw new FeatureExtractorException(e.getMessage());
        }
      }
    }
  }
}