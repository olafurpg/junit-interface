package com.geirsson.junit;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.junit.runner.Computer;
import org.junit.runner.Runner;
import org.junit.runners.model.RunnerBuilder;

public class JUnitComputer extends Computer {
  final Map<Class<?>, Class<?>> suiteRunners;

  public JUnitComputer(ClassLoader testClassLoader, CustomRunners customRunners) {
    suiteRunners = new HashMap<>();
      customRunners.all().forEach((suite, runner) -> {
        try {
          suiteRunners.put(
              testClassLoader.loadClass(suite),
              testClassLoader.loadClass(runner)
          );
        } catch (ClassNotFoundException e) {
          e.printStackTrace();
        }
      });
  }

  public Optional<Class<?>> customRunner(Class<?> clazz) {
    for (Map.Entry<Class<?>, Class<?>> entry : suiteRunners.entrySet()) {
      if (entry.getKey().isAssignableFrom(clazz)) {
        return Optional.of(entry.getValue());
      }
    }
    return Optional.empty();
  }

  @Override
  protected Runner getRunner(RunnerBuilder builder, Class<?> testClass) throws Throwable {
    Optional<Class<?>> runnerClass = customRunner(testClass);
    if (runnerClass.isPresent()) {
      Runner runner = (Runner) runnerClass.get().getConstructor(Class.class).newInstance(testClass);
      return new JUnitRunnerWrapper(runner);
    } else {
      return super.getRunner(builder, testClass);
    }
  }
}
