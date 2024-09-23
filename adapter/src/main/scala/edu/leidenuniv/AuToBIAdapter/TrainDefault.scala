package edu.leidenuniv.AuToBIAdapter

import edu.cuny.qc.speech.AuToBI.AuToBITrainer
import edu.leidenuniv.AuToBIAdapter.utils.Parsing

class TrainDefault:
  def run(args: Array[String]): Unit =
    AuToBITrainer.main(args)

  def run(args_str: String): Unit =
    run(Parsing.splitStringToArgArray(args_str))
