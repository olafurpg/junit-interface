package com.geirsson.junit;

import static com.geirsson.junit.Ansi.ENAME1;
import static com.geirsson.junit.Ansi.ENAME2;
import static com.geirsson.junit.Ansi.ENAME3;
import static com.geirsson.junit.Ansi.ERRMSG;
import static com.geirsson.junit.Ansi.FAILURE1;
import static com.geirsson.junit.Ansi.FAILURE2;
import static com.geirsson.junit.Ansi.NNAME1;
import static com.geirsson.junit.Ansi.NNAME2;
import static com.geirsson.junit.Ansi.NNAME3;
import static com.geirsson.junit.Ansi.SKIPPED;
import static com.geirsson.junit.Ansi.SUCCESS1;
import static com.geirsson.junit.Ansi.SUCCESS2;
import static com.geirsson.junit.Ansi.c;

import java.lang.reflect.Method;
import java.util.*;

import org.junit.runner.Description;

import sbt.testing.Status;

class RunSettings {
  private static final Object NULL = new Object();

  final boolean color;
  final boolean quiet;
  final boolean logAssert;
  final boolean logExceptionClass;
  final Set<String> includeTags, excludeTags;
  final boolean useSbtLoggers;
  final boolean verbose;
  final boolean suppressSystemError;
  final Summary summary;
  final ArrayList<String> globPatterns;
  final Set<String> includeCategories, excludeCategories;
  final String testFilter;

  private final boolean decodeScalaNames;
  private final HashMap<String, String> sysprops;
  private final HashSet<String> ignoreRunners = new HashSet<String>();

  RunSettings(boolean color, boolean decodeScalaNames, boolean quiet,
              boolean verbose, boolean useSbtLoggers, Summary summary, boolean logAssert, String ignoreRunners,
              boolean logExceptionClass,
              boolean suppressSystemError, HashMap<String, String> sysprops,
              ArrayList<String> globPatterns,
              Set<String> includeCategories, Set<String> excludeCategories,
              Set<String> includeTags, Set<String> excludeTags,
              String testFilter) {
    this.color = color;
    this.decodeScalaNames = decodeScalaNames;
    this.quiet = quiet;
    this.verbose = verbose;
    this.summary = summary;
    this.logAssert = logAssert;
    this.logExceptionClass = logExceptionClass;
    this.suppressSystemError = suppressSystemError;
    this.includeTags = includeTags;
    this.excludeTags = excludeTags;
    for(String s : ignoreRunners.split(","))
      this.ignoreRunners.add(s.trim());
    this.sysprops = sysprops;
    this.globPatterns = globPatterns;
    this.includeCategories = includeCategories;
    this.excludeCategories = excludeCategories;
    this.testFilter = testFilter;
    this.useSbtLoggers = useSbtLoggers;
  }

  String decodeName(String name) {
    return decodeScalaNames ? decodeScalaName(name) : name;
  }

  private static String decodeScalaName(String name) {
    try {
      Class<?> cl = Class.forName("scala.reflect.NameTransformer");
      Method m = cl.getMethod("decode", String.class);
      String decoded = (String)m.invoke(null, name);
      return decoded == null ? name : decoded;
    } catch(Throwable t) {
      //System.err.println("Error decoding Scala name:");
      //t.printStackTrace(System.err);
      return name;
    }
  }

  String buildInfoName(Description desc) {
    return buildColoredName(desc, NNAME1, NNAME2, NNAME3);
  }

  String buildInfoName(Description desc, Status status) {
    switch (status) {
      case Success:
        return buildSuccessName(desc);
      case Ignored:
      case Skipped:
        return buildSkippedName(desc);
      default:
        return buildInfoName(desc);
    }
  }

  String buildErrorName(Description desc) {
    return buildColoredName(desc, ENAME1, ENAME2, ENAME3);
  }

  String buildErrorName(Description desc, Status status) {
    switch (status) {
      case Failure:
        return buildColoredName(desc, FAILURE1, FAILURE2, FAILURE2);
      case Skipped:
      case Ignored:
        return buildSkippedName(desc);
      default:
        return buildErrorName(desc);
    }
  }

  String buildSuccessName(Description desc) {
    return buildColoredName(desc, SUCCESS1, SUCCESS2, SUCCESS2);
  }

  String buildSkippedName(Description desc) {
    return buildColoredName(desc, SKIPPED, SKIPPED, SKIPPED);
  }

  String buildPlainName(Description desc) {
    return buildColoredName(desc, null, null, null);
  }

  String buildTestResult(Status status) {
    switch (status) {
        case Success:
          return c("+ ", SUCCESS1);
        case Ignored:
          return c("==> i ", SKIPPED);
        case Skipped:
          return c("==> s ", SKIPPED);
        default:
          return c("==> X ", ERRMSG);
      }
  }

  String buildColoredMessage(Throwable t, String c1) {
    if(t == null) return "null";
    if(!logExceptionClass || (!logAssert && (t instanceof AssertionError)))  return t.getMessage();
    StringBuilder b = new StringBuilder();
    b.append(decodeName(t.getClass().getName()));
    b.append(": ").append(t.getMessage());
    return b.toString();
  }

  String buildErrorMessage(Throwable t) {
    return buildColoredMessage(t, ENAME2);
  }

  private String buildColoredName(Description desc, String c1, String c2, String c3) {
    StringBuilder b = new StringBuilder();
    String cn = decodeName(desc.getClassName());
    b.append(c(cn, c1));
    String m = desc.getMethodName();
    if(m != null) {
      b.append('.');
      int mpos1 = m.lastIndexOf('[');
      int mpos2 = m.lastIndexOf(']');
      if(mpos1 == -1 || mpos2 < mpos1) b.append(c(decodeName(m), c2));
      else {
        b.append(c(decodeName(m.substring(0, mpos1)), c2));
        b.append('[');
        b.append(c(m.substring(mpos1+1, mpos2), c3));
        b.append(']');
      }
    }

    return b.toString();
  }

  boolean ignoreRunner(String cln) { return ignoreRunners.contains(cln); }

  Map<String, Object> overrideSystemProperties() {
    HashMap<String, Object> oldprops = new HashMap<String, Object>();
    synchronized(System.getProperties()) {
      for(Map.Entry<String, String> me : sysprops.entrySet()) {
        String old = System.getProperty(me.getKey());
        oldprops.put(me.getKey(), old == null ? NULL : old);
      }
      for(Map.Entry<String, String> me : sysprops.entrySet()) {
        System.setProperty(me.getKey(), me.getValue());
      }
    }
    return oldprops;
  }

  void restoreSystemProperties(Map<String, Object> oldprops) {
    synchronized(System.getProperties()) {
      for(Map.Entry<String, Object> me : oldprops.entrySet()) {
        if(me.getValue() == NULL) {
          System.clearProperty(me.getKey());
        } else {
          System.setProperty(me.getKey(), (String)me.getValue());
        }
      }
    }
  }

  static enum Summary {
    SBT, ONE_LINE, LIST_FAILED
  }
}
