package org.yankov.mso.application.media.decode

import java.io.{ByteArrayInputStream, ByteArrayOutputStream, IOException}
import java.nio.file.{Files, Path}

import org.jflac.FLACDecoder
import org.slf4j.LoggerFactory

object FlacDecoder extends AudioDecoder {
  private val log = LoggerFactory.getLogger(getClass)

  override def decode(bytes: Array[Byte]): Option[Array[Byte]] = {
    try {
      val is = new ByteArrayInputStream(bytes)
      val os = new ByteArrayOutputStream
      val decoder = new FLACDecoder(is)
      decoder.addPCMProcessor(WavProcessor(os))
      decoder.decode()
      Option(os.toByteArray)
    } catch {
      case e: IOException =>
        log.error("Unable to decode FLAC bytes to WAVE bytes", e)
        Option.empty
    }
  }

  override def decode(inputFile: Path, outputFile: Path): Boolean = {
    val input = Files.readAllBytes(inputFile)
    val output = decode(input)
    if (output.isDefined) {
      Files.write(outputFile, output.get)
      true
    }
    else {
      log.error(s"Unable to decode FLAC file [${inputFile.toString}] to WAVE file [${outputFile.toString}]")
      false
    }
  }
}
