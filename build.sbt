import sbt.Keys._

val VERSION = "2.0.0-SNAPSHOT"
val module = "kryo"

enablePlugins(ScalaUnidocPlugin, JavaUnidocPlugin)

organization := "kr.bydelta"
sonatypeProfileName := "kr.bydelta"

name := s"koalaNLP-$module"

version := VERSION

scalaVersion := "2.12.3"

fork in Test := true
testForkedParallel in Test := true
concurrentRestrictions in Global := Seq(Tags.limit(Tags.Test, 1))

resolvers ++= Seq("releases").map(Resolver.sonatypeRepo)
resolvers += Resolver.typesafeRepo("releases")
resolvers += Resolver.mavenLocal

scalacOptions ++= Seq("-deprecation", "-unchecked", "-feature")

scalacOptions in Test ++= Seq("-Yrangepos")

crossScalaVersions := Seq("2.11.11", "2.12.3")

publishArtifact in Test := false

coverageExcludedPackages := ".*\\.helper\\..*"

test in assembly := {}

libraryDependencies ++= Seq(
  "org.specs2" %% "specs2-core" % "3.9.5" % "test",
  "kr.bydelta" %% "koalanlp-core" % "2.0.0",
  "com.twitter" %% "chill" % "0.9.2"
)

apiURL := Some(url(s"https://koalanlp.github.io/wrapper-$module/api/scala/"))

homepage := Some(url("http://koalanlp.github.io/KoalaNLP-core"))

publishTo := version { v: String ⇒
  val nexus = "https://oss.sonatype.org/"
  if (v.trim.endsWith("SNAPSHOT"))
    Some("snapshots" at nexus + "content/repositories/snapshots")
  else
    Some("releases" at nexus + "service/local/staging/deploy/maven2")
}.value

licenses := Seq("MIT" -> url("https://tldrlegal.com/license/mit-license"))

publishMavenStyle := true

publishArtifact in Test := false

pomIncludeRepository := { _ ⇒ false }

pomExtra :=
  <scm>
    <url>git@github.com:koalanlp/util-kryo.git</url>
    <connection>scm:git:git@github.com:koalanlp/util-kryo.git</connection>
  </scm>
    <developers>
      <developer>
        <id>nearbydelta</id>
        <name>Bugeun Kim</name>
        <url>http://bydelta.kr</url>
      </developer>
    </developers>
