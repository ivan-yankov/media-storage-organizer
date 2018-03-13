package org.yankov.mso.application.ui.controls;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.util.StringConverter;
import org.yankov.mso.application.ApplicationContext;
import org.yankov.mso.application.ui.converters.*;
import org.yankov.mso.application.ui.tabs.FolklorePieceTable;
import org.yankov.mso.application.ui.tabs.FolkloreSearchTab;
import org.yankov.mso.datamodel.*;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class FolkloreComboBoxFactory {

    private static final String CLASS_NAME = FolkloreComboBoxFactory.class.getName();

    public static final String SOURCE_TYPE = CLASS_NAME + "-source-type";
    public static final String INSTRUMENT = CLASS_NAME + "-instrument";

    private static final ResourceBundle resourceBundle = ApplicationContext.getInstance().getFolkloreResourceBundle();

    public static LabeledComboBox<SourceType> createSourceTypeInput() {
        ObservableList<SourceType> sourceTypes = FXCollections.observableArrayList();
        sourceTypes.addAll(ApplicationContext.getInstance().getFolkloreEntityCollections().getSourceTypes());
        SourceType defaultSourceType = sourceTypes.get(0);
        LabeledComboBox<SourceType> cb = new LabeledComboBox<>(new SourceTypeStringConverter(), false, true);
        cb.setLabelText(resourceBundle.getString(SOURCE_TYPE));
        cb.setItems(sourceTypes);
        cb.setValue(defaultSourceType);
        return cb;
    }

    public static LabeledComboBox<Album> createAlbum(FolklorePieceProperties piece) {
        StringConverter<Album> albumStringConverter = new AlbumStringConverter(
                ApplicationContext.getInstance().getFolkloreEntityCollections().getAlbums(), true);
        LabeledComboBox<Album> cb = new LabeledComboBox<>(albumStringConverter, false, true);
        cb.setLabelText(resourceBundle.getString(FolklorePieceTable.COL_ALBUM));
        cb.setItems(collectAlbums());
        cb.setValue(piece.getAlbum());
        cb.setNewValueConsumer(piece::setAlbum);
        cb.setNullable(true);
        return cb;
    }

    public static LabeledComboBox<Artist> createPerformer(FolklorePieceProperties piece) {
        LabeledComboBox<Artist> cb = new LabeledComboBox<>(new ArtistStringConverter(), false, true);
        cb.setLabelText(resourceBundle.getString(FolklorePieceTable.COL_PERFORMER));
        cb.setItems(collectArtists(ArtistMission.SINGER, ArtistMission.ORCHESTRA, ArtistMission.INSTRUMENT_PLAYER,
                                   ArtistMission.ENSEMBLE, ArtistMission.CHOIR, ArtistMission.CHAMBER_GROUP));
        cb.setValue(piece.getPerformer());
        cb.setNewValueConsumer(piece::setPerformer);
        cb.setNullable(true);
        return cb;
    }

    public static LabeledComboBox<Artist> createAccompanimentPerformer(FolklorePieceProperties piece) {
        LabeledComboBox<Artist> cb = new LabeledComboBox<>(new ArtistStringConverter(), false, true);
        cb.setLabelText(resourceBundle.getString(FolklorePieceTable.COL_ACCOMPANIMENT_PERFORMER));
        cb.setItems(collectArtists(ArtistMission.ORCHESTRA, ArtistMission.INSTRUMENT_PLAYER, ArtistMission.ENSEMBLE,
                                   ArtistMission.CHAMBER_GROUP));
        cb.setValue(piece.getAccompanimentPerformer());
        cb.setNewValueConsumer(piece::setAccompanimentPerformer);
        cb.setNullable(true);
        return cb;
    }

    public static LabeledComboBox<Artist> createArrangementAuthor(FolklorePieceProperties piece) {
        LabeledComboBox<Artist> cb = new LabeledComboBox<>(new ArtistStringConverter(), false, true);
        cb.setLabelText(resourceBundle.getString(FolklorePieceTable.COL_ARRANGEMENT_AUTHOR));
        cb.setItems(collectArtists(ArtistMission.COMPOSER));
        cb.setValue(piece.getArrangementAuthor());
        cb.setNewValueConsumer(piece::setArrangementAuthor);
        cb.setNullable(true);
        return cb;
    }

    public static LabeledComboBox<Artist> createConductor(FolklorePieceProperties piece) {
        LabeledComboBox<Artist> cb = new LabeledComboBox<>(new ArtistStringConverter(), false, true);
        cb.setLabelText(resourceBundle.getString(FolklorePieceTable.COL_CONDUCTOR));
        cb.setItems(collectArtists(ArtistMission.CONDUCTOR));
        cb.setValue(piece.getConductor());
        cb.setNewValueConsumer(piece::setConductor);
        cb.setNullable(true);
        return cb;
    }

    public static LabeledComboBox<Artist> createAuthor(FolklorePieceProperties piece) {
        LabeledComboBox<Artist> cb = new LabeledComboBox<>(new ArtistStringConverter(), false, true);
        cb.setLabelText(resourceBundle.getString(FolklorePieceTable.COL_AUTHOR));
        cb.setItems(collectArtists(ArtistMission.COMPOSER));
        cb.setValue(piece.getAuthor());
        cb.setNewValueConsumer(piece::setAuthor);
        cb.setNullable(true);
        return cb;
    }

    public static LabeledComboBox<Artist> createSoloist(FolklorePieceProperties piece) {
        LabeledComboBox<Artist> cb = new LabeledComboBox<>(new ArtistStringConverter(), false, true);
        cb.setLabelText(resourceBundle.getString(FolklorePieceTable.COL_SOLOIST));
        cb.setItems(collectArtists(ArtistMission.SINGER, ArtistMission.INSTRUMENT_PLAYER));
        cb.setValue(piece.getSoloist());
        cb.setNewValueConsumer(piece::setSoloist);
        cb.setNullable(true);
        return cb;
    }

    public static LabeledComboBox<EthnographicRegion> createEthnographicRegion(FolklorePieceProperties piece) {
        LabeledComboBox<EthnographicRegion> cb = new LabeledComboBox<>(new EthnographicRegionStringConverter(), false,
                                                                       true);
        cb.setLabelText(resourceBundle.getString(FolklorePieceTable.COL_ETHNOGRAPHIC_REGION));
        cb.setItems(collectEthnographicRegions());
        cb.setValue(piece.getEthnographicRegion());
        cb.setNewValueConsumer(piece::setEthnographicRegion);
        cb.setNullable(true);
        return cb;
    }

    public static LabeledComboBox<Source> createSource(FolklorePieceProperties piece) {
        LabeledComboBox<Source> cb = new LabeledComboBox<>(new SourceStringConverter(), false, true);
        cb.setLabelText(resourceBundle.getString(FolklorePieceTable.COL_SOURCE));
        cb.setItems(collectSources());
        cb.setValue(piece.getSource());
        cb.setNewValueConsumer(piece::setSource);
        cb.setNullable(true);
        return cb;
    }

    public static LabeledComboBox<Variable<FolklorePiece>> createSearchVariable() {
        LabeledComboBox<Variable<FolklorePiece>> cb = new LabeledComboBox<>(new VariableStringConverter<>(), false,
                                                                            false);
        cb.setLabelText(resourceBundle.getString(FolkloreSearchTab.VARIABLE));
        cb.setItems(FolkloreSearchFactory.createFolkloreVariables());
        cb.setValue(FolkloreSearchFactory.VAR_TITLE);
        return cb;
    }

    public static LabeledComboBox<Operator> createSearchOperators() {
        LabeledComboBox<Operator> cb = new LabeledComboBox<>(new OperatorStringConverter(), false, false);
        cb.setLabelText(resourceBundle.getString(FolkloreSearchTab.OPERATOR));
        cb.setItems(FolkloreSearchFactory.createOperators());
        cb.setValue(FolkloreSearchFactory.OPERATOR_EQUALS);
        return cb;
    }

    public static LabeledComboBox<Instrument> createInstrument() {
        ObservableList<Instrument> instruments = FXCollections
                .observableArrayList(ApplicationContext.getInstance().getFolkloreEntityCollections().getInstruments());
        LabeledComboBox<Instrument> cb = new LabeledComboBox<>(new InstrumentStringConverter(), false, true);
        cb.setLabelText(resourceBundle.getString(INSTRUMENT));
        cb.setItems(instruments);
        cb.setNullable(true);
        cb.setValue(null);
        return cb;
    }

    private static ObservableList<Album> collectAlbums() {
        List<Album> list = new ArrayList<>(ApplicationContext.getInstance().getFolkloreEntityCollections().getAlbums());
        return FXCollections.observableList(list);
    }

    private static ObservableList<Artist> collectArtists(ArtistMission... missions) {
        Stream<Artist> artists = ApplicationContext.getInstance().getFolkloreEntityCollections().getArtists().stream();
        return artists.filter(artist -> hasMission(artist, Arrays.asList(missions)))
                      .collect(Collectors.collectingAndThen(Collectors.toList(), FXCollections::observableList));
    }

    private static boolean hasMission(Artist artist, Collection<ArtistMission> missions) {
        for (ArtistMission mission : missions) {
            if (artist.getMissions().contains(mission)) {
                return true;
            }
        }
        return false;
    }

    private static ObservableList<EthnographicRegion> collectEthnographicRegions() {
        List<EthnographicRegion> list = new ArrayList<>(
                ApplicationContext.getInstance().getFolkloreEntityCollections().getEthnographicRegions());
        return FXCollections.observableList(list);
    }

    private static ObservableList<Source> collectSources() {
        List<Source> list = new ArrayList<>(
                ApplicationContext.getInstance().getFolkloreEntityCollections().getSources());
        return FXCollections.observableList(list);
    }

}
