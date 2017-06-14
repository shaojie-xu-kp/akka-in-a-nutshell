name := """hello-akka"""

version := "1.0"

scalaVersion := "2.11.7"

lazy val akkaVersion = "2.5.2"

libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-actor" % akkaVersion,
  "com.typesafe.akka" %% "akka-testkit" % akkaVersion,
  "org.scalatest" %% "scalatest" % "2.2.4" % "test",
  "junit" % "junit" % "4.12" % "test",
  "com.novocode" % "junit-interface" % "0.11" % "test",
  "com.typesafe.akka" %% "akka-stream-experimental" % "2.0.1"
)

testOptions += Tests.Argument(TestFrameworks.JUnit, "-v")

fork in run := true