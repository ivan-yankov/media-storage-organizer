package org.yankov.mso.application.media

import java.io.ByteArrayInputStream

import javax.sound.sampled._
import org.jflac.metadata.StreamInfo
import org.jflac.util.ByteData
import org.jflac.{FLACDecoder, PCMProcessor}
import org.slf4j.LoggerFactory

object Player extends PCMProcessor {
  private val log = LoggerFactory.getLogger(getClass)

  private var line: SourceDataLine = _

  def isPlaying: Boolean = line != null && line.isActive

  def play(bytes: Array[Byte]): Unit = {
    val is = new ByteArrayInputStream(bytes)
    val decoder = new FLACDecoder(is)
    decoder.addPCMProcessor(this)
    decoder.decode()
    close()
  }

  def stop(): Unit = {
    if (line != null) {
      line.stop()
      close()
    }
  }

  override def processStreamInfo(streamInfo: StreamInfo): Unit = {
    try {
      val audioFormat = streamInfo.getAudioFormat
      val info = new DataLine.Info(classOf[SourceDataLine], audioFormat, AudioSystem.NOT_SPECIFIED)
      line = AudioSystem.getLine(info).asInstanceOf[SourceDataLine]
      line.open(audioFormat, AudioSystem.NOT_SPECIFIED)
      line.start()
    } catch {
      case e: Exception =>
        log.error("Unable to play audio.", e)
    }
  }

  override def processPCM(byteData: ByteData): Unit = line.write(byteData.getData(), 0, byteData.getLen)

  private def close(): Unit = {
    if (line != null) {
      line.drain()
      line.close()
    }
  }
}
