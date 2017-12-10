package org.yankov.mso.datamodel.folklore;

import org.yankov.mso.application.folklore.FolkloreApplicationSettings;
import org.yankov.mso.application.folklore.FolkloreInputTabControls;
import org.yankov.mso.application.folklore.FolkloreScene;

import java.util.ListResourceBundle;

public class FolkloreResources extends ListResourceBundle {

    public static final String REGION_TRAKIA = "region-trakia";
    public static final String REGION_STRANDZHA = "region-strandzha";
    public static final String REGION_RODOPI = "region-rodopi";
    public static final String REGION_PIRIN = "region-pirin";
    public static final String REGION_SHOPSKI = "region-shopski";
    public static final String REGION_MIZIA = "region-mizia";
    public static final String REGION_DOBRUDZHA = "region-dobrudzha";
    public static final String REGION_MACEDONIA = "region-vardarska-makedonia";
    public static final String REGION_AUTHORITY = "region-authority";

    public static final String INSTRUMENT_KAVAL = "instrument-kaval";
    public static final String INSTRUMENT_GAIDA = "instrument-gaida";
    public static final String INSTRUMENT_GADULKA = "instrument-gadulka";
    public static final String INSTRUMENT_TAMBURA = "instrument-tambura";
    public static final String INSTRUMENT_TAPAN = "instrument-tapan";

    public static final String SOURCE_TYPE_TAPE = "source-type-tape";
    public static final String SOURCE_TYPE_CARTRIDGE = "source-type-cartridge";
    public static final String SOURCE_TYPE_GRAMOPHONE_RECORD = "source-type-gramophone-record";
    public static final String SOURCE_TYPE_CD = "source-type-cd";
    public static final String SOURCE_TYPE_VIDEO = "source-type-video";
    public static final String SOURCE_TYPE_INTERNET = "source-type-internet";

    @Override
    protected Object[][] getContents() {
        return new Object[][] {
                { FolkloreResources.REGION_TRAKIA, "" },
                { FolkloreResources.REGION_STRANDZHA, "" },
                { FolkloreResources.REGION_RODOPI, "" },
                { FolkloreResources.REGION_PIRIN, "" },
                { FolkloreResources.REGION_SHOPSKI, "" },
                { FolkloreResources.REGION_MIZIA, "" },
                { FolkloreResources.REGION_DOBRUDZHA, "" },
                { FolkloreResources.REGION_MACEDONIA, "" },
                { FolkloreResources.REGION_AUTHORITY, "" },

                { FolkloreResources.INSTRUMENT_KAVAL, "" },
                { FolkloreResources.INSTRUMENT_GAIDA, "" },
                { FolkloreResources.INSTRUMENT_GADULKA, "" },
                { FolkloreResources.INSTRUMENT_TAMBURA, "" },
                { FolkloreResources.INSTRUMENT_TAPAN, "" },

                { FolkloreResources.SOURCE_TYPE_TAPE, "" },
                { FolkloreResources.SOURCE_TYPE_CARTRIDGE, "" },
                { FolkloreResources.SOURCE_TYPE_GRAMOPHONE_RECORD, "" },
                { FolkloreResources.SOURCE_TYPE_CD, "" },
                { FolkloreResources.SOURCE_TYPE_VIDEO, "" },
                { FolkloreResources.SOURCE_TYPE_INTERNET, "" },

                { FolkloreApplicationSettings.STAGE_TITLE, "" },

                { FolkloreScene.TAB_INPUT, "" },
                { FolkloreScene.TAB_OUTPUT, "" },

                { FolkloreInputTabControls.COL_ALBUM, "" },
                { FolkloreInputTabControls.COL_ALBUM_TRACK_ORDER, "" },
                { FolkloreInputTabControls.COL_TITLE, "" },
                { FolkloreInputTabControls.COL_PERFORMER, "" },
                { FolkloreInputTabControls.COL_ACCOMPANIMENT_PERFORMER, "" },
                { FolkloreInputTabControls.COL_AUTHOR, "" },
                { FolkloreInputTabControls.COL_ARRANGEMENT_AUTHOR, "" },
                { FolkloreInputTabControls.COL_CONDUCTOR, "" },
                { FolkloreInputTabControls.COL_SOLOIST, "" },
                { FolkloreInputTabControls.COL_DURATION, "" },
                { FolkloreInputTabControls.COL_SOURCE, "" },
                { FolkloreInputTabControls.COL_ETHNOGRAPHIC_REGION, "" },
                { FolkloreInputTabControls.COL_FILE, "" },
                { FolkloreInputTabControls.COL_NOTE, "" },

                { FolkloreInputTabControls.BTN_ADD, "" },
                { FolkloreInputTabControls.BTN_REMOVE, "" },
                { FolkloreInputTabControls.BTN_COPY, "" },
                { FolkloreInputTabControls.BTN_CLEAR, "" },
                { FolkloreInputTabControls.BTN_LOAD_ALBUM_TRACKS, "" },
                };
    }

}
