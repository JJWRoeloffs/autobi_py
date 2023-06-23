package edu.leidenuniv.AuToBIAdapter.core

import edu.cuny.qc.speech.AuToBI.core.FeatureSet
import edu.leidenuniv.AuToBIAdapter.utils.AutobiReflections
import java.lang.reflect.Constructor
import scala.jdk.CollectionConverters.*

object FeatureSets:
  lazy val featureSets: Map[String, Class[? <: FeatureSet]] =
    AutobiReflections.ref
      .getSubTypesOf(classOf[FeatureSet])
      .asScala
      .collect {
        case subtype if AutobiReflections.isInstantiable(subtype) =>
          subtype.getSimpleName -> subtype
      }
      .toMap

  lazy val possibleSets: Set[String] = featureSets.keySet

  private def getFeatureSetConstructor(
    featureSet: Class[? <: FeatureSet],
  ): Option[Constructor[FeatureSet]] =
    featureSet.getConstructors.collectFirst {
      case c: Constructor[FeatureSet] if c.getParameterCount == 0 => c
    }

  def getFeatureSet(name: String): Option[FeatureSet] =
    for
      extractor   <- featureSets.get(name)
      constructor <- getFeatureSetConstructor(extractor)
    yield constructor.newInstance()
