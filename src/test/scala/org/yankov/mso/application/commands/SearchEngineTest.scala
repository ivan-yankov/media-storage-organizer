package org.yankov.mso.application.commands

import java.time.Duration

import org.scalatest.{FreeSpec, Matchers}
import org.yankov.mso.application.model.DataModel._
import org.yankov.mso.application.search.SearchEngine
import org.yankov.mso.application.search.SearchModel.Filters._
import org.yankov.mso.application.search.SearchModel.Variables._
import org.yankov.mso.application.search.SearchModel._

class SearchEngineTest extends FreeSpec with Matchers {
  private val comparator: (FolkloreTrack, FolkloreTrack) => Boolean = (x, y) => x.id < y.id
  private val getDuration: FolkloreTrack => Duration = x => x.duration

  "search" - {
    val t1 = FolkloreTrack(
      title = "Недо ле, Недке, хубава",
      performer = Artist(name = "Борис Машалов"),
      accompanimentPerformer = Artist(name = "Оркестър"),
      arrangementAuthor = Artist(name = "Стефан Кънев"),
      conductor = Artist(name = "Коста Колев"),
      source = Source(sourceType = SourceType(name = "Лента"), signature = "3506")
    )
    val t2 = FolkloreTrack(
      title = "Димитър кара гемия",
      performer = Artist(name = "Вълкана Стоянова"),
      accompanimentPerformer = Artist(name = "Оркестър"),
      arrangementAuthor = Artist(name = "Христо Тодоров"),
      conductor = Artist(name = "Коста Колев")
    )
    val t3 = FolkloreTrack(
      title = "Полегнала е Тодора",
      performer = Artist(name = "Ансамбъл"),
      arrangementAuthor = Artist(name = "Филип Кутев"),
      conductor = Artist(name = "Филип Кутев"),
      source = Source(sourceType = SourceType(name = "DDD"), signature = "3507")
    )
    val t4 = FolkloreTrack(
      title = "Стани ми, майчо, откачи",
      performer = Artist(name = "Ансамбъл"),
      arrangementAuthor = Artist(name = "Красимир Кюркчийски"),
      conductor = Artist(name = "Красимир Кюркчийски"),
      soloist = Artist(name = "Надежда Хвойнева")
    )
    val t5 = FolkloreTrack(
      title = "Ръченица",
      performer = Artist(name = "Никола Ганчев", instrument = Instrument(name = "Кавал")),
      accompanimentPerformer = Artist(name = "Група"),
      arrangementAuthor = Artist(name = "Красимир Кюркчийски"),
      source = Source(sourceType = SourceType(name = "Лента"), signature = "3506")
    )
    val tracks = List(t1, t2, t3, t4, t5)

    "contains title" in {
      val filters = List(SearchParameters(varTitle, filterContains, "недке хубава"))
      val (found, _) = SearchEngine.search[FolkloreTrack](tracks, filters, comparator, getDuration)
      found shouldBe List(t1)
    }

    "not contains title" in {
      val parameters = List(SearchParameters(varTitle, filterNotContains, "недке хубава"))
      val tracksWithIds = tracks.zip(tracks.reverse.indices).map(x => x._1.withId(x._2 + 1))
      val (found, _) = SearchEngine.search[FolkloreTrack](
        tracksWithIds,
        parameters,
        comparator,
        getDuration
      )
      found shouldBe tracksWithIds.tail
    }

    "equals title" in {
      val parameters = List(SearchParameters(varTitle, filterEquals, "стани ми майчо откачи"))
      val (found, _) = SearchEngine.search[FolkloreTrack](tracks, parameters, comparator, getDuration)
      found shouldBe List(t4)
    }

    "not equals title" in {
      val parameters = List(SearchParameters(varTitle, filterNotEquals, "ръченица"))
      val (found, _) = SearchEngine.search[FolkloreTrack](tracks, parameters, comparator, getDuration)
      found shouldBe tracks.reverse.tail.reverse
    }

    "contains performer" in {
      val parameters = List(SearchParameters(varPerformer, filterContains, "анс"))
      val (found, _) = SearchEngine.search[FolkloreTrack](tracks, parameters, comparator, getDuration)
      found shouldBe List(t3, t4)
    }

    "contains arrangement author" in {
      val parameters = List(SearchParameters(varArrangementAuthor, filterContains, "кюрк"))
      val (found, _) = SearchEngine.search[FolkloreTrack](tracks, parameters, comparator, getDuration)
      found shouldBe List(t4, t5)
    }

    "contains accompaniment performer" in {
      val parameters = List(SearchParameters(varAccompanimentPerformer, filterContains, "орк"))
      val (found, _) = SearchEngine.search[FolkloreTrack](tracks, parameters, comparator, getDuration)
      found shouldBe List(t1, t2)
    }

    "contains instrument performance" in {
      val parameters = List(SearchParameters(varInstrumentPerformance, filterContains, "кавал"))
      val (found, _) = SearchEngine.search[FolkloreTrack](tracks, parameters, comparator, getDuration)
      found shouldBe List(t5)
    }

    "contains source type" in {
      val parameters = List(SearchParameters(varSourceType, filterContains, "лента"))
      val (found, _) = SearchEngine.search[FolkloreTrack](tracks, parameters, comparator, getDuration)
      found shouldBe List(t1, t5)
    }

    "contains source signature" in {
      val parameters = List(SearchParameters(varSourceSignature, filterContains, "07"))
      val (found, _) = SearchEngine.search[FolkloreTrack](tracks, parameters, comparator, getDuration)
      found shouldBe List(t3)
    }

    "multiple filters" in {
      val parameters = List(
        SearchParameters(varPerformer, filterContains, "анс"),
        SearchParameters(varSoloist, filterContains, "надежда")
      )
      val (found, _) = SearchEngine.search[FolkloreTrack](tracks, parameters, comparator, getDuration)
      found shouldBe List(t4)
    }
  }
}
