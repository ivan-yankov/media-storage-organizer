package yankov.mso.application.media

import cats.effect.{ContextShift, Fiber, IO, Timer}
import org.http4s.HttpApp
import org.http4s.implicits._
import org.http4s.server.blaze.BlazeServerBuilder
import yankov.mso.application.Main.getApplicationArgument
import yankov.mso.application.Resources

import scala.concurrent.ExecutionContext
import scala.concurrent.ExecutionContext.Implicits.global

object MediaServer {
  implicit val executionContext: ExecutionContext = global
  implicit val cs: ContextShift[IO] = IO.contextShift(executionContext)
  implicit val timer: Timer[IO] = IO.timer(executionContext)

  private var fiber: Fiber[IO, Nothing] = _

  private val port = getApplicationArgument(
    Resources.ApplicationArgumentKeys.mediaServerPort,
    Resources.ApplicationArgumentValues.mediaServerPort,
    required = false
  ).toInt

  private val serverBuilder = BlazeServerBuilder[IO]
    .withoutBanner
    .bindHttp(port, Resources.Media.host)
    .withHttpApp(service())

  def start(): Unit = fiber = serverBuilder.resource.use(_ => IO.never).start.unsafeRunSync()

  def stop(): Unit = fiber.cancel.unsafeRunSync()

  private def service(): HttpApp[IO] = AudioService.createRoutes().orNotFound
}
