package edu.leidenuniv.AuToBIAdapter

import edu.cuny.qc.speech.AuToBI.core.FeatureSet
import edu.leidenuniv.AuToBIAdapter.core.{FeatureExtractors, FeatureSets}
import scala.jdk.CollectionConverters.*

class FeatureNamesBuilder(val featureSet: FeatureSet):
  def this() = this(FeatureSet())

  def withDefaultFeatures(name: String): this.type =
    FeatureSets.getFeatureSet(name) match
      case None    =>
        throw new IllegalAccessException(s"Cannot add features $name, not found")
      case Some(x) => x.getRequiredFeatures.forEach(featureSet.insertRequiredFeature(_))
    this

  def withFeature(name: String): this.type =
    if FeatureExtractors.checkFeaturePossible(name) then
      println(s"Adding: $name")
      featureSet.insertRequiredFeature(name)
    else
      println(s"cannot add $name")
      throw new IllegalAccessException(
        s"Cannot add feature $name, no extractor available",
      )
    this

  def withFeatures(names: List[String]): this.type =
    names.foreach(withFeature)
    this

  def withFeatures(names: String): this.type =
    withFeatures(names.split('\t').toList)

  def build(): String =
    featureSet.getRequiredFeatures.asScala.mkString("\t")
