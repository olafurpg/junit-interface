package com.geirsson.junit;

import java.io.OutputStream;
import java.io.PrintStream;

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

  @Override
  public sbt.testing.Runner runner(String[] args, String[] remoteArgs, ClassLoader testClassLoader) {
    boolean isVerbose = false;
    for (String arg : args) {
      if (arg.equals("-v") || arg.equals("--verbose")) {
        isVerbose = true;
      }
    }
    if (!isVerbose) {
      OutputStream out = new OutputStream() {
        @Override public void write(int x) {}
      };
      System.setErr(new PrintStream(out));
    }
    return super.runner(args, remoteArgs, testClassLoader);
  }
}
