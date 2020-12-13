package org.yankov.mso.application

import java.time.Duration

import org.yankov.mso.application.converters.DurationConverter

object Resources {

  object MainForm {
    val title: String = "База данни - българска народна музика"
    val inputTab: String = "Въвеждане на записи"
    val inputArtifactsTab: String = "Въвеждане на артефакти"
    val searchTab: String = "Справка"
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
  }

  object ToolbarButtons {
    val add: String = "Добавяне"
    val remove: String = "Премахване"
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
  }

  object ConsoleMessages {
    val uploadStarted: String = "Запис в база данни - начало"
    val uploadCompleted: String = "Запис в база данни - край"
    val unableWriteFile: String = "Неуспешен запис на файл"
    val errorUpdateDataModel: String = "Неуспешно обновяване на данни"
    val exportStarted: String = "Експорт на файлове - начало"
    val exportCompleted: String = "Експорт на файлове - край"
  }

  object Controls {
    val sourceType: String = "Вид източник"
    val instrument: String = "Инструмент"
  }

  object Search {
    val variable: String = "Категория"
    val operator: String = "Оператор"
    val value: String = "Стойност"
    val search: String = "Търсене"
    def totalItemsFound(totalCount: Int, totalDuration: Duration): String =
      s"Брой намерени записи $totalCount, общо времетраене ${DurationConverter.toString(totalDuration)}"
    val filter: String = "Филтър"
  }

  object TrackEditor {
    val title: String = "Редактиране на запис"
  }

  object FormButtons {
    val ok: String = "OK"
    val cancel = "Отказ"
  }
}
