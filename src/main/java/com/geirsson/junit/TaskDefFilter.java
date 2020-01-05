package com.geirsson.junit;

import sbt.testing.TaskDef;

public class TaskDefFilter {

  public TaskDefFilter(ClassLoader testClassLoader) {
  }

  public boolean shouldRun(TaskDef taskDef) {
    return true;
  }
}
