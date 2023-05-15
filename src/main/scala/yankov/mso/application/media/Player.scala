package yankov.mso.application.media

import yankov.mso.application.{Main, Resources}

import java.io.File
import scala.sys.process.Process

object Player {
  private var player: FolkloreTrackMediaPlayer = _

  def play(files: List[File]): Unit = {
    if (Main.programArguments.getExternalPlayer.nonEmpty) {
      val cmd = Main.programArguments.getExternalPlayer + " " + files.map(_.toPath.toString).mkString(" ")
      Process(cmd).run()
    }
    else {
      val port = Main.programArguments.getMediaServerPort
      val urls = files.zipWithIndex.map(x => s"http://localhost:$port/${Resources.Media.audioHttpApi}/${x._2}").toVector

      AudioService.setAudioData(files.zipWithIndex.map(x => x._2 -> x._1).toMap)

      player = FolkloreTrackMediaPlayer(urls, AudioService.clearAudioData)
      player.start()
    }
  }
}
