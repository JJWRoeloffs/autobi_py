package edu.leidenuniv.AuToBIAdapter.utils

import scala.util.matching.Regex

private[AuToBIAdapter] object Parsing:
  private lazy val splitOnWhitespaceRegex = new Regex("\"(.*?)\"|([^\\s]+)")

  // Simulate java args parsing to String[].
  // This way, we do not need to pass a JavaArray from python, and can just pass a str
  def splitStringToArgArray(str: String): Array[String] =
    splitOnWhitespaceRegex
      .findAllMatchIn(str)
      .map(_.subgroups.flatMap(Option(_)).fold("")(_ ++ _))
      .toArray
