package org.yankov.mso.application;

import org.yankov.mso.application.ui.ApplicationConsoleLogHandler;
import org.yankov.mso.application.ui.FolkloreMainForm;
import org.yankov.mso.application.ui.datamodel.PieceProperties;
import org.yankov.mso.application.ui.edit.FolklorePieceEditor;
import org.yankov.mso.application.ui.input.*;
import org.yankov.mso.application.utils.FileUtils;
import org.yankov.mso.database.folklore.FolkloreEntityCollectionFactory;

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

                {FolkloreInputTable.COL_ALBUM, ""},
                {FolkloreInputTable.COL_ALBUM_TRACK_ORDER, ""},
                {FolkloreInputTable.COL_TITLE, ""},
                {FolkloreInputTable.COL_PERFORMER, ""},
                {FolkloreInputTable.COL_ACCOMPANIMENT_PERFORMER, ""},
                {FolkloreInputTable.COL_AUTHOR, ""},
                {FolkloreInputTable.COL_ARRANGEMENT_AUTHOR, ""},
                {FolkloreInputTable.COL_CONDUCTOR, ""},
                {FolkloreInputTable.COL_SOLOIST, ""},
                {FolkloreInputTable.COL_DURATION, ""},
                {FolkloreInputTable.COL_SOURCE, ""},
                {FolkloreInputTable.COL_ETHNOGRAPHIC_REGION, ""},
                {FolkloreInputTable.COL_FILE, ""},
                {FolkloreInputTable.COL_NOTE, ""},

                {FolkloreInputButtons.BTN_ADD, ""},
                {FolkloreInputButtons.BTN_REMOVE, ""},
                {FolkloreInputButtons.BTN_COPY, ""},
                {FolkloreInputButtons.BTN_CLEAR, ""},
                {FolkloreInputButtons.BTN_LOAD_ALBUM_TRACKS, ""},
                {FolkloreInputButtons.SELECT_AUDIO_FILES, ""},
                {FolkloreInputButtons.FLAC_FILTER_NAME, ""},
                {FolkloreInputButtons.FLAC_FILTER_EXT, ""},
                {FolkloreInputButtons.BTN_EDIT, ""},

                {FileUtils.FILE_NOT_FOUND, ""},
                {FileUtils.CANNOT_DETECT_AUDIO_FILE_DURATION, ""},

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
                };
    }

}
