package org.yankov.mso.application.media

import org.scalatest.{FreeSpec, Matchers}
import org.yankov.mso.application.database.FakeDatabase
import org.yankov.mso.application.model.DataModel.AudioSearchResult
import org.yankov.mso.application.model.DatabaseModel.DbAudioIndexItem
import org.yankov.mso.application.model.DatabasePaths

import java.nio.file.Paths

class AudioIndexTest extends FreeSpec with Matchers {
  private val dbPaths = DatabasePaths(Paths.get("db-dir"))

  "audio index" - {
    val db = FakeDatabase()
    db.setInsertResult(Right(()))
    val audioIndex = AudioIndex(db, dbPaths)
    val inputs = List(1, 2, 3, 4, 5)
      .map(x => s"0$x" -> getClass.getResourceAsStream(s"/audio-search/0$x.flac"))
      .toMap
    audioIndex.build(inputs)

    "build" in {
      db.getInsertEntries.map(x => x.asInstanceOf[DbAudioIndexItem]).size shouldBe 5
    }

    "search" - {
      "exact match" in {
        db.setReadResult(Right(db.getInsertEntries.map(x => x.asInstanceOf[DbAudioIndexItem])))
        audioIndex.search(
          Map("identical-sample" -> getClass.getResourceAsStream(s"/audio-search/identical-sample.flac")),
          0.9,
          50
        ) shouldBe List(
          AudioSearchResult(
            sampleId = "identical-sample",
            exactMatches = List("01"),
            similarMatches = List()
          )
        )
      }

      "similar match" in {
        db.setReadResult(Right(db.getInsertEntries.map(x => x.asInstanceOf[DbAudioIndexItem])))
        audioIndex.search(
          Map("similar-sample" -> getClass.getResourceAsStream(s"/audio-search/similar-sample.flac")),
          0.9,
          50
        ) shouldBe List(
          AudioSearchResult(
            sampleId = "similar-sample",
            exactMatches = List(),
            similarMatches = List("03")
          )
        )
      }
    }
  }
}
