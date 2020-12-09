package org.yankov.mso.application.ui

object Resources {
  object ControlIds {
    val inputTab: String = "input-tab"
    val inputArtifactsTab: String = "input-artifacts-tab"
    val searchTab: String = "search-tab"

    val btnAdd: String = "atn-add"
    val btnRemove: String = "btn-remove"
    val btnClone: String = "btn-clone"
    val btnCopyProperties: String = "btn-copy-properties"
    val btnApplyProperties: String = "btn-apply-properties"
    val btnImportFromClipboard: String = "btn-import-from-clipboard"
    val btnClear: String = "btn-clean"
    val btnLoadTracks: String = "btn-load-tracks"
    val btnEditProperties: String = "btn-edit-properties"
    val btnPlay: String = "btn-play"
    val btnUpload: String = "btn-upload"
    val btnUpdate: String = "btn-update"
    val btnExport: String = "btn-export"
  }

  object Stage {
    val title: String = "База данни - българска народна музика"
  }

  object MainForm {
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
    val _clone: String = "Клониране"
    val copyProperties: String = "Копиране на атрибути"
    val applyProperties: String = "Поставяне на атрибути"
    val importFromClipboard: String = "Въвеждане на заглавия от клипборд"
    val clear: String = "Изчистване"
    val loadTracks: String = "Избор на файлове"
    val editProperties: String = "Редактиране"
    val play: String = "Възпроизвеждане"
    val stop: String = "Стоп"
    val upload: String = "Добавяне в базата данни"
    val update: String = "Запис"
    val export: String = "Експорт"
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
}
