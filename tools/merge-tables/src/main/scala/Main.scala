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

    val folklorePiecesFile = Paths.get(args(0))
    val recordsFile = Paths.get(args(1))
    val outputFile = Paths.get(args(2))

    val folklorePieces = Files.readAllLines(folklorePiecesFile).asScala.toList
    val records = Files.readAllLines(recordsFile).asScala.toList

    val result = process(folklorePieces, records)

    if (result.forall(x => x.isRight)) {
      writeFile(result.map(x => x.getOrElse("")).mkString("\n") + "\n", outputFile)
      println("Merge tables successful.")
    }
    else {
      writeFile(result.filter(x => x.isLeft).map(x => x.swap.getOrElse("")).mkString("\n") + "\n", outputFile)
      println("Error during merging tables. Some record ids were not found.")
    }
  }

  def process(folklorePieces: List[String], records: List[String]): List[Either[String, String]] = {
    val recordColumns = records
      .map(x => {
        val columns = x.split(columnDelimiter)
        (columns(0), columns(1), columns(2))
      })

    folklorePieces.map(x => processFolklorePieceRecord(x, recordColumns))
  }

  private def processFolklorePieceRecord(folklorePieceRecord: String, records: List[(String, String, String)]): Either[String, String] = {
    val columns = folklorePieceRecord.split(columnDelimiter)
    val recordId = columns(9)
    val record = findRecord(recordId, records)
    if (record.nonEmpty) {
      val newLineColumns: List[String] = List(
        columns(0),
        columns(1),
        columns(2),
        columns(3),
        columns(4),
        columns(5),
        columns(6),
        columns(7),
        columns(8),
        columns(10),
        columns(11),
        columns(12),
        record,
        columns(14),
      )
      Right(newLineColumns.mkString(columnDelimiter))
    }
    else Left(s"Record with id [$recordId] not found.")
  }

  private def findRecord(recordId: String, records: List[(String, String, String)]): String = {
    val result = records
      .find(x => x._1.equals(recordId))
      .getOrElse(("", "", ""))
    result._2
  }

  private def writeFile(line: String, file: Path): Unit = Files.write(file, line.getBytes("UTF-8"))
}
