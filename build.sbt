val scalaFxVersion = "8.0.144-R12"
val slfVersion = "1.7.30"
val derbyVersion = "10.14.2.0"
val jaudioTaggerVersion = "2.0.3"
val jFlacVersion = "1.5.2"
val http4sVersion = "0.20.10"
val circeVersion = "0.14.1"
val chromaprintVersion = "0.3.1"
val scalaTestVersion = "3.0.8"
val scalaMockVersion = "4.4.0"
val mathVersion = "latest"

lazy val osName = System.getProperty("os.name") match {
  case n if n.startsWith("Linux") => "linux"
  case n if n.startsWith("Mac") => "mac"
  case n if n.startsWith("Windows") => "win"
  case _ => throw new Exception("Unknown platform")
}

lazy val javaFxModules = Seq("base", "controls", "fxml", "graphics", "media", "swing", "web")

lazy val root = (project in file("."))
  .settings(
    organization := "yankov",
    name := "media-storage-organizer",
    scalaVersion := "2.12.15",
    scalacOptions ++= Seq("-deprecation", "-feature"),
    version := readVersion.value(),
    isSnapshot := true,

    resolvers += Resolver.jcenterRepo,

    Test / parallelExecution := false,

    fork := true,
    outputStrategy := Some(StdoutOutput),
    connectInput := true,

    libraryDependencies ++= Seq(
      // math
      "yankov" %% "math" % mathVersion,

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
      "io.circe" %% "circe-generic" % circeVersion,
      "io.circe" %% "circe-parser" % circeVersion,
      "io.circe" %% "circe-literal" % circeVersion,

      //audio
      // https://mvnrepository.com/artifact/org/jaudiotagger
      "org" % "jaudiotagger" % jaudioTaggerVersion,
      // https://mvnrepository.com/artifact/org.jflac/jflac-codec
      "org.jflac" % "jflac-codec" % jFlacVersion,
      // https://github.com/mgdigital/Chromaprint.scala
      // https://acoustid.org/chromaprint
      "com.github.mgdigital" %% "chromaprint" % chromaprintVersion,

      // test
      "org.scalatest" %% "scalatest" % scalaTestVersion % Test,
      "org.scalamock" %% "scalamock" % scalaMockVersion % Test
    ),

    libraryDependencies ++= javaFxModules.map(x => "org.openjfx" % s"javafx-$x" % "11" classifier osName)
  )
