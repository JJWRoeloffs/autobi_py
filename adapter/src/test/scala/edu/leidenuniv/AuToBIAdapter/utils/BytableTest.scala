package edu.leidenuniv.AuToBIAdapter.utils

import edu.leidenuniv.AuToBIAdapter.utils.ByteableOps.given
import java.nio.ByteBuffer
import org.scalatest.funsuite.AnyFunSuite

class ByteableTest extends AnyFunSuite:
  test("short round-trip"):
      val value = 12.toShort
      assert(ByteBuffer.wrap(value.toByteArray).asShortBuffer.get == value)

  test("int round-trip"):
      val value = 12
      assert(ByteBuffer.wrap(value.toByteArray).asIntBuffer.get == value)

  test("long round-trip"):
      val value = 12.toLong
      assert(ByteBuffer.wrap(value.toByteArray).asLongBuffer.get == value)

  test("float round-trip"):
      val value = 12.12.toFloat
      assert(ByteBuffer.wrap(value.toByteArray).asFloatBuffer.get == value)

  test("double round-trip"):
      val value = 12.12
      assert(ByteBuffer.wrap(value.toByteArray).asDoubleBuffer.get == value)
