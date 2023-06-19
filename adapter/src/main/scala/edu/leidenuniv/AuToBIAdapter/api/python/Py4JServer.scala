package edu.leidenuniv.AuToBIAdapter.api.python

import edu.leidenuniv.AuToBIAdapter.utils.*
import java.net.InetAddress
import java.util.Locale

private[AuToBIAdapter] class NoPy4JServerError(s: String = "No server found")
    extends Exception(s)

// A wrapper for both GatewayServer, and ClientServer to pin Python thread to JVM thread.
private[AuToBIAdapter] class Py4JServer:
  private[AuToBIAdapter] val secret: String = Secrets.createSecret()

  // Launch a Py4J gateway or client server for the process to connect to; this will let it see our
  // Java system properties and such
  private val localhost = InetAddress.getLoopbackAddress
  private val server    =
    if sys.env.getOrElse("AUTOBI_PIN_THREAD", "true").toLowerCase(Locale.ROOT) == "true"
    then
      new py4j.ClientServer.ClientServerBuilder()
        .authToken(secret)
        .javaPort(0)
        .javaAddress(localhost)
        .build()
    else
      new py4j.GatewayServer.GatewayServerBuilder()
        .authToken(secret)
        .javaPort(0)
        .javaAddress(localhost)
        .callbackClient(py4j.GatewayServer.DEFAULT_PYTHON_PORT, localhost, secret)
        .build()

  def start(): Unit = server match
    case clientServer: py4j.ClientServer   => clientServer.startServer()
    case gatewayServer: py4j.GatewayServer => gatewayServer.start()
    case null                              => throw new NoPy4JServerError()

  def getListeningPort: Int = server match
    case clientServer: py4j.ClientServer   => clientServer.getJavaServer.getListeningPort
    case gatewayServer: py4j.GatewayServer => gatewayServer.getListeningPort
    case null                              => throw new NoPy4JServerError()

  def shutdown(): Unit = server match
    case clientServer: py4j.ClientServer   => clientServer.shutdown()
    case gatewayServer: py4j.GatewayServer => gatewayServer.shutdown()
    case null                              => throw new NoPy4JServerError()
