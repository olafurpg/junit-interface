package com.geirsson.junit;

import sbt.testing.Fingerprint;

public class PantsFramework extends JUnitFramework {

  private static final CustomFingerprint scalatestFingerprint = CustomFingerprint.of(
      "org.scalatest.Suite",
      "org.scalatest.junit.JUnitRunner"
  );

  private static final Fingerprint[] FINGERPRINTS = new Fingerprint[] {
      new RunWithFingerprint(),
      new JUnitFingerprint(),
      new JUnit3Fingerprint(),
      scalatestFingerprint
  };

  @Override
  public Fingerprint[] fingerprints() {
    return FINGERPRINTS;
  }

  @Override
  public CustomRunners customRunners() {
    return CustomRunners.of(scalatestFingerprint);
  }
}
