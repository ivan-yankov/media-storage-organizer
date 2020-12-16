name := "media-storage-organizer"

version := "2.0"

scalaVersion := "2.12.4"

val scalaFxVersion = "8.0.144-R12"
val slfVersion = "1.7.30"

mainClass in assembly := Some("org.yankov.mso.application.Main")

libraryDependencies ++= Seq(
  "org.scalafx" %% "scalafx" % scalaFxVersion,
  "org.slf4j" % "slf4j-api" % slfVersion,
  "org.slf4j" % "slf4j-simple" % slfVersion
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
  case PathList("META-INF", _ @ _*) => MergeStrategy.discard
  case _ => MergeStrategy.first
}

scalacOptions ++= Seq("-deprecation", "-feature")