package org.yankov.mso.application;

import org.yankov.mso.application.FolkloreApplicationSettings;
import org.yankov.mso.application.ui.ApplicationConsoleLogHandler;
import org.yankov.mso.application.ui.FolkloreInputTabControls;
import org.yankov.mso.application.ui.FolkloreScene;
import org.yankov.mso.application.utils.FileUtils;
import org.yankov.mso.database.folklore.FolkloreEntityCollectionFactory;

import java.util.ListResourceBundle;

public class FolkloreResources extends ListResourceBundle {

    @Override
    protected Object[][] getContents() {
        return new Object[][] {
                { FolkloreEntityCollectionFactory.REGION_TRAKIA, "" },
                { FolkloreEntityCollectionFactory.REGION_STRANDZHA, "" },
                { FolkloreEntityCollectionFactory.REGION_RODOPI, "" },
                { FolkloreEntityCollectionFactory.REGION_PIRIN, "" },
                { FolkloreEntityCollectionFactory.REGION_SHOPSKI, "" },
                { FolkloreEntityCollectionFactory.REGION_MIZIA, "" },
                { FolkloreEntityCollectionFactory.REGION_DOBRUDZHA, "" },
                { FolkloreEntityCollectionFactory.REGION_MACEDONIA, "" },
                { FolkloreEntityCollectionFactory.REGION_AUTHORITY, "" },

                { FolkloreEntityCollectionFactory.INSTRUMENT_KAVAL, "" },
                { FolkloreEntityCollectionFactory.INSTRUMENT_GAIDA, "" },
                { FolkloreEntityCollectionFactory.INSTRUMENT_GADULKA, "" },
                { FolkloreEntityCollectionFactory.INSTRUMENT_TAMBURA, "" },
                { FolkloreEntityCollectionFactory.INSTRUMENT_TAPAN, "" },

                { FolkloreEntityCollectionFactory.SOURCE_TYPE_TAPE, "" },
                { FolkloreEntityCollectionFactory.SOURCE_TYPE_CARTRIDGE, "" },
                { FolkloreEntityCollectionFactory.SOURCE_TYPE_GRAMOPHONE_RECORD, "" },
                { FolkloreEntityCollectionFactory.SOURCE_TYPE_CD, "" },
                { FolkloreEntityCollectionFactory.SOURCE_TYPE_VIDEO, "" },
                { FolkloreEntityCollectionFactory.SOURCE_TYPE_INTERNET, "" },

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
                { FolkloreInputTabControls.SELECT_AUDIO_FILES, "" },
                { FolkloreInputTabControls.FLAC_FILTER_NAME, "" },
                { FolkloreInputTabControls.FLAC_FILTER_EXT, "" },

                { FileUtils.FILE_NOT_FOUND, "" },
                { FileUtils.CANNOT_DETECT_AUDIO_FILE_DURATION, "" },

                { ApplicationConsoleLogHandler.ERROR, "" },
                { ApplicationConsoleLogHandler.WARNING, "" },
                { ApplicationConsoleLogHandler.INFO, "" },
                };
    }

}
