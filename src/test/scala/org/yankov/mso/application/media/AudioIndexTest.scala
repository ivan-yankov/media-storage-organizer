package org.yankov.mso.application.media

import org.scalatest.{FreeSpec, Matchers}
import org.yankov.mso.application.database.FakeDatabase
import org.yankov.mso.application.model.DataModel.{AudioSearchResult, AudioSearchSample, ExactMatch, SimilarMatch}
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
      .map(x => AudioSearchSample(s"0$x", getClass.getResourceAsStream(s"/audio-search/0$x.flac")))
    audioIndex.build(inputs)

    "build" in {
      db.getInsertEntries.map(x => x.asInstanceOf[DbAudioIndexItem]).size shouldBe 5
    }

    "search" - {
      "exact match" in {
        db.setReadResult(Right(db.getInsertEntries.map(x => x.asInstanceOf[DbAudioIndexItem])))
        val result = audioIndex.search(
          List(AudioSearchSample("identical-sample", getClass.getResourceAsStream(s"/audio-search/identical-sample.flac"))),
          0.9,
          50
        )
        result.size shouldBe 1
        result.head.size shouldBe 1
        result.head.head.sample.id shouldBe "identical-sample"
        result.head.head.matchId shouldBe "01"
        result.head.head.matchType shouldBe ExactMatch
        result.head.head.correlation shouldBe 1.0
      }

      "similar match" in {
        db.setReadResult(Right(db.getInsertEntries.map(x => x.asInstanceOf[DbAudioIndexItem])))
        val result = audioIndex.search(
          List(AudioSearchSample("similar-sample", getClass.getResourceAsStream(s"/audio-search/similar-sample.flac"))),
          0.9,
          50
        )
        result.size shouldBe 1
        result.head.size shouldBe 1
        result.head.head.sample.id shouldBe "similar-sample"
        result.head.head.matchId shouldBe "03"
        result.head.head.matchType shouldBe SimilarMatch
      }
    }
  }
}
