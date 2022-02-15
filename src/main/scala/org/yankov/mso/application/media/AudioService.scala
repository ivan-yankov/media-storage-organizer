package org.yankov.mso.application.media

import cats.effect.IO
import org.http4s.HttpRoutes
import org.http4s.dsl.Http4sDsl
import org.yankov.mso.application.Resources
import org.yankov.mso.application.media.decode.FlacDecoder

import java.io.File
import java.nio.file.Files

object AudioService extends Http4sDsl[IO] {
  private var audioData: Map[Int, File] = Map()

  def setAudioData(audioData: Map[Int, File]): Unit = this.audioData = audioData

  def clearAudioData(): Unit = audioData = Map()

  def createRoutes(): HttpRoutes[IO] = HttpRoutes.of[IO] {
    case GET -> Root => Ok("Media server")

    case GET -> Root / Resources.Media.audioHttpApi / id => Ok(audioBytes(audioData(id.toInt)))
  }

  private def audioBytes(file: File): Array[Byte] =
    FlacDecoder.decode(Files.readAllBytes(file.toPath)).getOrElse(Array())
}
