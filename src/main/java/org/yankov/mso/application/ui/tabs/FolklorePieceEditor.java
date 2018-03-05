package org.yankov.mso.application.ui.tabs;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import org.yankov.mso.application.ApplicationContext;
import org.yankov.mso.application.Form;
import org.yankov.mso.application.UserInterfaceControls;
import org.yankov.mso.application.ui.controls.FileSelectionField;
import org.yankov.mso.application.ui.controls.LabeledComboBox;
import org.yankov.mso.application.ui.controls.LabeledTextField;
import org.yankov.mso.application.ui.converters.AlbumStringConverter;
import org.yankov.mso.application.ui.converters.ArtistStringConverter;
import org.yankov.mso.application.ui.converters.EthnographicRegionStringConverter;
import org.yankov.mso.application.ui.converters.SourceStringConverter;
import org.yankov.mso.datamodel.*;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class FolklorePieceEditor implements Form {

    private static final String CLASS_NAME = FolklorePieceEditor.class.getName();

    public static final String STAGE_TITLE = CLASS_NAME + "-stage-title";
    public static final String BTN_OK = CLASS_NAME + "-btn-ok";
    public static final String BTN_CANCEL = CLASS_NAME + "-btn-cancel";

    private static final Insets MARGINS = new Insets(25.0);
    private static final Double HORIZONTAL_SPACE = 25.0;
    private static final Double VERTICAL_SPACE = 25.0;
    private static final Double FORM_BUTTON_WIDTH = 150.0;

    private final ResourceBundle resourceBundle = ApplicationContext.getInstance().getFolkloreResourceBundle();

    private Stage stage;
    private TableView<FolklorePieceProperties> table;
    private FolklorePieceProperties piece;
    private int selectedIndex;

    public FolklorePieceEditor(TableView<FolklorePieceProperties> table, int selectedIndex) {
        this.stage = new Stage();
        this.table = table;
        this.piece = PiecePropertiesUtils.copyFolklorePieceProperties(table.getItems().get(selectedIndex));
        this.selectedIndex = selectedIndex;
    }

    @Override
    public void createControls() {
        UserInterfaceControls title = new LabeledTextField(resourceBundle.getString(FolklorePieceTable.COL_TITLE),
                                                           piece.getTitle(), piece::setTitle);
        title.layout();

        StringConverter<Album> albumStringConverter = new AlbumStringConverter(
                ApplicationContext.getInstance().getFolkloreEntityCollections().getAlbums(), true);
        UserInterfaceControls album = new LabeledComboBox<>(resourceBundle.getString(FolklorePieceTable.COL_ALBUM),
                                                            collectAlbums(), piece.getAlbum(), piece::setAlbum,
                                                            albumStringConverter, false, true);
        album.layout();

        UserInterfaceControls performer = new LabeledComboBox<>(
                resourceBundle.getString(FolklorePieceTable.COL_PERFORMER),
                collectArtists(ArtistMission.SINGER, ArtistMission.ORCHESTRA, ArtistMission.INSTRUMENT_PLAYER,
                               ArtistMission.ENSEMBLE, ArtistMission.CHOIR, ArtistMission.CHAMBER_GROUP),
                piece.getPerformer(), piece::setPerformer, new ArtistStringConverter(), false, true);
        performer.layout();

        UserInterfaceControls accompanimentPerformer = new LabeledComboBox<>(
                resourceBundle.getString(FolklorePieceTable.COL_ACCOMPANIMENT_PERFORMER),
                collectArtists(ArtistMission.ORCHESTRA, ArtistMission.INSTRUMENT_PLAYER, ArtistMission.ENSEMBLE,
                               ArtistMission.CHAMBER_GROUP), piece.getAccompanimentPerformer(),
                piece::setAccompanimentPerformer, new ArtistStringConverter(), false, true);
        accompanimentPerformer.layout();

        UserInterfaceControls arrangementAuthor = new LabeledComboBox<>(
                resourceBundle.getString(FolklorePieceTable.COL_ARRANGEMENT_AUTHOR),
                collectArtists(ArtistMission.COMPOSER), piece.getArrangementAuthor(), piece::setArrangementAuthor,
                new ArtistStringConverter(), false, true);
        arrangementAuthor.layout();

        UserInterfaceControls conductor = new LabeledComboBox<>(
                resourceBundle.getString(FolklorePieceTable.COL_CONDUCTOR), collectArtists(ArtistMission.CONDUCTOR),
                piece.getConductor(), piece::setConductor, new ArtistStringConverter(), false, true);
        conductor.layout();

        UserInterfaceControls author = new LabeledComboBox<>(resourceBundle.getString(FolklorePieceTable.COL_AUTHOR),
                                                             collectArtists(ArtistMission.COMPOSER), piece.getAuthor(),
                                                             piece::setAuthor, new ArtistStringConverter(), false,
                                                             true);
        author.layout();

        UserInterfaceControls soloist = new LabeledComboBox<>(resourceBundle.getString(FolklorePieceTable.COL_SOLOIST),
                                                              collectArtists(ArtistMission.SINGER,
                                                                             ArtistMission.INSTRUMENT_PLAYER),
                                                              piece.getSoloist(), piece::setSoloist,
                                                              new ArtistStringConverter(), false, true);
        soloist.layout();

        UserInterfaceControls ethnographicRegion = new LabeledComboBox<>(
                resourceBundle.getString(FolklorePieceTable.COL_ETHNOGRAPHIC_REGION), collectEthnographicRegions(),
                piece.getEthnographicRegion(), piece::setEthnographicRegion, new EthnographicRegionStringConverter(),
                false, true);
        ethnographicRegion.layout();

        UserInterfaceControls source = new LabeledComboBox<>(resourceBundle.getString(FolklorePieceTable.COL_SOURCE),
                                                             collectSources(), piece.getSource(), piece::setSource,
                                                             new SourceStringConverter(), false, true);
        source.layout();

        UserInterfaceControls note = new LabeledTextField(resourceBundle.getString(FolklorePieceTable.COL_NOTE),
                                                          piece.getNote(), piece::setNote);
        note.layout();

        UserInterfaceControls fileSelectionField = new FileSelectionField(
                resourceBundle.getString(FolklorePieceTable.COL_FILE), piece.getFile(), piece::setFile);
        fileSelectionField.layout();

        GridPane gridPane = new GridPane();
        gridPane.setPadding(MARGINS);
        gridPane.setAlignment(Pos.BASELINE_LEFT);
        gridPane.setHgap(HORIZONTAL_SPACE);
        gridPane.setVgap(VERTICAL_SPACE);

        gridPane.add(title.getContainer(), 1, 0, 2, 1);
        gridPane.add(album.getContainer(), 0, 0);
        gridPane.add(performer.getContainer(), 0, 1);
        gridPane.add(accompanimentPerformer.getContainer(), 1, 1);
        gridPane.add(arrangementAuthor.getContainer(), 2, 1);
        gridPane.add(conductor.getContainer(), 3, 1);
        gridPane.add(author.getContainer(), 0, 2);
        gridPane.add(soloist.getContainer(), 1, 2);
        gridPane.add(ethnographicRegion.getContainer(), 0, 3);
        gridPane.add(source.getContainer(), 1, 3);
        gridPane.add(note.getContainer(), 2, 3);
        gridPane.add(fileSelectionField.getContainer(), 3, 3);

        VBox container = new VBox();
        container.getChildren().add(gridPane);
        container.getChildren().add(createFormButtons());

        initializeStage();

        stage.setScene(new Scene(new StackPane(container)));
    }

    @Override
    public void show() {
        stage.showAndWait();
    }

    @Override
    public void close() {
        stage.close();
    }

    private void initializeStage() {
        ApplicationContext.getInstance().getApplicationSettings().getIcon().ifPresent(stage.getIcons()::add);
        stage.initOwner(ApplicationContext.getInstance().getPrimaryStage());
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setTitle(resourceBundle.getString(STAGE_TITLE) + String.format(" %d", piece.getAlbumTrackOrder()));
    }

    private HBox createFormButtons() {
        Button btnOk = new Button();
        btnOk.setText(resourceBundle.getString(BTN_OK));
        btnOk.setPrefWidth(FORM_BUTTON_WIDTH);
        btnOk.setDefaultButton(true);
        btnOk.setOnAction(event -> {
            table.getItems().set(selectedIndex, piece);
            table.getSelectionModel().select(selectedIndex);
            stage.close();
        });

        Button btnCancel = new Button();
        btnCancel.setText(resourceBundle.getString(BTN_CANCEL));
        btnCancel.setPrefWidth(FORM_BUTTON_WIDTH);
        btnCancel.setCancelButton(true);
        btnCancel.setOnAction(event -> stage.close());

        HBox formButtonsContainer = new HBox();
        formButtonsContainer.setSpacing(HORIZONTAL_SPACE);
        formButtonsContainer.setPadding(MARGINS);
        formButtonsContainer.getChildren().add(btnOk);
        formButtonsContainer.getChildren().add(btnCancel);
        formButtonsContainer.setAlignment(Pos.CENTER);

        return formButtonsContainer;
    }

    private ObservableList<Artist> collectArtists(ArtistMission... missions) {
        Stream<Artist> artists = ApplicationContext.getInstance().getFolkloreEntityCollections().getArtists().stream();
        return artists.filter(artist -> hasMission(artist, Arrays.asList(missions)))
                      .collect(Collectors.collectingAndThen(Collectors.toList(), FXCollections::observableList));
    }

    private ObservableList<EthnographicRegion> collectEthnographicRegions() {
        List<EthnographicRegion> list = new ArrayList<>(
                ApplicationContext.getInstance().getFolkloreEntityCollections().getEthnographicRegions());
        return FXCollections.observableList(list);
    }

    private ObservableList<Source> collectSources() {
        List<Source> list = new ArrayList<>(
                ApplicationContext.getInstance().getFolkloreEntityCollections().getSources());
        return FXCollections.observableList(list);
    }

    private ObservableList<Album> collectAlbums() {
        List<Album> list = new ArrayList<>(ApplicationContext.getInstance().getFolkloreEntityCollections().getAlbums());
        return FXCollections.observableList(list);
    }

    private boolean hasMission(Artist artist, Collection<ArtistMission> missions) {
        for (ArtistMission mission : missions) {
            if (artist.getMissions().contains(mission)) {
                return true;
            }
        }
        return false;
    }

}
