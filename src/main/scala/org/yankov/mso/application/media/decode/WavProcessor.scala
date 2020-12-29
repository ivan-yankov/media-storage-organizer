package org.yankov.mso.application.media.decode

import java.io.{IOException, OutputStream}

import org.jflac.PCMProcessor
import org.jflac.metadata.StreamInfo
import org.jflac.util.{ByteData, WavWriter}
import org.slf4j.LoggerFactory

case class WavProcessor(outputStream: OutputStream) extends PCMProcessor {
  private val log = LoggerFactory.getLogger(getClass)
  private val wav = new WavWriter(outputStream)

  override def processStreamInfo(streamInfo: StreamInfo): Unit = {
    try {
      wav.writeHeader(streamInfo)
    }
    catch {
      case e: IOException =>
        log.error("Error on processing stream info", e)
    }
  }

  override def processPCM(byteData: ByteData): Unit = {
    try {
      wav.writePCM(byteData)
    }
    catch {
      case e: IOException =>
        log.error("Error on processing byte data", e)
    }
  }
}
