package com.geirsson.junit;

import java.util.Arrays;
import java.util.stream.Stream;

import sbt.testing.EventHandler;
import sbt.testing.Fingerprint;
import sbt.testing.Framework;
import sbt.testing.Logger;
import sbt.testing.Runner;
import sbt.testing.SubclassFingerprint;
import sbt.testing.Task;
import sbt.testing.TaskDef;


public final class JUnitFramework implements Framework
{
  private static final Fingerprint[] FINGERPRINTS = new Fingerprint[] {
      new RunWithFingerprint(),
      new JUnitFingerprint(),
      new JUnit3Fingerprint(),
      new ScalatestFingerprint()
  };

  @Override
  public String name() { return "JUnit"; }

  @Override
  public sbt.testing.Fingerprint[] fingerprints() {
    return FINGERPRINTS;
  }

  @Override
  public sbt.testing.Runner runner(String[] args, String[] remoteArgs, ClassLoader testClassLoader) {
    return new JUnitRunner(args, remoteArgs, testClassLoader);
  }
}
