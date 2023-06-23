package edu.leidenuniv.AuToBIAdapter

import edu.cuny.qc.speech.AuToBI.core.AuToBIParameters
import edu.leidenuniv.AuToBIAdapter.core.fromArgsStrings
import java.io.File

// This could have been a case class.
// Using `given Conversion[String, File] = (string: String) => File(string)` for the functions.
// Using reflections to not have to copy-paste so many functions
// However, the current ugly way leaves us with cleaner bytecode,
// which is what we are interfacing with in the first place.
class AuToBIArgumentsBuilder:
  private var inputTextGrid: Option[File]                      = None
  private var inputWav: Option[File]                           = None
  private var outputFile: Option[File]                         = None
  private var arffFile: Option[File]                           = None
  private var pitchAccentDetector: Option[File]                = None
  private var pitchAccentClassifier: Option[File]              = None
  private var intonalPhraseBoundaryDetector: Option[File]      = None
  private var intermediatePhraseBoundaryDetector: Option[File] = None
  private var boundaryToneClassifier: Option[File]             = None
  private var phraseAccentClassifier: Option[File]             = None
  private var log4jConfigFile: Option[File]                    = None
  private var cpromFile: Option[File]                          = None
  private var rhapsodieFile: Option[File]                      = None
  private var silenceRegex: Option[String]                     = None
  private var wordsTierName: Option[String]                    = None
  private var breaksTierName: Option[String]                   = None
  private var charset: Option[String]                          = None
  private var endIDX: Option[String]                           = None
  private var orthoIDX: Option[String]                         = None
  private var startIDX: Option[String]                         = None
  private var silenceTreshhold: Option[Float]                  = None
  private var distributions: Option[Boolean]                   = None

  private lazy val argumentNamesMap = Map(
    inputTextGrid                      -> "input_file",
    inputWav                           -> "wav_file",
    outputFile                         -> "out_file",
    arffFile                           -> "arff_file",
    pitchAccentDetector                -> "pitch_accent_detector",
    pitchAccentClassifier              -> "pitch_accent_classifier",
    intonalPhraseBoundaryDetector      -> "intonal_phrase_boundary_detector",
    intermediatePhraseBoundaryDetector -> "intermediate_phrase_boundary_detector",
    boundaryToneClassifier             -> "boundary_tone_classifier",
    phraseAccentClassifier             -> "phrase_accent_classifier",
    log4jConfigFile                    -> "log4j_config_file",
    cpromFile                          -> "cprom_file",
    rhapsodieFile                      -> "rhapsodie_file",
    silenceRegex                       -> "silence_regex",
    wordsTierName                      -> "words_tier_name",
    breaksTierName                     -> "breaks_tier_name",
    charset                            -> "charset",
    endIDX                             -> "end_idx",
    orthoIDX                           -> "ortho_idx",
    startIDX                           -> "start_idx",
    silenceTreshhold                   -> "silence_threshold",
    distributions                      -> "distributions",
  )

  def withInputTextGrid(textGrid: File): this.type =
    inputTextGrid = Some(textGrid)
    this

  def withInputTextGrid(textGrid: String): this.type =
    withInputTextGrid(File(textGrid))

  def withInputWav(wav: File): this.type =
    inputWav = Some(wav)
    this

  def withInputWav(wav: String): this.type =
    withInputWav(File(wav))

  def withOutputFile(outFile: File): this.type =
    outputFile = Some(outFile)
    this

  def withOutputFile(outFile: String): this.type =
    withOutputFile(File(outFile))

  def withOutputArffFile(outputArffFile: File): this.type =
    arffFile = Some(outputArffFile)
    this

  def withOutputArffFile(outputArffFile: String): this.type =
    withOutputArffFile(File(outputArffFile))

  def withPitchAccentDetector(detector: File): this.type =
    pitchAccentDetector = Some(detector)
    this

  def withPitchAccentDetector(detector: String): this.type =
    withPitchAccentDetector(File(detector))

  def withPitchAccentClassifier(classifier: File): this.type =
    pitchAccentClassifier = Some(classifier)
    this

  def withPitchAccentClassifier(classifier: String): this.type =
    withPitchAccentDetector(File(classifier))

  def withIntonalPhraseBoundaryDetector(detector: File): this.type =
    intonalPhraseBoundaryDetector = Some(detector)
    this

  def withIntonalPhraseBoundaryDetector(detector: String): this.type =
    withIntonalPhraseBoundaryDetector(File(detector))

  def withIntermediatePhraseBoundaryDetector(detector: File): this.type =
    intermediatePhraseBoundaryDetector = Some(detector)
    this

  def withIntermediatePhraseBoundaryDetector(detector: String): this.type =
    withIntermediatePhraseBoundaryDetector(File(detector))

  def withBoundaryToneClassifier(classifier: File): this.type =
    boundaryToneClassifier = Some(classifier)
    this

  def withBoundaryToneClassifier(classifier: String): this.type =
    withBoundaryToneClassifier(File(classifier))

  def withPhraseAccentClassifier(classifier: File): this.type =
    phraseAccentClassifier = Some(classifier)
    this

  def withPhraseAccentClassifier(classifier: String): this.type =
    withPhraseAccentClassifier(File(classifier))

  def withLog4jConfigFile(configFile: File): this.type =
    log4jConfigFile = Some(configFile)
    this

  def withLog4jConfigFile(configFile: String): this.type =
    withLog4jConfigFile(File(configFile))

  def withCpromFile(configFile: File): this.type =
    cpromFile = Some(configFile)
    this

  def withCpromFile(configFile: String): this.type =
    withCpromFile(File(configFile))

  def withRhapsodieFile(configFile: File): this.type =
    rhapsodieFile = Some(configFile)
    this

  def withRhapsodieFile(configFile: String): this.type =
    withRhapsodieFile(File(configFile))

  def withsilenceRegex(name: String): this.type =
    silenceRegex = Some(name)
    this

  def withWordsTierName(name: String): this.type =
    wordsTierName = Some(name)
    this

  def withBreaksTierName(name: String): this.type =
    breaksTierName = Some(name)
    this

  def withCharset(new_charset: String): this.type =
    charset = Some(new_charset)
    this

  def withEndIDX(idx: String): this.type =
    endIDX = Some(idx)
    this

  def withOrthoIDX(idx: String): this.type =
    orthoIDX = Some(idx)
    this

  def withStartIDX(idx: String): this.type =
    startIDX = Some(idx)
    this

  def withSilenceTreshold(treshhold: Float): this.type =
    silenceTreshhold = Some(treshhold)
    this

  def withSilenceTreshold(treshhold: String): this.type =
    withSilenceTreshold(treshhold.toFloat)

  def withDistributions(distr: Boolean): this.type =
    distributions = Some(distr)
    this

  def withDistributions(distr: String): this.type =
    withDistributions(distr.toLowerCase.toBoolean)

  def toArgsStringArray(): Array[String] =
    argumentNamesMap.collect { case (Some(argument), argumentSpecifier) =>
      s"-$argumentSpecifier=$argument"
    }.toArray

  def toAuToBIParameters(): AuToBIParameters =
    AuToBIParameters().fromArgsStrings(toArgsStringArray())

  def toArgsString(): String =
    toArgsStringArray().mkString(" ")
