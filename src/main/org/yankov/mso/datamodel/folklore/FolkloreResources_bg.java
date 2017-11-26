package org.yankov.mso.datamodel.folklore;

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
                { FolkloreResources.SOURCE_TYPE_GRAMOPHONE_PLATE, "Грамофонна плоча" },
                { FolkloreResources.SOURCE_TYPE_CD, "CD" },
                { FolkloreResources.SOURCE_TYPE_VIDEO, "Видео" },
                { FolkloreResources.SOURCE_TYPE_INTERNET, "Internet" },
        };
    }

}
