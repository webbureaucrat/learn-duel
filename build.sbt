name := "LearnDuel"
organization := "de.htwg.se"
version := "0.0.1"
scalaVersion := "2.12.4"

libraryDependencies += "org.scalactic" %% "scalactic" % "3.0.4"
libraryDependencies += "org.scalatest" %% "scalatest" % "3.0.4" % "test"
libraryDependencies += "junit" % "junit" % "4.12" % "test"

libraryDependencies += "org.scalafx" %% "scalafx" % "8.0.144-R12"
libraryDependencies += "com.google.inject" % "guice" % "4.1.0"
libraryDependencies += "net.codingwell" %% "scala-guice" % "4.1.0"
libraryDependencies += "com.typesafe.play" %% "play-json" % "2.6.6"
libraryDependencies += "ch.qos.logback" % "logback-classic" % "1.2.3"
libraryDependencies += "com.typesafe.scala-logging" %% "scala-logging" % "3.7.2"
libraryDependencies += "com.typesafe.akka" %% "akka-http" % "10.0.0"


enablePlugins(GatlingPlugin)
libraryDependencies += "io.gatling.highcharts" % "gatling-charts-highcharts" % "2.3.0" % "test"

libraryDependencies += "io.gatling"            % "gatling-test-framework"    % "2.3.0" % "test"


unmanagedSourceDirectories in Compile := (unmanagedSourceDirectories in Compile).value
  .filter {
    _.exists
  }
unmanagedSourceDirectories in Test := (unmanagedSourceDirectories in Test).value
  .filter {
    _.exists
  }
coverageExcludedPackages := ".*view.*;.*GuiceModule.*;.*LearnDuel.*"
coverageEnabled := true
coverageHighlighting := true

