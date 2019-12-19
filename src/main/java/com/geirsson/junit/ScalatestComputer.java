package com.geirsson.junit;

import org.junit.runner.Computer;
import org.junit.runner.Description;
import org.junit.runner.Runner;
import org.junit.runner.manipulation.Filter;
import org.junit.runner.manipulation.Filterable;
import org.junit.runner.manipulation.NoTestsRemainException;
import org.junit.runner.notification.RunNotifier;
import org.junit.runners.model.RunnerBuilder;

public class ScalatestComputer extends Computer {
  Class<?> scalatestSuiteClass;
  Class<?> scalatestRunnerClass;

  public ScalatestComputer(ClassLoader testClassLoader) {
    try {
      scalatestSuiteClass = testClassLoader.loadClass("org.scalatest.Suite");
      scalatestRunnerClass = testClassLoader.loadClass("org.scalatest.junit.JUnitRunner");
    } catch (ClassNotFoundException ex) {
    }
  }
  public boolean isScalatestSuite(Class<?> clazz) {
    return scalatestSuiteClass != null && scalatestSuiteClass.isAssignableFrom(clazz);
  }

  private static class ScalatestJunitRunnerWrapper extends Runner implements Filterable {
    private final Runner delegate;

    private ScalatestJunitRunnerWrapper(Runner delegate) {
      this.delegate = delegate;
    }

    @Override
    public Description getDescription() {
      return delegate.getDescription();
    }

    @Override
    public void run(RunNotifier notifier) {
      delegate.run(notifier);
    }

    @Override
    public void filter(Filter filter) throws NoTestsRemainException {
      if (!filter.shouldRun(getDescription())) throw new NoTestsRemainException();
    }
  }

  @Override
  protected Runner getRunner(RunnerBuilder builder, Class<?> testClass) throws Throwable {
    if (scalatestRunnerClass != null && isScalatestSuite(testClass)) {
      Runner runner = (Runner) scalatestRunnerClass.getConstructor(Class.class).newInstance(testClass);
      return new ScalatestJunitRunnerWrapper(runner);
    } else {
      return super.getRunner(builder, testClass);
    }
  }
}
