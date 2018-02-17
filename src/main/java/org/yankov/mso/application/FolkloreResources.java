package org.yankov.mso.application;

import org.yankov.mso.application.ui.ApplicationConsoleLogHandler;
import org.yankov.mso.application.ui.FolkloreMainForm;
import org.yankov.mso.datamodel.FolkloreSearchFactory;
import org.yankov.mso.datamodel.PieceProperties;
import org.yankov.mso.application.ui.tabs.FolklorePieceEditor;
import org.yankov.mso.application.ui.tabs.*;
import org.yankov.mso.application.utils.FlacProcessor;
import org.yankov.mso.database.FolkloreEntityCollectionFactory;

import java.util.ListResourceBundle;

public class FolkloreResources extends ListResourceBundle {

    @Override
    protected Object[][] getContents() {
        return new Object[][] {
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

                {FolklorePieceTable.COL_ALBUM, ""},
                {FolklorePieceTable.COL_ALBUM_TRACK_ORDER, ""},
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

                {FolkloreInputButtons.BTN_ADD, ""},
                {FolkloreInputButtons.BTN_REMOVE, ""},
                {FolkloreInputButtons.BTN_COPY, ""},
                {FolkloreInputButtons.BTN_CLEAR, ""},
                {FolkloreInputButtons.BTN_LOAD_ALBUM_TRACKS, ""},
                {FolkloreInputButtons.SELECT_AUDIO_FILES, ""},
                {FolkloreInputButtons.FLAC_FILTER_NAME, ""},
                {FolkloreInputButtons.FLAC_FILTER_EXT, ""},
                {FolkloreInputButtons.BTN_EDIT, ""},
                {FolkloreInputButtons.BTN_PLAYER_RUN, ""},
                {FolkloreInputButtons.BTN_PLAYER_STOP, ""},
                {FolkloreInputButtons.BTN_UPLOAD, ""},

                {FlacProcessor.FILE_NOT_FOUND, ""},
                {FlacProcessor.CANNOT_DETECT_AUDIO_FILE_DURATION, ""},
                {FlacProcessor.FLAC_ERROR, ""},

                {ApplicationConsoleLogHandler.ERROR, ""},
                {ApplicationConsoleLogHandler.WARNING, ""},
                {ApplicationConsoleLogHandler.INFO, ""},

                {FolklorePieceEditor.STAGE_TITLE, ""},
                {FolklorePieceEditor.BTN_OK, ""},
                {FolklorePieceEditor.BTN_CANCEL, ""},

                {PieceProperties.UNDEFINED_ALBUM, ""},

                {FolkloreInputArtifactsTab.ARTIFACT_TYPE, ""},
                {FolkloreInputArtifactsTab.SOURCE, ""},
                {FolkloreInputArtifactsTab.INSTRUMENT, ""},
                {FolkloreInputArtifactsTab.ARTIST, ""},
                {FolkloreInputArtifactsTab.ALBUM, ""},
                {FolkloreInputArtifactsTab.ETHNOGRAPHIC_REGION, ""},

                {ArtifactInputControls.EXISTING_ARTIFACTS_LABEL, ""},
                {ArtifactInputControls.BTN_ADD_ARTIFACT, ""},
                {ArtifactInputControls.BTN_SAVE_ARTIFACT, ""},
                {ArtifactInputControls.ARTIFACT_EXISTS, ""},
                {ArtifactInputControls.NO_SELECTED_ARTIFACT, ""},
                {ArtifactInputControls.ARTIFACT_ADDED, ""},
                {ArtifactInputControls.ARTIFACT_SAVED, ""},

                {SourceInputControls.SOURCE_TYPE, ""},
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
                {ArtistInputControls.INSTRUMENT, ""},

                {AlbumInputControls.TITLE, ""},
                {AlbumInputControls.PRODUCTION_SIGNATURE, ""},
                {AlbumInputControls.COLLECTION_SIGNATURE, ""},
                {AlbumInputControls.NOTE, ""},
                {AlbumInputControls.TITLE_UNDEFINED, ""},
                {AlbumInputControls.COLLECTION_SIGNATURE_UNDEFINED, ""},

                {EthnographicRegionInputControls.NAME, ""},
                {EthnographicRegionInputControls.NAME_UNDEFINED, ""},

                {FolklorePieceUploader.UNABLE_READ_FILE, ""},
                {FolklorePieceUploader.FILE_LOADED, ""},
                {FolklorePieceUploader.PIECES_UPLOAD_COMPLETED, ""},

                {FolkloreSearchTab.VARIABLE, ""},
                {FolkloreSearchTab.OPERATOR, ""},
                {FolkloreSearchTab.VALUE, ""},
                {FolkloreSearchTab.BTN_SEARCH, ""},

                {FolkloreSearchFactory.VAR_TITLE, ""},
                {FolkloreSearchFactory.VAR_PERFORMER, ""},
                {FolkloreSearchFactory.VAR_ACCOMPANIMENT_PERFORMER, ""},
                {FolkloreSearchFactory.VAR_ARRANGEMENT_AUTHOR, ""},
                {FolkloreSearchFactory.VAR_CONDUCTOR, ""},
                {FolkloreSearchFactory.VAR_INSTRUMENT_PERFORMANCE, ""},
                {FolkloreSearchFactory.VAR_INSTRUMENT_ACCOMPANIMENT, ""},
                {FolkloreSearchFactory.VAR_SOLOIST, ""},
                {FolkloreSearchFactory.VAR_AUTHOR, ""},
                {FolkloreSearchFactory.VAR_ETHNOGRAPHIC_REGION, ""},
                {FolkloreSearchFactory.VAR_ALBUM_TITLE, ""},
                {FolkloreSearchFactory.VAR_ALBUM_PRODUCTION_SIGNATURE, ""},
                {FolkloreSearchFactory.VAR_ALBUM_COLLECTION_SIGNATURE, ""},
                {FolkloreSearchFactory.VAR_SOURCE_TYPE, ""},
                {FolkloreSearchFactory.VAR_SOURCE_SIGNATURE, ""},
                {FolkloreSearchFactory.OPERATOR_EQUALS, ""},
                {FolkloreSearchFactory.OPERATOR_NOT_EQUALS, ""},
                {FolkloreSearchFactory.OPERATOR_CONTAINS, ""},
                {FolkloreSearchFactory.OPERATOR_NOT_CONTAINS, ""},
                {FolkloreSearchFactory.OPERATOR_STARTS, ""},
                {FolkloreSearchFactory.OPERATOR_ENDS, ""},
                };
    }

}
