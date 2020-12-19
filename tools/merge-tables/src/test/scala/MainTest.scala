import org.scalatest.freespec.AnyFreeSpec
import org.scalatest.matchers.should.Matchers

class MainTest extends AnyFreeSpec with Matchers {
  "process" - {
    val folklorePieces = List(
      "24701#@00:02:28@##@title1@#7#304##1#5303#5##10901#7##@FLAC@",
      "24702#@00:03:10@##@title2@#7#501##501#5303#1##10901#7##@FLAC@",
      "24703#@00:02:28@##@title3@#7#501##501#5303#3##10901#7##@FLAC@"

    )
    val records = List(
      "1#@ABC@#@FLAC@",
      "2#@DEF@#@FLAC@",
      "3#@GHI@#@FLAC@",
      "4#@JKL@#@FLAC@",
      "5#@MNO@#@FLAC@",
      "6#@PQR@#@FLAC@",
      "7#@STU@#@FLAC@",
      "8#@VWX@#@FLAC@",
      "9#@YZZ@#@FLAC@"
    )
    val expectedOutput = List(
      "24701#@00:02:28@##@title1@#7#304##1#5303##10901#7#@MNO@#@FLAC@",
      "24702#@00:03:10@##@title2@#7#501##501#5303##10901#7#@ABC@#@FLAC@",
      "24703#@00:02:28@##@title3@#7#501##501#5303##10901#7#@GHI@#@FLAC@"
    )

    "should succeed" in {
      Main.process(folklorePieces, records).map(x => x.getOrElse(List())) shouldBe expectedOutput
    }
  }
}
