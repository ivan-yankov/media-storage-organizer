package org.yankov.mso.application

import cats.effect.IO
import org.http4s.HttpRoutes
import org.http4s.dsl.Http4sDsl

object AudioService extends Http4sDsl[IO] {
  private var audioData: Array[Byte] = Array()

  def setAudioData(audioData: Array[Byte]): Unit = this.audioData = audioData

  def createRoutes(): HttpRoutes[IO] = HttpRoutes.of[IO] {
    case GET -> Root => Ok("Media server")

    case GET -> Root / Resources.Media.audioHttpApi => Ok(audioData)
  }
}
