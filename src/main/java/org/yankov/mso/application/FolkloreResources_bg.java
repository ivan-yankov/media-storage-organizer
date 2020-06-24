package org.yankov.mso.application;

import org.yankov.mso.application.ui.ApplicationConsoleLogHandler;
import org.yankov.mso.application.ui.FolkloreMainForm;
import org.yankov.mso.application.ui.controls.FolkloreComboBoxFactory;
import org.yankov.mso.application.ui.tabs.*;
import org.yankov.mso.application.ui.tabs.buttons.Buttons;
import org.yankov.mso.application.utils.FlacPlayer;
import org.yankov.mso.application.utils.FlacProcessor;
import org.yankov.mso.application.utils.FxUtils;
import org.yankov.mso.database.FolkloreEntityCollectionFactory;
import org.yankov.mso.datamodel.FolkloreSearchFactory;
import org.yankov.mso.datamodel.PiecePropertiesUtils;

import java.util.ListResourceBundle;

public class FolkloreResources_bg extends ListResourceBundle {

    @Override
    protected Object[][] getContents() {
        return new Object[][]{
            {FolkloreEntityCollectionFactory.REGION_TRAKIA, "Тракийска"},
            {FolkloreEntityCollectionFactory.REGION_STRANDZHA, "Странджанска"},
            {FolkloreEntityCollectionFactory.REGION_RODOPI, "Родопска"},
            {FolkloreEntityCollectionFactory.REGION_PIRIN, "Пиринска"},
            {FolkloreEntityCollectionFactory.REGION_SHOPSKI, "Шопска"},
            {FolkloreEntityCollectionFactory.REGION_MIZIA, "Северняшка"},
            {FolkloreEntityCollectionFactory.REGION_DOBRUDZHA, "Добруджанска"},
            {FolkloreEntityCollectionFactory.REGION_MACEDONIA, "Вардарска Македония"},
            {FolkloreEntityCollectionFactory.REGION_AUTHORITY, "Авторска"},

            {FolkloreEntityCollectionFactory.INSTRUMENT_KAVAL, "Кавал"},
            {FolkloreEntityCollectionFactory.INSTRUMENT_GAIDA, "Гайда"},
            {FolkloreEntityCollectionFactory.INSTRUMENT_GADULKA, "Гъдулка"},
            {FolkloreEntityCollectionFactory.INSTRUMENT_TAMBURA, "Тамбура"},
            {FolkloreEntityCollectionFactory.INSTRUMENT_TAPAN, "Тъпан"},

            {FolkloreEntityCollectionFactory.SOURCE_TYPE_TAPE, "Лента"},
            {FolkloreEntityCollectionFactory.SOURCE_TYPE_CARTRIDGE, "Касета"},
            {FolkloreEntityCollectionFactory.SOURCE_TYPE_GRAMOPHONE_RECORD, "Грамофонна плоча"},
            {FolkloreEntityCollectionFactory.SOURCE_TYPE_CD, "CD"},
            {FolkloreEntityCollectionFactory.SOURCE_TYPE_VIDEO, "Видео"},
            {FolkloreEntityCollectionFactory.SOURCE_TYPE_INTERNET, "Internet"},

            {FolkloreApplicationSettings.STAGE_TITLE, "База данни - българска народна музика"},

            {FolkloreMainForm.TAB_INPUT, "Въвеждане на записи"},
            {FolkloreMainForm.TAB_INPUT_ARTIFACTS, "Въвеждане на артефакти"},
            {FolkloreMainForm.TAB_OUTPUT, "Справка"},

            {FolklorePieceTable.COL_TITLE, "Заглавие"},
            {FolklorePieceTable.COL_PERFORMER, "Изпълнител"},
            {FolklorePieceTable.COL_ACCOMPANIMENT_PERFORMER, "Съпровод"},
            {FolklorePieceTable.COL_AUTHOR, "Автор"},
            {FolklorePieceTable.COL_ARRANGEMENT_AUTHOR, "Обработка"},
            {FolklorePieceTable.COL_CONDUCTOR, "Диригент"},
            {FolklorePieceTable.COL_SOLOIST, "Солист"},
            {FolklorePieceTable.COL_DURATION, "Времетраене"},
            {FolklorePieceTable.COL_SOURCE, "Източник"},
            {FolklorePieceTable.COL_ETHNOGRAPHIC_REGION, "Област"},
            {FolklorePieceTable.COL_FILE, "Файл"},
            {FolklorePieceTable.COL_NOTE, "Забележка"},

            {Buttons.BTN_ADD, "Добавяне"},
            {Buttons.BTN_REMOVE, "Премахване"},
            {Buttons.BTN_CLONE, "Клониране"},
            {Buttons.BTN_COPY_PROPERTIES, "Копиране на атрибути"},
            {Buttons.BTN_APPLY_PROPERTIES, "Поставяне на атрибути"},
            {Buttons.BTN_IMPORT_FROM_CLIPBOARD, "Въвеждане на заглавия от клипборд"},
            {Buttons.BTN_CLEAR, "Изчистване"},
            {Buttons.BTN_LOAD_ALBUM_TRACKS, "Избор на файлове"},
            {Buttons.BTN_EDIT_PROPERTIES, "Редактиране"},
            {Buttons.BTN_PLAYER_RUN, "Възпроизвеждане"},
            {Buttons.BTN_PLAYER_STOP, "Стоп"},
            {Buttons.BTN_UPLOAD, "Добавяне в базата данни"},
            {Buttons.BTN_UPDATE, "Запис"},
            {Buttons.BTN_EXPORT, "Експорт"},
            {Buttons.UPLOAD_STARTED, "Запис в база данни - начало"},
            {Buttons.UPLOAD_COMPLETED, "Запис в база данни - край"},
            {Buttons.UNABLE_WRITE_FILE, "Неуспешен запис на файл"},
            {Buttons.ERROR_UPDATE_DATA_MODEL, "Неуспешно обновяване на данни"},
            {Buttons.EXPORT_STARTED, "Експорт на файлове - начало"},
            {Buttons.EXPORT_COMPLETED, "Експорт на файлове - край"},

            {FxUtils.SELECT_AUDIO_FILES, "Избор на файлове"},
            {FxUtils.SELECT_EXPORT_DIRECTORY, "Избор на папка за експорт"},
            {FxUtils.FLAC_FILTER_NAME, "FLAC файлове"},
            {FxUtils.FLAC_FILTER_EXT, "*.flac"},
            {FxUtils.CONFIRMATION, "Потвърждение"},
            {
                FxUtils.OVERWRITE_RECORDS_IN_DATABASE,
                "Записите, показани в таблицата ще бъдат обновени в базата данни."
            },
            {FxUtils.CLOSE_APPLICATION, "Изход от програмата."},
            {FxUtils.ARE_YOU_SURE, "Сигурни ли сте?"},
            {FxUtils.YES, "Да"},
            {FxUtils.NO, "Не"},

            {FlacProcessor.FILE_NOT_FOUND, "Не е намерен файл"},
            {
                FlacProcessor.CANNOT_DETECT_AUDIO_FILE_DURATION,
                "Не може да бъде определена продължителността на файл с аудио запис"
            },
            {FlacProcessor.FLAC_ERROR, "Грешка при работа с FLAC файл"},
            {FlacProcessor.UNABLE_READ_FILE, "Грешка при четене на файл"},
            {FlacProcessor.FILE_LOADED, "Успешно прочетен файл"},

            {FlacPlayer.FLAC_PLAY_ERROR, "Грешка при възпроизвеждане на FLAC файл"},

            {ApplicationConsoleLogHandler.ERROR, "Грешка"},
            {ApplicationConsoleLogHandler.WARNING, "Внимание"},
            {ApplicationConsoleLogHandler.INFO, "Съобщение"},

            {FolklorePieceEditor.STAGE_TITLE, "Редактиране на запис"},
            {FolklorePieceEditor.BTN_OK, "OK"},
            {FolklorePieceEditor.BTN_CANCEL, "Отказ"},

            {FolkloreInputArtifactsTab.ARTIFACT_TYPE, "Вид артефакт"},
            {FolkloreInputArtifactsTab.SOURCE, "Източник"},
            {FolkloreInputArtifactsTab.INSTRUMENT, "Инструмент"},
            {FolkloreInputArtifactsTab.ARTIST, "Артист"},
            {FolkloreInputArtifactsTab.ALBUM, "Албум"},
            {FolkloreInputArtifactsTab.ETHNOGRAPHIC_REGION, "Етнографска област"},

            {ArtifactInputControls.EXISTING_ARTIFACTS_LABEL, "Съществуващи артефакти"},
            {ArtifactInputControls.BTN_ADD_ARTIFACT, "Добавяне"},
            {ArtifactInputControls.BTN_SAVE_ARTIFACT, "Запис"},
            {ArtifactInputControls.ARTIFACT_EXISTS, "Артефакт с такава дефиниция вече съществува"},
            {ArtifactInputControls.NO_SELECTED_ARTIFACT, "Не е избран съществуващ артефакт за запис"},
            {ArtifactInputControls.ARTIFACT_ADDED, "Артефактът е добавен успешно"},
            {ArtifactInputControls.ARTIFACT_SAVED, "Промените в артефакта са записани"},

            {SourceInputControls.SIGNATURE, "Сигнатура"},
            {SourceInputControls.SIGNATURE_UNDEFINED, "Не е въведена сигнатура"},

            {InstrumentInputControls.INSTRUMENT, "Название"},
            {InstrumentInputControls.INSTRUMENT_NAME_UNDEFINED, "Не е въведено название на инструмент"},

            {ArtistInputControls.NAME, "Име на артист"},
            {ArtistInputControls.NOTE, "Забележка"},
            {ArtistInputControls.MISSIONS, "Роли"},
            {ArtistInputControls.SINGER, "Певец"},
            {ArtistInputControls.INSTRUMENT_PLAYER, "Инструменталист"},
            {ArtistInputControls.COMPOSER, "Композитор"},
            {ArtistInputControls.CONDUCTOR, "Диригент"},
            {ArtistInputControls.ORCHESTRA, "Оркестър"},
            {ArtistInputControls.CHOIR, "Хор"},
            {ArtistInputControls.ENSEMBLE, "Ансамбъл"},
            {ArtistInputControls.CHAMBER_GROUP, "Камерна група"},
            {ArtistInputControls.ARTIST_NAME_UNDEFINED, "Не е въведено име на артист"},
            {
                ArtistInputControls.ARTIST_INSTRUMENT_UNDEFINED,
                "Избрана е роля 'инструменталист', но не е посочен инструмент"
            },
            {ArtistInputControls.NO_ARTIST_MISSION_SELECTED, "Не е избрана нито една роля"},

            {EthnographicRegionInputControls.NAME, "Название"},
            {EthnographicRegionInputControls.NAME_UNDEFINED, "Не е въведено название"},

            {FolkloreSearchTab.VARIABLE, "Категория"},
            {FolkloreSearchTab.OPERATOR, "Оператор"},
            {FolkloreSearchTab.VALUE, "Стойност"},
            {FolkloreSearchTab.BTN_SEARCH, "Търсене"},
            {FolkloreSearchTab.TOTAL_ITEMS_FOUND, "Брой намерени записи {0}, общо времетраене {1}"},
            {FolkloreSearchTab.PLAY_NEXT, "Възпроизвеждане на следващ запис"},
            {FolkloreSearchTab.PLAY_RANDOM, "Случаен следващ запис"},
            {FolkloreSearchTab.FILTER, "Филтър"},

            {FolkloreSearchFactory.VAR_LABEL_TITLE, "Заглавие"},
            {FolkloreSearchFactory.VAR_LABEL_PERFORMER, "Изпълнител"},
            {FolkloreSearchFactory.VAR_LABEL_ACCOMPANIMENT_PERFORMER, "Съпровод"},
            {FolkloreSearchFactory.VAR_LABEL_ARRANGEMENT_AUTHOR, "Обработка"},
            {FolkloreSearchFactory.VAR_LABEL_CONDUCTOR, "Диригент"},
            {FolkloreSearchFactory.VAR_LABEL_INSTRUMENT_PERFORMANCE, "Изпълнение на инструмент"},
            {FolkloreSearchFactory.VAR_LABEL_INSTRUMENT_ACCOMPANIMENT, "Съпровод на инструмент"},
            {FolkloreSearchFactory.VAR_LABEL_SOLOIST, "Солист"},
            {FolkloreSearchFactory.VAR_LABEL_AUTHOR, "Автор"},
            {FolkloreSearchFactory.VAR_LABEL_ETHNOGRAPHIC_REGION, "Етнографска област"},
            {FolkloreSearchFactory.VAR_LABEL_PIECE_NOTE, "Забележка"},
            {FolkloreSearchFactory.VAR_LABEL_SOURCE_TYPE, "Тип на източник"},
            {FolkloreSearchFactory.VAR_LABEL_SOURCE_SIGNATURE, "Сигнатура на източник"},
            {FolkloreSearchFactory.OPERATOR_LABEL_EQUALS, "Идентично"},
            {FolkloreSearchFactory.OPERATOR_LABEL_NOT_EQUALS, "Различно"},
            {FolkloreSearchFactory.OPERATOR_LABEL_CONTAINS, "Съдържа"},
            {FolkloreSearchFactory.OPERATOR_LABEL_NOT_CONTAINS, "Не съдържа"},
            {FolkloreSearchFactory.OPERATOR_LABEL_STARTS, "Започва с ..."},
            {FolkloreSearchFactory.OPERATOR_LABEL_ENDS, "Завършва с ..."},

            {FolkloreComboBoxFactory.SOURCE_TYPE, "Вид източник"},
            {FolkloreComboBoxFactory.INSTRUMENT, "Инструмент"},
        };
    }

}
