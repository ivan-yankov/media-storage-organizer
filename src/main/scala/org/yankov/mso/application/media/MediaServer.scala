package org.yankov.mso.application.media

import cats.effect.{ExitCode, IO, IOApp}
import cats.syntax.all._
import org.http4s.HttpApp
import org.http4s.implicits._
import org.http4s.server.blaze.BlazeServerBuilder
import org.yankov.mso.application.Main.getApplicationArgument
import org.yankov.mso.application.{AudioService, Resources}

object MediaServer extends IOApp {
  override def run(args: List[String]): IO[ExitCode] = {
    val port = getApplicationArgument(
      Resources.ApplicationArgumentKeys.mediaServerPort,
      Resources.ApplicationArgumentValues.mediaServerPort,
      required = false
    ).toInt

    for {
      s <- server(port).serve.compile.drain.as(ExitCode.Success)
    } yield s
  }

  private def server(port: Int): BlazeServerBuilder[IO] = {
    BlazeServerBuilder[IO]
      .withoutBanner
      .bindHttp(port, "localhost")
      .withHttpApp(service())
  }

  private def service(): HttpApp[IO] = AudioService.createRoutes().orNotFound
}
