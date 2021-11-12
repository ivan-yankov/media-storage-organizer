name := "media-storage-organizer"

version := "2.0"

scalaVersion := "2.12.4"

val scalaFxVersion = "8.0.144-R12"
val slfVersion = "1.7.30"
val derbyVersion = "10.14.2.0"
val jaudioTaggerVersion = "2.0.3"
val jFlacVersion = "1.5.2"
val http4sVersion = "0.20.10"
val circeVersion = "0.14.1"

mainClass in assembly := Some("org.yankov.mso.application.Main")

libraryDependencies ++= Seq(
  // ui
  "org.scalafx" %% "scalafx" % scalaFxVersion,

  // logging
  "org.slf4j" % "slf4j-api" % slfVersion,
  "org.slf4j" % "slf4j-simple" % slfVersion,

  // http
  "org.http4s" %% "http4s-dsl" % http4sVersion,
  "org.http4s" %% "http4s-blaze-server" % http4sVersion,
  "org.http4s" %% "http4s-blaze-client" % http4sVersion,

  // database
  "org.apache.derby" % "derby" % derbyVersion,

  "io.circe" %% "circe-generic" % circeVersion,
  "io.circe" %% "circe-parser" % circeVersion,
  "io.circe" %% "circe-literal" % circeVersion,


  //audio
  // https://mvnrepository.com/artifact/org/jaudiotagger
  "org" % "jaudiotagger" % jaudioTaggerVersion,
  // https://mvnrepository.com/artifact/org.jflac/jflac-codec
  "org.jflac" % "jflac-codec" % jFlacVersion,

  // test
  "org.scalatest" %% "scalatest" % "3.0.8" % Test,
  "org.scalamock" %% "scalamock" % "4.4.0" % Test
)

lazy val osName = System.getProperty("os.name") match {
  case n if n.startsWith("Linux") => "linux"
  case n if n.startsWith("Mac") => "mac"
  case n if n.startsWith("Windows") => "win"
  case _ => throw new Exception("Unknown platform.")
}

// Add JavaFX dependencies
lazy val javaFxModules = Seq("base", "controls", "fxml", "graphics", "media", "swing", "web")
libraryDependencies ++= javaFxModules.map(x =>
  "org.openjfx" % s"javafx-$x" % "11" classifier osName
)

assemblyMergeStrategy in assembly := {
  case PathList("META-INF", _@_*) => MergeStrategy.discard
  case _ => MergeStrategy.first
}

scalacOptions ++= Seq("-deprecation", "-feature")
