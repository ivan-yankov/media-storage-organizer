package org.yankov.mso.datamodel.folklore;

import org.yankov.mso.application.folklore.FolkloreApplicationSettings;
import org.yankov.mso.application.folklore.FolkloreInputTabControls;
import org.yankov.mso.application.folklore.FolkloreScene;

import java.util.ListResourceBundle;

public class FolkloreResources_bg extends ListResourceBundle {

    @Override
    protected Object[][] getContents() {
        return new Object[][] {
                { FolkloreResources.REGION_TRAKIA, "Тракийска" },
                { FolkloreResources.REGION_STRANDZHA, "Странджанска" },
                { FolkloreResources.REGION_RODOPI, "Родопска" },
                { FolkloreResources.REGION_PIRIN, "Пиринска" },
                { FolkloreResources.REGION_SHOPSKI, "Шопска" },
                { FolkloreResources.REGION_MIZIA, "Северняшка" },
                { FolkloreResources.REGION_DOBRUDZHA, "Добруджанска" },
                { FolkloreResources.REGION_MACEDONIA, "Вардарска Македония" },
                { FolkloreResources.REGION_AUTHORITY, "Авторска" },

                { FolkloreResources.INSTRUMENT_KAVAL, "Кавал" },
                { FolkloreResources.INSTRUMENT_GAIDA, "Гайда" },
                { FolkloreResources.INSTRUMENT_GADULKA, "Гъдулка" },
                { FolkloreResources.INSTRUMENT_TAMBURA, "Тамбура" },
                { FolkloreResources.INSTRUMENT_TAPAN, "Тъпан" },

                { FolkloreResources.SOURCE_TYPE_TAPE, "Лента" },
                { FolkloreResources.SOURCE_TYPE_CARTRIDGE, "Касета" },
                { FolkloreResources.SOURCE_TYPE_GRAMOPHONE_RECORD, "Грамофонна плоча" },
                { FolkloreResources.SOURCE_TYPE_CD, "CD" },
                { FolkloreResources.SOURCE_TYPE_VIDEO, "Видео" },
                { FolkloreResources.SOURCE_TYPE_INTERNET, "Internet" },

                { FolkloreApplicationSettings.STAGE_TITLE, "Управление на база данни с българска народна музика" },

                { FolkloreScene.TAB_INPUT, "Въвеждане" },
                { FolkloreScene.TAB_OUTPUT, "Справка" },

                { FolkloreInputTabControls.COL_ALBUM, "Албум" },
                { FolkloreInputTabControls.COL_ALBUM_TRACK_ORDER, "Номер в албума" },
                { FolkloreInputTabControls.COL_TITLE, "Заглавие" },
                { FolkloreInputTabControls.COL_PERFORMER, "Изпълнител" },
                { FolkloreInputTabControls.COL_ACCOMPANIMENT_PERFORMER, "Съпровод" },
                { FolkloreInputTabControls.COL_AUTHOR, "Автор" },
                { FolkloreInputTabControls.COL_ARRANGEMENT_AUTHOR, "Обработка" },
                { FolkloreInputTabControls.COL_CONDUCTOR, "Диригент" },
                { FolkloreInputTabControls.COL_SOLOIST, "Солист" },
                { FolkloreInputTabControls.COL_DURATION, "Времетраене" },
                { FolkloreInputTabControls.COL_SOURCE, "Източник" },
                { FolkloreInputTabControls.COL_ETHNOGRAPHIC_REGION, "Област" },
                { FolkloreInputTabControls.COL_FILE, "Файл" },
                { FolkloreInputTabControls.COL_NOTE, "Забележка" },

                { FolkloreInputTabControls.BTN_ADD, "Добавяне" },
                { FolkloreInputTabControls.BTN_REMOVE, "Премахване" },
                { FolkloreInputTabControls.BTN_COPY, "Копиране" },
                { FolkloreInputTabControls.BTN_CLEAR, "Изчистване" },
                { FolkloreInputTabControls.BTN_LOAD_ALBUM_TRACKS, "Зареждане на файлове" },
                };
    }

}
