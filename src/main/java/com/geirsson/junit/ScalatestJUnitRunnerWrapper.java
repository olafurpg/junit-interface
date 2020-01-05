package com.geirsson.junit;

import org.junit.runner.Description;
import org.junit.runner.Runner;
import org.junit.runner.manipulation.Filter;
import org.junit.runner.manipulation.Filterable;
import org.junit.runner.manipulation.NoTestsRemainException;
import org.junit.runner.notification.RunNotifier;

class ScalatestJUnitRunnerWrapper extends Runner implements Filterable {
    private final Runner delegate;

    ScalatestJUnitRunnerWrapper(Runner delegate) {
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
