import java.io.{FileInputStream, RandomAccessFile}
import java.nio.file.{Files, Path, Paths}

import scala.jdk.CollectionConverters._

object Main {
  private val columnDelimiter = "#"

  def main(args: Array[String]): Unit = {
    val expectedNumberOfArguments = 3
    if (args.length != expectedNumberOfArguments) {
      println(s"Wrong number of arguments. Expected $expectedNumberOfArguments")
      return
    }

    val folkloreTrackTable = Paths.get(args(0))
    val folkloreTrackBin = Paths.get(args(1))
    val outputDir = Paths.get(args(2))

    val folklorePieces = Files.readAllLines(folkloreTrackTable).asScala.toList

    val in = new RandomAccessFile(folkloreTrackBin.toFile, "r")

    val recordInfo = parse(folklorePieces)
    recordInfo.foreach(x => {
      val bytes: Array[Byte] = Array.ofDim(x._3.toInt)
      in.seek(x._2.toLong)
      in.read(bytes)
      val out: Path = Paths.get(outputDir.toString, x._1 + ".flac")
      Files.write(out, bytes)
      println("Created: " + out.toString)
    })

    in.close()
  }

  def parse(folklorePieces: List[String]): List[(String, String, String)] = {
    folklorePieces
      .map(x => {
        val columns = x.split(columnDelimiter)
        val record = columns(12).replace("@", "").replace("/", "").split("\\.").reverse
        (columns(0), record.tail.head, record.head)
      })
  }
}
