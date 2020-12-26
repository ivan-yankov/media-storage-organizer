import org.scalatest.freespec.AnyFreeSpec
import org.scalatest.matchers.should.Matchers

class MainTest extends AnyFreeSpec with Matchers {
  "parse" - {
    val folkloreTracks = List(
      "24701#@00:02:28@##@Стойчо седи на дюкяна@#7#304##1#5303##10901#7#@folklore-track.bin.0.14933090/@#@FLAC@",
      "24702#@00:03:10@##@Иван Неделя думаше@#7#501##501#5303##10901#7#@folklore-track.bin.14933090.19364081/@#@FLAC@",
      "24703#@00:02:28@##@Горице ле, лилянова@#7#501##501#5303##10901#7#@folklore-track.bin.34297171.15459780/@#@FLAC@"
    )
    val expectedOutput = List(
      ("24701", "0", "14933090"),
      ("24702", "14933090", "19364081"),
      ("24703", "34297171", "15459780")
    )

    "should succeed" in {
      Main.parse(folkloreTracks) shouldBe expectedOutput
    }
  }
}
