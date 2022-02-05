package org.yankov.mso.application

import org.yankov.mso.application.converters.DurationConverter

import java.time.Duration

object Resources {
  object ApplicationArgumentKeys {
    val databaseDirectory: String = "--db-dir"
    val mediaServerPort: String = "--media-server-port"
    val buildAudioIndex: String = "--build-audio-index"
  }

  object ApplicationArgumentValues {
    val mediaServerPort: String = "5432"
  }

  object Media {
    val flacExtension: String = ".flac"
    val playPause: String = "> ||"
    val next: String = ">>"
    val previous: String = "<<"
    val stop: String = "|=|"
    val close: String = "X"
    val host: String = "localhost"
    val audioHttpApi: String = "wav-audio-bytes"
  }

  object MainForm {
    val title: String = "База данни - българска народна музика"
    val inputTab: String = "Въвеждане на записи"
    val inputArtifactsTab: String = "Артефакти"
    val searchTab: String = "Търсене"
    val audioSearchTab: String = "Аудио търсене"
    val extractTitleFromFileName: String = "Заглавие от име на файл"
    val extractTitleFromFileNameRegex: String = "Регулярен израз"
    val invalidRegex: String = "Невалиден регулярен израз"
  }

  object Dialogs {
    val selectAudioFiles: String = "Избор на файлове"
    val selectExportDirectory: String = "Избор на папка за експорт"
    val flacFilterName: String = "FLAC файлове"
    val flacFilterExtension: String = "*.flac"
    val confirmation: String = "Потвърждение"
    val overwriteRecordsInDatabase: String = "Записите, показани в таблицата ще бъдат обновени в базата данни."
    val closeApplication: String = "Изход от програмата."
    val areYouSure: String = "Сигурни ли сте?"
    val yes: String = "Да"
    val no: String = "Не"
    val deleteTrackFromDatabase: String = "Избраните записи ще бъдат изтрити от базата данни."
  }

  object ToolbarButtons {
    val add: String = "Добавяне"
    val remove: String = "Премахване от таблицата"
    val delete: String = "Изтриване от базата данни"
    val cloneItems: String = "Клониране"
    val copyProperties: String = "Копиране на атрибути"
    val applyProperties: String = "Поставяне на атрибути"
    val importFromClipboard: String = "Въвеждане на заглавия от клипборд"
    val clear: String = "Изчистване"
    val loadTracks: String = "Избор на файлове"
    val editTrack: String = "Редактиране"
    val play: String = "Възпроизвеждане"
    val stop: String = "Стоп"
    val upload: String = "Добавяне в базата данни"
    val update: String = "Запис"
    val exportItems: String = "Експорт"
  }

  object TableColumns {
    val id: String = "Идентификатор"
    val title: String = "Заглавие"
    val performer: String = "Изпълнител"
    val accompanimentPerformer: String = "Съпровод"
    val author: String = "Автор"
    val arrangementAuthor: String = "Обработка"
    val conductor: String = "Диригент"
    val soloist: String = "Солист"
    val duration: String = "Време"
    val source: String = "Източник"
    val ethnographicRegion: String = "Област"
    val file: String = "Файл"
    val note: String = "Забележка"
    val sampleId: String = "Образец за търсене"
    val matchType: String = "Тип съвпадение"
    val correlation: String = "Корелация"
  }

  object ConsoleMessages {
    val uploadSuccessful: String = "Успешно обновяване на база данни"
    val uploadFailed: String = "Неуспешно обновяване на база данни"
    val exportCompleted: String = "Успешен експорт на файлове"
    val deleteTrackSuccessful: String = "Успешно изтриване"
    val deleteTrackFailed: String = "Грешка при изтриване"

    def unableToWriteFile(path: String): String = s"Не може да се запише файл $path"

    def errorFingerprintCalculation(id: String): String = s"Грешка при декодиране на flac [$id]"

    def audioIndexItemError(id: String): String = s"Запис с идентификатор [$id] не е индексиран в аудио индекс"
  }

  object Controls {
    val sourceType: String = "Вид източник"
    val instrument: String = "Инструмент"
  }

