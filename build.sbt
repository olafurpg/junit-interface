def scala212 = "2.12.10"
inThisBuild(
  List(
    organization := "com.geirsson",
    homepage := Some(url("https://github.com/olafurpg/junit-interface")),
    licenses := List(
      "Two-clause BSD-style license" ->
      url("http://github.com/sbt/junit-interface/blob/master/LICENSE.txt")
    ),
    developers := List(
      Developer(
        "olafurpg",
        "Ólafur Páll Geirsson",
        "olafurpg@gmail.com",
        url("https://geirsson.com")
      )
    ),
    scalaVersion := scala212,
    crossScalaVersions := List(scala212)
  )
)

lazy val interface = project
  .in(file("."))
  .settings(
    name := "junit-interface",
    autoScalaLibrary := false,
    crossPaths := false,
    sbtPlugin := false,
    libraryDependencies ++= Seq(
      "junit" % "junit" % "4.12",
      "org.scala-sbt" % "test-interface" % "1.0"
    ),
    javacOptions in Compile ++= List("-target", "1.8", "-source", "1.8"),
    javacOptions in (Compile, doc) --= List("-target", "1.8"),
    description := "An implementation of sbt's test interface for JUnit 4",
    scriptedBufferLog := false,
    scriptedLaunchOpts ++= Seq(
      s"-Dplugin.version=${version.value}",
      "-Xmx256m"
    ),
  )
  .enablePlugins(SbtPlugin)

lazy val tests = project
  .dependsOn(interface)
  .settings(
    skip in publish := true,
    testFrameworks := List(new TestFramework("com.geirsson.junit.PantsFramework")),
    classLoaderLayeringStrategy := ClassLoaderLayeringStrategy.Flat,
    libraryDependencies ++= List(
      "org.scalatest" %% "scalatest" % "3.0.8",
      "junit" % "junit" % "4.11"
    )
  )

