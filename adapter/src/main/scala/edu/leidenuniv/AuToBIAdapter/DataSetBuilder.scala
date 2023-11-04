package edu.leidenuniv.AuToBIAdapter

import edu.cuny.qc.speech.AuToBI.core.{AuToBIParameters, FeatureSet, Word}
import edu.leidenuniv.AuToBIAdapter.core.{
  fromArgsString,
  FeatureExtractors,
  FeatureSets,
  InputFiles,
}
import java.util.List as JavaList

class DataSetBuilder(val featureSet: FeatureSet):
  def this(dataPoints: JavaList[Word]) =
    this(FeatureSet())
    featureSet.setDataPoints(dataPoints)

  def this(params: AuToBIParameters) =
    this(InputFiles.getData(params).get)

  def this(params: String) =
    this((new AuToBIParameters()).fromArgsString(params))

  def withDefaultFeatures(name: String): this.type =
    FeatureSets.getFeatureSet(name) match
      case None    =>
        throw new IllegalArgumentException(s"Cannot add features $name, not found")
      case Some(x) => x.getRequiredFeatures.forEach(featureSet.insertRequiredFeature(_))
    this

  def withFeature(name: String): this.type =
    if FeatureExtractors.checkFeaturePossible(name) then
      featureSet.insertRequiredFeature(name)
    else
      throw new IllegalArgumentException(
        s"Cannot add feature $name, no extractor available",
      )
    this

  def withFeatures(names: List[String]): this.type =
    names.foreach(withFeature)
    this

  def withFeatures(names: String): this.type =
    withFeatures(names.split('\t').toList)

  private def build(): Unit =
    FeatureExtractors(featureSet).extractFeatures()
    featureSet.constructFeatures()

  def writeCSV(filename: String): Unit =
    build()
    featureSet.writeCSVFile(filename)

  def writeARFF(filename: String, name: String): Unit =
    build()
    featureSet.writeArff(filename, name)

  def writeLibLinear(filename: String): Unit =
    build()
    featureSet.writeLibLinear(filename)
