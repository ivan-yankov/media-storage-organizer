package org.yankov.mso.application

import org.yankov.mso.application.database.RealDatabase
import org.yankov.mso.application.model.DataManager
import org.yankov.mso.application.model.DataModel._

import java.nio.file.{Files, Path, Paths}
import java.util.UUID
import scala.collection.JavaConverters._

object ImportDatabase {
  private val columnDelimiter = "#"
  private val textWrapper = "@"

  def main(args: Array[String]): Unit = {
    val dir = args(0)
    val dataManager = DataManager(dir, RealDatabase(), doIndex = false)

    clear(Paths.get(dir, "data"))

    val sourceTypesInput = Paths.get(dir, "source-type.txt")
    val sourceTypesRaw = parse(sourceTypesInput)
    assert(sourceTypesRaw.forall(x => x.length == 2))
    val sourceTypeIds = sourceTypesRaw.map(x => (x(0), generateId)).toMap
    val sourceTypes = sourceTypesRaw.map(x => (x(0), x(1).replace(textWrapper, "")))
    insertSourceTypes(sourceTypeIds, sourceTypes, dataManager)

    println("Done")
  }

  private def clear(path: Path): Unit = {
    val sourceTypes = Paths.get(path.toString, "source-types")
    Files.deleteIfExists(sourceTypes)
    Files.createFile(sourceTypes)
  }

  private def parse(path: Path): List[Array[String]] = {
    Files
      .readAllLines(path)
      .asScala
      .toList
      .map(x => x.split(columnDelimiter))
  }

  private def generateId: String = UUID.randomUUID.toString

  private def insertSourceTypes(ids: Map[String, String],
                                sourceTypes: List[(String, String)],
                                dataManager: DataManager): Unit = {
    val result = sourceTypes
      .map(x => SourceType(id = ids(x._1), name = x._2))
      .map(x => dataManager.insertSourceType(x))
      .forall(x => x)
    assert(result)
  }
}
