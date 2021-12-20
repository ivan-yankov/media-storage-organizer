package org.yankov.mso.application.media

import org.scalatest.{FreeSpec, Matchers}
import org.yankov.mso.application.database.FakeDatabase
import org.yankov.mso.application.model.DatabaseModel.DbAudioIndexItem
import org.yankov.mso.application.model.DatabasePaths

import java.nio.file.Paths

class AudioIndexTest extends FreeSpec with Matchers {
  private val dbPaths = DatabasePaths(Paths.get("db-dir"))

  "build" in {
    val db = FakeDatabase()
    db.setInsertResult(Right(()))
    val audioIndex = AudioIndex(db, dbPaths)
    val inputs = List(1, 2, 3, 4, 5)
      .map(x => s"0$x" -> getClass.getResourceAsStream(s"/audio-search/0$x.flac"))
      .toMap
    audioIndex.build(inputs)
    db.getInsertEntries.map(x => x.asInstanceOf[DbAudioIndexItem]).size shouldBe 5
  }

  "remove item" in {
    val db = FakeDatabase()
    db.setInsertResult(Right(()))
    db.setDeleteResult(Right(1))
    val audioIndex = AudioIndex(db, dbPaths)
    val inputs = List(1, 2, 3, 4, 5)
      .map(x => s"0$x" -> getClass.getResourceAsStream(s"/audio-search/0$x.flac"))
      .toMap

    audioIndex.build(inputs)
    db.getInsertEntries.map(x => x.asInstanceOf[DbAudioIndexItem]).size shouldBe 5
    audioIndex.remove("03") shouldBe true
  }
}
