package com.geirsson.junit;

import sbt.testing.Fingerprint;
import sbt.testing.SubclassFingerprint;

public class ScalatestFingerprint implements SubclassFingerprint {
  public static boolean isScalatest(Fingerprint fingerprint) {
    if (fingerprint instanceof SubclassFingerprint) {
      SubclassFingerprint subclassFingerprint = (SubclassFingerprint) fingerprint;
      return subclassFingerprint.superclassName().equals(ScalatestFingerprint.scalatestSuite());
    }
    else {
      return false;
    }
  }
  public static String scalatestSuite() {
      return "org.scalatest.Suite";
  }
  @Override
  public boolean isModule() {
    return false;
  }

  @Override
  public String superclassName() {
    return ScalatestFingerprint.scalatestSuite();
  }

  @Override
  public boolean requireNoArgConstructor() {
    return true;
  }
}
