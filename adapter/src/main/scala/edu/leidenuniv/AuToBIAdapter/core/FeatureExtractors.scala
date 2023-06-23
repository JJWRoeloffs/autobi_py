package edu.leidenuniv.AuToBIAdapter.core

import edu.cuny.qc.speech.AuToBI.core.{FeatureExtractor, FeatureSet}
import edu.cuny.qc.speech.AuToBI.util.AuToBIUtils
import edu.leidenuniv.AuToBIAdapter.utils.AutobiReflections
import java.lang.reflect.Constructor
import scala.collection.mutable
import scala.jdk.CollectionConverters.*
import scala.util.{Success, Try}

class FeatureExtractors(val featureSet: FeatureSet):
  private val finishedExtractors: mutable.Set[String] = mutable.Set()
  private val requiredFeatures: Set[String]           =
    featureSet.getRequiredFeatures.asScala.toSet

  def extractFeatures(): FeatureSet =
    requiredFeatures.foreach(extractFeature)
    cleanupFeatureSet()
    featureSet

  private def extractFeature(feature: String): Unit =
    if finishedExtractors.add(feature) then extractUnknownFeature(feature)

  private def extractUnknownFeature(feature: String): Unit =
    for
      extractor <- FeatureExtractors.getExtractor(feature)
      _          = extractor.getRequiredFeatures.asScala.foreach(extractFeature)
      _          = extractor.extractFeaturesWord(featureSet.getDataPoints)
    yield ()

  private def cleanupFeatureSet(): Unit =
    for
      garbage <- finishedExtractors --= requiredFeatures
      _        = featureSet.removeFeatureFromDataPoints(garbage)
    yield ()

object FeatureExtractors:
  private def getMonikerFields(c: Class[? <: FeatureExtractor]): Option[List[String]] =
    Try(c.getDeclaredField("moniker").get(c).toString.split(",").toList).toOption

  lazy val extractors: Map[String, Class[? <: FeatureExtractor]] =
    (for
      extractor <- AutobiReflections.ref.getSubTypesOf(classOf[FeatureExtractor]).asScala
      if AutobiReflections.isInstantiable(extractor)
      moniker   <- getMonikerFields(extractor).toList
      field     <- moniker
    yield field -> extractor).toMap

  lazy val possibleFeatures: Set[String] = extractors.keySet

  private def getFeatureConstructor(
    extractor: Class[? <: FeatureExtractor],
    nrParameters: Int,
  ): Option[Constructor[FeatureExtractor]] =
    (for
      cons <- extractor.getConstructors
      if cons.getParameterCount == nrParameters
      if cons.getParameterTypes.forall(_ == classOf[String])
    yield cons).headOption.asInstanceOf[Option[Constructor[FeatureExtractor]]]

  def getExtractor(feature: String): Option[FeatureExtractor] =
    for
      parameters  <- Try(AuToBIUtils.parseFeatureName(feature).asScala).toOption
      extractor   <- extractors.get(parameters.head)
      constructor <- getFeatureConstructor(extractor, parameters.tail.length)
    yield constructor.newInstance(parameters.tail.toArray*)

  private def checkFeaturePossibleAsArgs(feature: String): Boolean =
    Try(AuToBIUtils.parseFeatureName(feature).asScala.toList) match
      case Success(List(x)) if Try(x.toDouble).isSuccess    => true
      case Success(List(x)) if x.matches("f[0-3]b[0-3]")    => true
      case Success(List(x)) if x.matches("[0-9]+ms")        => true
      case Success(List(x)) if x.matches(".+_prediction.*") => true
      case Success(List(x))                                 => possibleFeatures.contains(x)
      case Success(head :: tail)                            =>
        possibleFeatures.contains(head) & tail.forall(checkFeaturePossibleAsArgs)
      case _                                                => false

  def checkFeaturePossible(feature: String): Boolean =
    Try(AuToBIUtils.parseFeatureName(feature).asScala.toList) match
      case Success(List(x)) if x.matches(".+_prediction.*") => true
      case Success(List(x))                                 => possibleFeatures.contains(x)
      case Success(head :: tail)                            =>
        possibleFeatures.contains(head) & tail.forall(checkFeaturePossibleAsArgs)
      case _                                                => false
