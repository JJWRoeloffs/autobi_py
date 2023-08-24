package edu.leidenuniv.AuToBIAdapter.utils

import java.nio.ByteBuffer

// Taken from Tupol's Scala-utils, reformatted to scala3
// https://mvnrepository.com/artifact/org.tupol/scala-utils
// This is used to get a cleaner secret management interface
object ByteableOps:
  trait Byteable[A]:
    extension (a: A) def toByteArray: Array[Byte]

  given Byteable[Short] with
    extension (x: Short)
      def toByteArray: Array[Byte] =
        val buf = ByteBuffer.allocate(2)
        buf.putShort(x)
        buf.array

  given Byteable[Int] with
    extension (x: Int)
      def toByteArray: Array[Byte] =
        val buf = ByteBuffer.allocate(4)
        buf.putInt(x)
        buf.array

  given Byteable[Long] with
    extension (x: Long)
      def toByteArray: Array[Byte] =
        val buf = ByteBuffer.allocate(8)
        buf.putLong(x)
        buf.array

  given Byteable[Float] with
    extension (x: Float)
      def toByteArray: Array[Byte] =
        val buf = ByteBuffer.allocate(4)
        buf.putFloat(x)
        buf.array

  given Byteable[Double] with
    extension (x: Double)
      def toByteArray: Array[Byte] =
        val buf = ByteBuffer.allocate(8)
        buf.putDouble(x)
        buf.array
