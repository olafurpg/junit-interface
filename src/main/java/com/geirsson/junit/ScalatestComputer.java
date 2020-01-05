package com.geirsson.junit;

import org.junit.runner.Computer;
import org.junit.runner.Runner;
import org.junit.runners.model.RunnerBuilder;

public class ScalatestComputer extends Computer {
  Class<?> scalatestSuiteClass;
  Class<?> scalatestRunnerClass;

  public ScalatestComputer(ClassLoader testClassLoader) {
    try {
      scalatestSuiteClass = testClassLoader.loadClass("org.scalatest.Suite");
      scalatestRunnerClass = testClassLoader.loadClass("org.scalatest.junit.JUnitRunner");
    } catch (ClassNotFoundException | NoClassDefFoundError ex) {
    }
  }
  public boolean isScalatestSuite(Class<?> clazz) {
    return scalatestSuiteClass != null && scalatestSuiteClass.isAssignableFrom(clazz);
  }


  @Override
  protected Runner getRunner(RunnerBuilder builder, Class<?> testClass) throws Throwable {
    if (scalatestRunnerClass != null && isScalatestSuite(testClass)) {
      Runner runner = (Runner) scalatestRunnerClass.getConstructor(Class.class).newInstance(testClass);
      return new ScalatestJUnitRunnerWrapper(runner);
    } else {
      return super.getRunner(builder, testClass);
    }
  }
}
