package edu.leidenuniv.AuToBIAdapter.utils

import org.scalatest.funsuite.AnyFunSuite
import scala.math.BigInt

class HexTest extends AnyFunSuite:
  test("short round-trip"):
      val value = 12.toShort
      assert(Integer.parseInt(Hex.valueOf(value), 16) == value)

  test("int round-trip"):
      val value = 4700808
      assert(Integer.parseInt(Hex.valueOf(value), 16) == value)

  test("long round-trip"):
      val value = 4700808.toLong
      assert(Integer.parseInt(Hex.valueOf(value), 16) == value)

class SecretsTest extends AnyFunSuite:
  test("Returns hex"):
      // We can use BigInt because we hard-coded the size to 256
      val secret = BigInt(Secrets.createSecret(), 16)
      assert(secret.isInstanceOf[BigInt])

  test("always returns something different"):
      val secret1 = Secrets.createSecret()
      val secret2 = Secrets.createSecret()
      assert(secret1 != secret2)

  test("always returns something different - Parsed as BigInt"):
      val secret1 = BigInt(Secrets.createSecret(), 16)
      val secret2 = BigInt(Secrets.createSecret(), 16)
      assert(secret1 != secret2)
