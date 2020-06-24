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

public class FolkloreResources extends ListResourceBundle {

    @Override
    protected Object[][] getContents() {
        return new Object[][]{
            {FolkloreEntityCollectionFactory.REGION_TRAKIA, ""},
            {FolkloreEntityCollectionFactory.REGION_STRANDZHA, ""},
            {FolkloreEntityCollectionFactory.REGION_RODOPI, ""},
            {FolkloreEntityCollectionFactory.REGION_PIRIN, ""},
            {FolkloreEntityCollectionFactory.REGION_SHOPSKI, ""},
            {FolkloreEntityCollectionFactory.REGION_MIZIA, ""},
            {FolkloreEntityCollectionFactory.REGION_DOBRUDZHA, ""},
            {FolkloreEntityCollectionFactory.REGION_MACEDONIA, ""},
            {FolkloreEntityCollectionFactory.REGION_AUTHORITY, ""},

            {FolkloreEntityCollectionFactory.INSTRUMENT_KAVAL, ""},
            {FolkloreEntityCollectionFactory.INSTRUMENT_GAIDA, ""},
            {FolkloreEntityCollectionFactory.INSTRUMENT_GADULKA, ""},
            {FolkloreEntityCollectionFactory.INSTRUMENT_TAMBURA, ""},
            {FolkloreEntityCollectionFactory.INSTRUMENT_TAPAN, ""},

            {FolkloreEntityCollectionFactory.SOURCE_TYPE_TAPE, ""},
            {FolkloreEntityCollectionFactory.SOURCE_TYPE_CARTRIDGE, ""},
            {FolkloreEntityCollectionFactory.SOURCE_TYPE_GRAMOPHONE_RECORD, ""},
            {FolkloreEntityCollectionFactory.SOURCE_TYPE_CD, ""},
            {FolkloreEntityCollectionFactory.SOURCE_TYPE_VIDEO, ""},
            {FolkloreEntityCollectionFactory.SOURCE_TYPE_INTERNET, ""},

            {FolkloreApplicationSettings.STAGE_TITLE, ""},

            {FolkloreMainForm.TAB_INPUT, ""},
            {FolkloreMainForm.TAB_OUTPUT, ""},

            {FolklorePieceTable.COL_TITLE, ""},
            {FolklorePieceTable.COL_PERFORMER, ""},
            {FolklorePieceTable.COL_ACCOMPANIMENT_PERFORMER, ""},
            {FolklorePieceTable.COL_AUTHOR, ""},
            {FolklorePieceTable.COL_ARRANGEMENT_AUTHOR, ""},
            {FolklorePieceTable.COL_CONDUCTOR, ""},
            {FolklorePieceTable.COL_SOLOIST, ""},
            {FolklorePieceTable.COL_DURATION, ""},
            {FolklorePieceTable.COL_SOURCE, ""},
            {FolklorePieceTable.COL_ETHNOGRAPHIC_REGION, ""},
            {FolklorePieceTable.COL_FILE, ""},
            {FolklorePieceTable.COL_NOTE, ""},

            {Buttons.BTN_ADD, ""},
            {Buttons.BTN_REMOVE, ""},
            {Buttons.BTN_CLONE, ""},
            {Buttons.BTN_COPY_PROPERTIES, ""},
            {Buttons.BTN_APPLY_PROPERTIES, ""},
            {Buttons.BTN_IMPORT_FROM_CLIPBOARD, ""},
            {Buttons.BTN_CLEAR, ""},
            {Buttons.BTN_LOAD_ALBUM_TRACKS, ""},
            {Buttons.BTN_EDIT_PROPERTIES, ""},
            {Buttons.BTN_PLAYER_RUN, ""},
            {Buttons.BTN_PLAYER_STOP, ""},
            {Buttons.BTN_UPLOAD, ""},
            {Buttons.BTN_UPDATE, ""},
            {Buttons.BTN_EXPORT, ""},
            {Buttons.UPLOAD_STARTED, ""},
            {Buttons.UPLOAD_COMPLETED, ""},
            {Buttons.UNABLE_WRITE_FILE, ""},
            {Buttons.ERROR_UPDATE_DATA_MODEL, ""},
            {Buttons.EXPORT_STARTED, ""},
            {Buttons.EXPORT_COMPLETED, ""},

            {FxUtils.SELECT_AUDIO_FILES, ""},
            {FxUtils.SELECT_EXPORT_DIRECTORY, ""},
            {FxUtils.FLAC_FILTER_NAME, ""},
            {FxUtils.FLAC_FILTER_EXT, ""},
            {FxUtils.CONFIRMATION, ""},
            {FxUtils.OVERWRITE_RECORDS_IN_DATABASE, ""},
            {FxUtils.CLOSE_APPLICATION, ""},
            {FxUtils.ARE_YOU_SURE, ""},
            {FxUtils.YES, ""},
            {FxUtils.NO, ""},

            {FlacProcessor.FILE_NOT_FOUND, ""},
            {FlacProcessor.CANNOT_DETECT_AUDIO_FILE_DURATION, ""},
            {FlacProcessor.FLAC_ERROR, ""},
            {FlacProcessor.UNABLE_READ_FILE, ""},
            {FlacProcessor.FILE_LOADED, ""},

            {FlacPlayer.FLAC_PLAY_ERROR, ""},

            {ApplicationConsoleLogHandler.ERROR, ""},
            {ApplicationConsoleLogHandler.WARNING, ""},
            {ApplicationConsoleLogHandler.INFO, ""},

            {FolklorePieceEditor.STAGE_TITLE, ""},
            {FolklorePieceEditor.BTN_OK, ""},
            {FolklorePieceEditor.BTN_CANCEL, ""},

            {FolkloreInputArtifactsTab.ARTIFACT_TYPE, ""},
            {FolkloreInputArtifactsTab.SOURCE, ""},
            {FolkloreInputArtifactsTab.INSTRUMENT, ""},
            {FolkloreInputArtifactsTab.ARTIST, ""},
            {FolkloreInputArtifactsTab.ETHNOGRAPHIC_REGION, ""},

            {ArtifactInputControls.EXISTING_ARTIFACTS_LABEL, ""},
            {ArtifactInputControls.BTN_ADD_ARTIFACT, ""},
            {ArtifactInputControls.BTN_SAVE_ARTIFACT, ""},
            {ArtifactInputControls.ARTIFACT_EXISTS, ""},
            {ArtifactInputControls.NO_SELECTED_ARTIFACT, ""},
            {ArtifactInputControls.ARTIFACT_ADDED, ""},
            {ArtifactInputControls.ARTIFACT_SAVED, ""},

            {SourceInputControls.SIGNATURE, ""},
            {SourceInputControls.SIGNATURE_UNDEFINED, ""},

            {InstrumentInputControls.INSTRUMENT, ""},
            {InstrumentInputControls.INSTRUMENT_NAME_UNDEFINED, ""},

            {ArtistInputControls.NAME, ""},
            {ArtistInputControls.NOTE, ""},
            {ArtistInputControls.MISSIONS, ""},
            {ArtistInputControls.SINGER, ""},
            {ArtistInputControls.INSTRUMENT_PLAYER, ""},
            {ArtistInputControls.COMPOSER, ""},
            {ArtistInputControls.CONDUCTOR, ""},
            {ArtistInputControls.ORCHESTRA, ""},
            {ArtistInputControls.CHOIR, ""},
            {ArtistInputControls.ENSEMBLE, ""},
            {ArtistInputControls.CHAMBER_GROUP, ""},
            {ArtistInputControls.ARTIST_NAME_UNDEFINED, ""},
            {ArtistInputControls.ARTIST_INSTRUMENT_UNDEFINED, ""},
            {ArtistInputControls.NO_ARTIST_MISSION_SELECTED, ""},

            {EthnographicRegionInputControls.NAME, ""},
            {EthnographicRegionInputControls.NAME_UNDEFINED, ""},

            {FolkloreSearchTab.VARIABLE, ""},
            {FolkloreSearchTab.OPERATOR, ""},
            {FolkloreSearchTab.VALUE, ""},
            {FolkloreSearchTab.BTN_SEARCH, ""},
            {FolkloreSearchTab.TOTAL_ITEMS_FOUND, ""},
            {FolkloreSearchTab.PLAY_NEXT, ""},
            {FolkloreSearchTab.PLAY_RANDOM, ""},
            {FolkloreSearchTab.FILTER, ""},

            {FolkloreSearchFactory.VAR_LABEL_TITLE, ""},
            {FolkloreSearchFactory.VAR_LABEL_PERFORMER, ""},
            {FolkloreSearchFactory.VAR_LABEL_ACCOMPANIMENT_PERFORMER, ""},
            {FolkloreSearchFactory.VAR_LABEL_ARRANGEMENT_AUTHOR, ""},
            {FolkloreSearchFactory.VAR_LABEL_CONDUCTOR, ""},
            {FolkloreSearchFactory.VAR_LABEL_INSTRUMENT_PERFORMANCE, ""},
            {FolkloreSearchFactory.VAR_LABEL_INSTRUMENT_ACCOMPANIMENT, ""},
            {FolkloreSearchFactory.VAR_LABEL_SOLOIST, ""},
            {FolkloreSearchFactory.VAR_LABEL_AUTHOR, ""},
            {FolkloreSearchFactory.VAR_LABEL_ETHNOGRAPHIC_REGION, ""},
            {FolkloreSearchFactory.VAR_LABEL_PIECE_NOTE, ""},
            {FolkloreSearchFactory.VAR_LABEL_SOURCE_TYPE, ""},
            {FolkloreSearchFactory.VAR_LABEL_SOURCE_SIGNATURE, ""},
            {FolkloreSearchFactory.OPERATOR_LABEL_EQUALS, ""},
            {FolkloreSearchFactory.OPERATOR_LABEL_NOT_EQUALS, ""},
            {FolkloreSearchFactory.OPERATOR_LABEL_CONTAINS, ""},
            {FolkloreSearchFactory.OPERATOR_LABEL_NOT_CONTAINS, ""},
            {FolkloreSearchFactory.OPERATOR_LABEL_STARTS, ""},
            {FolkloreSearchFactory.OPERATOR_LABEL_ENDS, ""},

            {FolkloreComboBoxFactory.SOURCE_TYPE, ""},
            {FolkloreComboBoxFactory.INSTRUMENT, ""},
        };
    }

}
