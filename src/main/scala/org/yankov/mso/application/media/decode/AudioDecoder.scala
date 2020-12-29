package org.yankov.mso.application.media.decode

import java.nio.file.Path

trait AudioDecoder {
  def decode(bytes: Array[Byte]): Option[Array[Byte]]

  def decode(inputFile: Path, outputFile: Path): Boolean
}
