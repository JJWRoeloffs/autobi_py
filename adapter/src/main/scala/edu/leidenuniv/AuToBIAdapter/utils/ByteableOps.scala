package edu.leidenuniv.AuToBIAdapter.utils

import java.nio.ByteBuffer

// Taken from Tupol's Scala-utils, reformatted to scala3
// https://mvnrepository.com/artifact/org.tupol/scala-utils
// This is used to get a cleaner secret management interface

private[AuToBIAdapter] object ByteableOps:
  trait Byteable extends Any:
    def toByteArray: Array[Byte]

  implicit class XShort(val x: Short) extends AnyVal with Byteable:
    def toByteArray: Array[Byte] =
      val buf = ByteBuffer.allocate(2)
      buf.putShort(x)
      buf.array

  implicit class XInt(val x: Int) extends AnyVal with Byteable:
    def toByteArray: Array[Byte] =
      val buf = ByteBuffer.allocate(4)
      buf.putInt(x)
      buf.array

  implicit class XLong(val x: Long) extends AnyVal with Byteable:
    def toByteArray: Array[Byte] =
      val buf = ByteBuffer.allocate(8)
      buf.putLong(x)
      buf.array

  implicit class XFloat(val x: Float) extends AnyVal with Byteable:
    def toByteArray: Array[Byte] =
      val buf = ByteBuffer.allocate(4)
      buf.putFloat(x)
      buf.array

  implicit class XDouble(val x: Double) extends AnyVal with Byteable:
    def toByteArray: Array[Byte] =
      val buf = ByteBuffer.allocate(8)
      buf.putDouble(x)
      buf.array
