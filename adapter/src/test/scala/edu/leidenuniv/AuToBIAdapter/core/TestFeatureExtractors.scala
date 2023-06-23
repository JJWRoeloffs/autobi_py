package edu.leidenuniv.AuToBIAdapter.core

import org.scalatest.funsuite.AnyFunSuite
import scala.jdk.CollectionConverters.*

class TestFeatureExtractors extends AnyFunSuite:
  test("All default features can be extracted"):
    for
      featureSetName <- FeatureSets.possibleSets
      // This indeed skips some. That is intentional, those are valid sets, but not instantiable sets
      featureSet <- FeatureSets.getFeatureSet(featureSetName).toList
      extractedFeatureSet = FeatureExtractors(featureSet).extractFeatures()
      _ = extractedFeatureSet.constructFeatures()
      // Nominal values are not in the getFeatures, but sometimes still show up because AuToBI is weirdly programmed
      testableFeatureSet = featureSet.getRequiredFeatures.asScala.filter(!_.contains("nominal_")).toSet
      testableExtractionSet = extractedFeatureSet.getFeatures.asScala.map(_.getName).filter(!_.contains("nominal_")).toSet
      _ = assert(testableFeatureSet == testableExtractionSet)
    yield ()

  test("All default features are valid"):
    for
      featureSetName <- FeatureSets.possibleSets
      featureSet <- FeatureSets.getFeatureSet(featureSetName).toList
      _ = assert(featureSet.getRequiredFeatures.asScala.forall(FeatureExtractors.checkFeaturePossible))
    yield ()