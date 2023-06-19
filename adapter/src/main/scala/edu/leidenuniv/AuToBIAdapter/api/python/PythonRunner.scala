package edu.leidenuniv.AuToBIAdapter.api.python

import scala.util.control.ControlThrowable

object PythonRunner:
  def main(): Unit =
    val gatewayServer = new Py4JServer()
    val thread        = new Thread(() => printUncaughtExceptions(gatewayServer.start()))
    thread.setName("py4j-gateway-init")
    thread.setDaemon(true)
    thread.start()
    thread.join()

  // These cases should be properly logged.
  // However, For lack of time, nothing is done ,and they are simply printed.
  private[AuToBIAdapter] def printUncaughtExceptions[T](f: => T): T =
    try f
    catch
      case ct: ControlThrowable =>
        throw ct
      case t: Throwable         =>
        println(s"Uncaught exception in thread ${Thread.currentThread().getName}: {t}")
        throw t
