package edu.leidenuniv.AuToBIAdapter

import edu.cuny.qc.speech.AuToBI.AuToBI
import edu.leidenuniv.AuToBIAdapter.utils.*

class RunDefault:
  def run(args: Array[String]): Unit =
    args match
      case Array("-adapter_test") => println("TEST SUCCESS: The adapter is running")
      case a                      =>
        val autobi = new AuToBI()
        autobi.init(a)
        autobi.run()

  def run(args_str: String): Unit =
    run(Parsing.splitStringToArgArray(args_str))
