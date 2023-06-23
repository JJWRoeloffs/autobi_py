package edu.leidenuniv.AuToBIAdapter.utils

import edu.cuny.qc.speech.AuToBI.AuToBI
import java.lang.reflect.Modifier
import org.reflections.Reflections

private[AuToBIAdapter] object AutobiReflections:
  def isInstantiable(c: Class[?]): Boolean =
    !(c.isPrimitive
      | Modifier.isAbstract(c.getModifiers)
      | c.isInterface
      | c.isArray
      | classOf[String].getName.equals(c.getName)
      | classOf[Integer].getName.equals(c.getName))

  lazy val ref = new Reflections(classOf[AuToBI].getPackageName)
