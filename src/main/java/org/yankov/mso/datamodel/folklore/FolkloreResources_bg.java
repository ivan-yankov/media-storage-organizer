package org.yankov.mso.datamodel.folklore;

import org.yankov.mso.application.FolkloreApplicationSettings;
import org.yankov.mso.application.ui.FolkloreInputTabControls;
import org.yankov.mso.application.ui.FolkloreScene;
import org.yankov.mso.database.folklore.FolkloreEntityCollectionFactory;

import java.util.ListResourceBundle;

public class FolkloreResources_bg extends ListResourceBundle {

    @Override
    protected Object[][] getContents() {
        return new Object[][] {
                { FolkloreEntityCollectionFactory.REGION_TRAKIA, "Тракийска" },
                { FolkloreEntityCollectionFactory.REGION_STRANDZHA, "Странджанска" },
                { FolkloreEntityCollectionFactory.REGION_RODOPI, "Родопска" },
                { FolkloreEntityCollectionFactory.REGION_PIRIN, "Пиринска" },
                { FolkloreEntityCollectionFactory.REGION_SHOPSKI, "Шопска" },
                { FolkloreEntityCollectionFactory.REGION_MIZIA, "Северняшка" },
                { FolkloreEntityCollectionFactory.REGION_DOBRUDZHA, "Добруджанска" },
                { FolkloreEntityCollectionFactory.REGION_MACEDONIA, "Вардарска Македония" },
                { FolkloreEntityCollectionFactory.REGION_AUTHORITY, "Авторска" },

                { FolkloreEntityCollectionFactory.INSTRUMENT_KAVAL, "Кавал" },
                { FolkloreEntityCollectionFactory.INSTRUMENT_GAIDA, "Гайда" },
                { FolkloreEntityCollectionFactory.INSTRUMENT_GADULKA, "Гъдулка" },
                { FolkloreEntityCollectionFactory.INSTRUMENT_TAMBURA, "Тамбура" },
                { FolkloreEntityCollectionFactory.INSTRUMENT_TAPAN, "Тъпан" },

                { FolkloreEntityCollectionFactory.SOURCE_TYPE_TAPE, "Лента" },
                { FolkloreEntityCollectionFactory.SOURCE_TYPE_CARTRIDGE, "Касета" },
                { FolkloreEntityCollectionFactory.SOURCE_TYPE_GRAMOPHONE_RECORD, "Грамофонна плоча" },
                { FolkloreEntityCollectionFactory.SOURCE_TYPE_CD, "CD" },
                { FolkloreEntityCollectionFactory.SOURCE_TYPE_VIDEO, "Видео" },
                { FolkloreEntityCollectionFactory.SOURCE_TYPE_INTERNET, "Internet" },

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
