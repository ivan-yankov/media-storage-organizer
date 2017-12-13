package org.yankov.mso.application;

import org.yankov.mso.application.ui.ApplicationConsoleLogHandler;
import org.yankov.mso.application.ui.FolkloreScene;
import org.yankov.mso.application.ui.input.FolkloreInputButtons;
import org.yankov.mso.application.ui.input.FolkloreInputTable;
import org.yankov.mso.application.utils.FileUtils;
import org.yankov.mso.database.folklore.FolkloreEntityCollectionFactory;

import java.util.ListResourceBundle;

public class FolkloreResources_bg extends ListResourceBundle {

    @Override
    protected Object[][] getContents() {
        return new Object[][] {
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

                {FolkloreApplicationSettings.STAGE_TITLE, "Управление на база данни с българска народна музика"},

                {FolkloreScene.TAB_INPUT, "Въвеждане"},
                {FolkloreScene.TAB_OUTPUT, "Справка"},

                {FolkloreInputTable.COL_ALBUM, "Албум"},
                {FolkloreInputTable.COL_ALBUM_TRACK_ORDER, "Номер в албума"},
                {FolkloreInputTable.COL_TITLE, "Заглавие"},
                {FolkloreInputTable.COL_PERFORMER, "Изпълнител"},
                {FolkloreInputTable.COL_ACCOMPANIMENT_PERFORMER, "Съпровод"},
                {FolkloreInputTable.COL_AUTHOR, "Автор"},
                {FolkloreInputTable.COL_ARRANGEMENT_AUTHOR, "Обработка"},
                {FolkloreInputTable.COL_CONDUCTOR, "Диригент"},
                {FolkloreInputTable.COL_SOLOIST, "Солист"},
                {FolkloreInputTable.COL_DURATION, "Времетраене"},
                {FolkloreInputTable.COL_SOURCE, "Източник"},
                {FolkloreInputTable.COL_ETHNOGRAPHIC_REGION, "Област"},
                {FolkloreInputTable.COL_FILE, "Файл"},
                {FolkloreInputTable.COL_NOTE, "Забележка"},

                {FolkloreInputButtons.BTN_ADD, "Добавяне"},
                {FolkloreInputButtons.BTN_REMOVE, "Премахване"},
                {FolkloreInputButtons.BTN_COPY, "Копиране"},
                {FolkloreInputButtons.BTN_CLEAR, "Изчистване"},
                {FolkloreInputButtons.BTN_LOAD_ALBUM_TRACKS, "Зареждане на файлове"},
                {FolkloreInputButtons.SELECT_AUDIO_FILES, "Избор на файлове"},
                {FolkloreInputButtons.FLAC_FILTER_NAME, "FLAC файлове"},
                {FolkloreInputButtons.FLAC_FILTER_EXT, "*.flac"},

                {FileUtils.FILE_NOT_FOUND, "Не е намерен файл"}, {
                        FileUtils.CANNOT_DETECT_AUDIO_FILE_DURATION,
                        "Не може да бъде определена продължителността на файл с аудио запис"
                },

                {ApplicationConsoleLogHandler.ERROR, "Грешка"},
                {ApplicationConsoleLogHandler.WARNING, "Внимание"},
                {ApplicationConsoleLogHandler.INFO, "Съобщение"},
                };
    }

}
