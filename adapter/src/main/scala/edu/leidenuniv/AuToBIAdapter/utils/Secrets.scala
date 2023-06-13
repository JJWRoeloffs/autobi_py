package edu.leidenuniv.AuToBIAdapter.utils

import edu.leidenuniv.AuToBIAdapter.utils.ByteableOps.*
import java.lang.Byte as JByte
import java.security.SecureRandom

private[AuToBIAdapter] object Hex:
  def valueOf(buf: Array[Byte]): String = buf.map("%02X" format _).mkString
  def valueOf(o: Byteable): String      = valueOf(o.toByteArray)

private[AuToBIAdapter] object Secrets:
  def createSecret(): String =
    // TODO: Make the bit array size a setting somehow
    val bits        = 256
    val rnd         = new SecureRandom()
    val secretBytes = new Array[Byte](bits / JByte.SIZE)
    rnd.nextBytes(secretBytes)
    Hex.valueOf(secretBytes)