  object Search {
    val variable: String = "Категория"
    val filter: String = "Филтър"
    val value: String = "Стойност"
    val search: String = "Търсене"
    val audioSearchPane = "Търсене на запис по звуков файл"
    val audioSearchButton: String = "Избор на файлове и търсене"
    val audioSearchClearButton: String = "Изчистване"
    val audioSearchPlayLeftButton: String = "Ляв >"
    val audioSearchPlayRightButton: String = "Десен >"
    val audioSearchError: String = "Грешка при аудио търсене"
    val audioSearchIdentical: String = "Идентично"
    val audioSearchSimilar: String = "Подобно"
    val correlation: String = "Гранична корелация"
    val crossCorrelationShift: String = "Отместване"

    def totalItemsFound(totalCount: Int, totalDuration: Duration): String =
      s"Брой намерени записи $totalCount, общо времетраене ${DurationConverter.toHourMinSecString(totalDuration)}"
  }

  object TrackEditor {
    val title: String = "Редактиране на запис"
  }

  object FormButtons {
    val ok: String = "OK"
    val cancel = "Отказ"
  }

  object Artifacts {
    val existingArtifacts: String = "Съществуващи артефакти"
    val btnAddArtifact: String = "Добавяне"
    val btnUpdateArtifact: String = "Запис"
    val artifactExists: String = "Артефакт с такава дефиниция вече съществува"
    val noSelectedArtifact: String = "Не е избран съществуващ артефакт"
    val artifactAdded: String = "Артефактът е добавен"
    val artifactUpdated: String = "Артефактът е обновен"
    val artifactUpdateFailed: String = "Грешка при обновяване на артефакт"
  }

  object Sources {
    val signature: String = "Сигнатура"
  }

  object EthnographicRegions {
    val name: String = "Название"
    val nameUndefined: String = "Не е въведено название"
  }

  object Instruments {
    val instrument: String = "Название"
    val instrumentNameUndefined: String = "Не е въведено название на инструмент"
  }

  object Artists {
    val artistName: String = "Име"
    val artistDisplayName: String = "Съставно име"
    val addMember: String = "Добавяне"
    val clearMembers: String = "Изчистване"
    val members: String = "Състав"
    val instruments: String = "Инструменти"
    val note: String = "Забележка"
    val missions: String = "Роли"
    val singer: String = "Певец"
    val instrumentPlayer: String = "Инструменталист"
    val composer: String = "Композитор"
    val conductor: String = "Диригент"
    val orchestra: String = "Оркестър"
    val choir: String = "Хор"
    val ensemble: String = "Ансамбъл"
    val chamberGroup: String = "Камерна група"
    val concertHost: String = "Водещ"
    val artistNameUndefined: String = "Не е въведено име на артист"
    val artistInstrumentUndefined: String = "Избрана е роля 'инструменталист', но не е посочен инструмент"
    val noArtistMissionSelected: String = "Не е избрана нито една роля"
  }

  object ArtifactsTab {
    val artifactType: String = "Вид артефакт"
    val source: String = "Източник"
    val instrument: String = "Инструмент"
    val artist: String = "Артист"
    val ethnographicRegion: String = "Етнографска област"
  }

  object Variables {
    val title: String = "Заглавие"
    val performer: String = "Изпълнител"
    val accompanimentPerformer: String = "Съпровод"
    val arrangementAuthor: String = "Обработка"
    val conductor: String = "Диригент"
    val instrumentPerformance: String = "Изпълнение на инструмент"
    val instrumentAccompaniment: String = "Съпровод на инструмент"
    val soloist: String = "Солист"
    val author: String = "Автор"
    val ethnographicRegion: String = "Етнографска област"
    val trackNote: String = "Забележка"
    val sourceType: String = "Тип на източник"
    val sourceSignature: String = "Сигнатура на източник"
    val id: String = "Идентификатор"
  }

  object Filters {
    val equalsLabel: String = "Идентично"
    val notEqualsLabel: String = "Различно"
    val containsLabel: String = "Съдържа"
    val notContainsLabel: String = "Не съдържа"
    val emptyLabel: String = "Празно"
  }
}
