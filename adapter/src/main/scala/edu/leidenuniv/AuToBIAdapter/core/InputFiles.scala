package edu.leidenuniv.AuToBIAdapter.core

import edu.cuny.qc.speech.AuToBI.core.{AuToBIParameters, WavData, Word}
import edu.cuny.qc.speech.AuToBI.io.{
  AuToBIWordReader,
  FormattedFile,
  PseudosyllableWordReader,
  WavReader,
}
import edu.cuny.qc.speech.AuToBI.util.WordReaderUtils
import edu.leidenuniv.AuToBIAdapter.core.*
import java.util.List as JavaList

object InputFiles:
  private val inputFileTypes: Map[String, FormattedFile.Format] = Map(
    "input_file"     -> FormattedFile.Format.TEXTGRID,
    "cprom_file"     -> FormattedFile.Format.CPROM,
    "rhapsodie_file" -> FormattedFile.Format.RHAPSODIE,
  )

  def getWordList(params: AuToBIParameters): Option[JavaList[Word]] =
    (for
      (name, kind) <- inputFileTypes.iterator
      file_name    <- params.getParameterOption(name)
      file          = FormattedFile(file_name, kind)
      reader        = WordReaderUtils.getAppropriateReader(file, params)
      _             = params.getParameterOption("silence_regex").foreach(reader.setSilenceRegex)
    yield reader).collectFirst { case reader: AuToBIWordReader =>
      reader.readWords
    }

  def generateWordList(params: AuToBIParameters, wavData: WavData): JavaList[Word] =
    val reader = params.getParameterOption("silence_threshold") match
      case Some(threshold) => PseudosyllableWordReader(wavData, threshold.toDouble)
      case None            => PseudosyllableWordReader(wavData)
    params.getParameterOption("silence_regex").foreach(reader.setSilenceRegex)
    reader.readWords

  def getWavData(params: AuToBIParameters): Option[WavData] =
    params.getParameterOption("wav_file").map(x => WavReader().read(x))

  def linkWordsToWavData(words: JavaList[Word], wav: WavData): Unit =
    words.forEach(_.setAttribute("wav", wav))

  def getData(params: AuToBIParameters): Option[JavaList[Word]] =
    for
      wav  <- getWavData(params)
      words = getWordList(params).getOrElse(generateWordList(params, wav))
      _     = linkWordsToWavData(words, wav)
    yield words
