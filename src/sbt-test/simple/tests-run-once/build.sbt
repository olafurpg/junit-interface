name := """tests-run-once"""

scalaVersion := "2.10.7"

libraryDependencies += "com.geirsson" % "junit-interface" % sys.props("plugin.version") % "test"

fork in Test := true

testOptions += Tests.Argument(TestFrameworks.JUnit, "--verbosity=1")
