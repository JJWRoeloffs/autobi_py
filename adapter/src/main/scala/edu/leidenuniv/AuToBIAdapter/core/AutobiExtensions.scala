package edu.leidenuniv.AuToBIAdapter.core

import edu.cuny.qc.speech.AuToBI.core.AuToBIParameters

extension (params: AuToBIParameters)
  def getParameterOption(param: String): Option[String] =
    Option(params.getOptionalParameter(param))

  def fromArgsStrings(paramStringArray: Array[String]): AuToBIParameters =
    params.readParameters(paramStringArray)
    params

  def fromArgsString(paramString: String): AuToBIParameters =
    params.fromArgsStrings(paramString.split("\\s+"))
