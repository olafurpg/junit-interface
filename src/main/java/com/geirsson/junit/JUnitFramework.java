package com.geirsson.junit;

import java.util.Arrays;
import java.util.stream.Stream;

import com.novocode.junit.JUnit3Fingerprint;
import com.novocode.junit.JUnitFingerprint;
import com.novocode.junit.RunWithFingerprint;

import org.pantsbuild.tools.junit.impl.ConsoleRunnerImpl;

import sbt.testing.EventHandler;
import sbt.testing.Fingerprint;
import sbt.testing.Framework;
import sbt.testing.Logger;
import sbt.testing.Runner;
import sbt.testing.Task;
import sbt.testing.TaskDef;


public final class JUnitFramework implements Framework
{
  private static final Fingerprint[] FINGERPRINTS = new Fingerprint[] {
    new RunWithFingerprint(),
    new JUnitFingerprint(),
    new JUnit3Fingerprint()
  };

  @Override
  public String name() { return "JUnit"; }

  @Override
  public sbt.testing.Fingerprint[] fingerprints() {
    return FINGERPRINTS;
  }

  @Override
  public sbt.testing.Runner runner(String[] args, String[] remoteArgs, ClassLoader testClassLoader) {

    return new Runner() {
      @Override
      public Task[] tasks(TaskDef[] taskDefs) {

        return new Task[] {
          new Task() {
            @Override
            public String[] tags() {
              return new String[0];
            }

            @Override
            public Task[] execute(EventHandler eventHandler, Logger[] loggers) {
              ClassLoader old = Thread.currentThread().getContextClassLoader();
              try {
                Thread.currentThread().setContextClassLoader(testClassLoader);
                String[] newArgs = Stream.concat(
                    Arrays.stream(args),
                    Arrays.stream(taskDefs).map(def -> def.fullyQualifiedName())
                ).toArray(String[]::new);
                ConsoleRunnerImpl runner = ConsoleRunnerImpl.mainImpl(newArgs);
                runner.run();
              } finally {
                Thread.currentThread().setContextClassLoader(old);
              }
              return new Task[0];
            }

            @Override
            public TaskDef taskDef() {
              return null;
            }
          }
        };
      }

      @Override
      public String done() {
        return "";
      }

      @Override
      public String[] remoteArgs() {
        return new String[0];
      }

      @Override
      public String[] args() {
        return new String[0];
      }
    };
  }
}
