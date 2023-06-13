package edu.leidenuniv.AuToBIAdapter.api.python

import scala.util.control.ControlThrowable

object PythonRunner:
  @main def main(): Unit =
    val gatewayServer = new Py4JServer()
    val thread = new Thread(() => printUncaughtExceptions {gatewayServer.start()})
    thread.setName("py4j-gateway-init")
    thread.setDaemon(true)
    thread.start()
    thread.join()

  // We should probably use logging instead.
  // However, I don't have the time for that.
  def printUncaughtExceptions[T](f: => T): T =
      try
        f
      catch
        case ct: ControlThrowable =>
          throw ct
        case t: Throwable =>
          println(s"Uncaught exception in thread ${Thread.currentThread().getName}: {t}")
          throw t
