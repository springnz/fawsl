name := "fawsl"

version := "1.0"

scalaVersion := "2.12.1"

val catsVersion = "0.9.0"
val fs2Version = "0.9.6"

libraryDependencies += "org.typelevel" %% "cats" % catsVersion
libraryDependencies += "org.typelevel" %% "cats-free" % catsVersion
libraryDependencies += "co.fs2" %% "fs2-core" % fs2Version

// TODO: Separate projects so we don't have to pull in every AWS client
val awsVersion = "1.11.147"
libraryDependencies += "com.amazonaws" % "aws-java-sdk-s3" % awsVersion
