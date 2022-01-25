package org.yankov.mso.application.media

import cats.effect.IO
import org.http4s.HttpRoutes
import org.http4s.dsl.Http4sDsl
import org.yankov.mso.application.Resources

object AudioService extends Http4sDsl[IO] {
  private var audioData: Map[String, Array[Byte]] = Map()

  def setAudioData(audioData: Map[String, Array[Byte]]): Unit = this.audioData = audioData

  def clearAudioData(): Unit = {
    audioData = Map()
    System.gc()
  }

  def createRoutes(): HttpRoutes[IO] = HttpRoutes.of[IO] {
    case GET -> Root => Ok("Media server")

    case GET -> Root / Resources.Media.audioHttpApi / id => Ok(audioData(id))
  }
}
