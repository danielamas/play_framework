name := """produtos-api"""

version := "1.0"

lazy val root = (project in file(".")).enablePlugins(PlayJava, PlayEbean)

scalaVersion := "2.11.7"

libraryDependencies ++= Seq(
  javaJdbc,
  cache,
  javaWs,
  "mysql" % "mysql-connector-java" % "5.1.36",
  "com.adrianhurt"% "play-bootstrap_2.11" % "1.0-P25-B3",
  "org.apache.cassandra" % "cassandra-all" % "0.7.3",
  "com.typesafe.play" % "play-mailer_2.11" % "5.0.0-M1"
)

//This will produce one jar as an output artifact, and allow you to execute it directly using the java command.
import AssemblyKeys._
assemblySettings
mainClass in assembly := Some("play.core.server.NettyServer")
fullClasspath in assembly += Attributed.blank(PlayKeys.playPackageAssets.value)

EclipseKeys.preTasks := Seq(compile in Compile)
EclipseKeys.projectFlavor := EclipseProjectFlavor.Java
EclipseKeys.createSrc := EclipseCreateSrc.ValueSet(EclipseCreateSrc.ManagedClasses, EclipseCreateSrc.ManagedResources)
