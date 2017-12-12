package org.yankov.mso.application.ui;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import org.yankov.mso.application.ApplicationContext;
import org.yankov.mso.datamodel.folklore.EthnographicRegion;
import org.yankov.mso.datamodel.generic.Artist;
import org.yankov.mso.datamodel.generic.Source;

import java.io.File;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

public class FolkloreInputTabControls implements UserInterfaceControls<Node> {

    private static final String CLASS_NAME = FolkloreInputTabControls.class.getName();

    public static final String COL_ALBUM = CLASS_NAME + "-col-album";
    public static final String COL_ALBUM_TRACK_ORDER = CLASS_NAME + "-col-album-track-order";
    public static final String COL_TITLE = CLASS_NAME + "-col-title";
    public static final String COL_PERFORMER = CLASS_NAME + "-col-performer";
    public static final String COL_ACCOMPANIMENT_PERFORMER = CLASS_NAME + "-col-accompaniment-performer";
    public static final String COL_AUTHOR = CLASS_NAME + "-col-author";
    public static final String COL_ARRANGEMENT_AUTHOR = CLASS_NAME + "-col-arrangement-author";
    public static final String COL_CONDUCTOR = CLASS_NAME + "-col-conductor";
    public static final String COL_SOLOIST = CLASS_NAME + "-col-soloist";
    public static final String COL_DURATION = CLASS_NAME + "-col-duration";
    public static final String COL_SOURCE = CLASS_NAME + "-col-source";
    public static final String COL_ETHNOGRAPHIC_REGION = CLASS_NAME + "-col-ethnographic-region";
    public static final String COL_FILE = CLASS_NAME + "-col-record";
    public static final String COL_NOTE = CLASS_NAME + "-col-note";

    public static final String BTN_ADD = CLASS_NAME + "-btn-add";
    public static final String BTN_REMOVE = CLASS_NAME + "-btn-remove";
    public static final String BTN_COPY = CLASS_NAME + "-btn-copy";
    public static final String BTN_CLEAR = CLASS_NAME + "-btn-clear";
    public static final String BTN_LOAD_ALBUM_TRACKS = CLASS_NAME + "-btn-load-album-tracks";

    public static final String SELECT_AUDIO_FILES = CLASS_NAME + "-select-audio-files";
    public static final String FLAC_FILTER_NAME = CLASS_NAME + "-flac-filter-name";
    public static final String FLAC_FILTER_EXT = CLASS_NAME + "-flac-filter-ext";

    private static final String DURATION_FORMAT = "%02d:%02d";

    private static final Double BUTTONS_SPACE = 25.0;
    private static final Insets BUTTONS_INSETS = new Insets(25.0);

    private ResourceBundle resourceBundle;

    private VBox content;
    private TableView<FolklorePieceProperties> table;

    public FolkloreInputTabControls() {
        this.content = new VBox();
        this.table = new TableView<>();
        this.resourceBundle = ApplicationContext.getInstance().getFolkloreResourceBundle();
    }

    @Override
    public Node getContent() {
        return content;
    }

    @Override
    public void layout() {
        HBox container = new HBox();

        initializeTable();
        container.getChildren().add(new StackPane(table));
        HBox.setHgrow(table, Priority.ALWAYS);

        VBox buttonsContainer = new VBox();
        buttonsContainer.setPadding(BUTTONS_INSETS);
        buttonsContainer.setSpacing(BUTTONS_SPACE);
        buttonsContainer.getChildren().addAll(createActionButtons());
        container.getChildren().add(buttonsContainer);

        content.getChildren().add(container);
    }

    private void initializeTable() {
        table.setEditable(false);
        table.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        table.getColumns().addAll(createTableColumns());
        table.setItems(FXCollections.observableArrayList());
    }

    private List<TableColumn<FolklorePieceProperties, String>> createTableColumns() {
        ResourceBundle bundle = ApplicationContext.getInstance().getFolkloreResourceBundle();

        TableColumn<FolklorePieceProperties, String> colAlbum = new TableColumn<>(bundle.getString(COL_ALBUM));
        colAlbum.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getAlbum()));

        TableColumn<FolklorePieceProperties, String> colAlbumTrackOrder = new TableColumn<>(
                bundle.getString(COL_ALBUM_TRACK_ORDER));
        colAlbumTrackOrder.setCellValueFactory(
                param -> new SimpleStringProperty(Integer.toString(param.getValue().getAlbumTrackOrder())));

        TableColumn<FolklorePieceProperties, String> colTitle = new TableColumn<>(bundle.getString(COL_TITLE));
        colTitle.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getTitle()));

        TableColumn<FolklorePieceProperties, String> colPerformer = new TableColumn<>(bundle.getString(COL_PERFORMER));
        colPerformer.setCellValueFactory(param -> {
            Artist performer = param.getValue().getPerformer();
            String s = performer != null ? performer.getName() : null;
            return new SimpleStringProperty(s);
        });

        TableColumn<FolklorePieceProperties, String> colAccompanimentPerformer = new TableColumn<>(
                bundle.getString(COL_ACCOMPANIMENT_PERFORMER));
        colAccompanimentPerformer.setCellValueFactory(param -> {
            Artist accompanimentPerformer = param.getValue().getAccompanimentPerformer();
            String s = accompanimentPerformer != null ? accompanimentPerformer.getName() : null;
            return new SimpleStringProperty(s);
        });

        TableColumn<FolklorePieceProperties, String> colAuthor = new TableColumn<>(bundle.getString(COL_AUTHOR));
        colAuthor.setCellValueFactory(param -> {
            Artist author = param.getValue().getAuthor();
            String s = author != null ? author.getName() : null;
            return new SimpleStringProperty(s);
        });

        TableColumn<FolklorePieceProperties, String> colArrangementAuthor = new TableColumn<>(
                bundle.getString(COL_ARRANGEMENT_AUTHOR));
        colArrangementAuthor.setCellValueFactory(param -> {
            Artist arrangementAuthor = param.getValue().getArrangementAuthor();
            String s = arrangementAuthor != null ? arrangementAuthor.getName() : null;
            return new SimpleStringProperty(s);
        });

        TableColumn<FolklorePieceProperties, String> colConductor = new TableColumn<>(bundle.getString(COL_CONDUCTOR));
        colConductor.setCellValueFactory(param -> {
            Artist conductor = param.getValue().getConductor();
            String s = conductor != null ? conductor.getName() : null;
            return new SimpleStringProperty(s);
        });

        TableColumn<FolklorePieceProperties, String> colSoloist = new TableColumn<>(bundle.getString(COL_SOLOIST));
        colSoloist.setCellValueFactory(param -> {
            Artist soloist = param.getValue().getSoloist();
            String s = soloist != null ? soloist.getName() : null;
            return new SimpleStringProperty(s);
        });

        TableColumn<FolklorePieceProperties, String> colDuration = new TableColumn<>(bundle.getString(COL_DURATION));
        colDuration.setCellValueFactory(param -> {
            Duration d = param.getValue().getDuration();
            String s = d != null ? String.format(DURATION_FORMAT, d.toMinutesPart(), d.toSecondsPart()) : null;
            return new SimpleStringProperty(s);
        });

        TableColumn<FolklorePieceProperties, String> colSource = new TableColumn<>(bundle.getString(COL_SOURCE));
        colSoloist.setCellValueFactory(param -> {
            Source source = param.getValue().getSource();
            String s = source != null ? source.getType() + "/" + source.getSignature() : null;
            return new SimpleStringProperty(s);
        });

        TableColumn<FolklorePieceProperties, String> colFile = new TableColumn<>(bundle.getString(COL_FILE));
        colFile.setCellValueFactory(param -> {
            File file = param.getValue().getFile();
            String s = file != null ? file.getName() : null;
            return new SimpleStringProperty(s);
        });

        TableColumn<FolklorePieceProperties, String> colEthnographicRegion = new TableColumn<>(
                bundle.getString(COL_ETHNOGRAPHIC_REGION));
        colEthnographicRegion.setCellValueFactory(param -> {
            EthnographicRegion ethnographicRegion = param.getValue().getEthnographicRegion();
            String s = ethnographicRegion != null ? ethnographicRegion.getName() : null;
            return new SimpleStringProperty(s);
        });

        TableColumn<FolklorePieceProperties, String> colNote = new TableColumn<>(bundle.getString(COL_NOTE));
        colNote.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getNote()));

        List<TableColumn<FolklorePieceProperties, String>> columns = new ArrayList<>();

        columns.add(colAlbum);
        columns.add(colAlbumTrackOrder);
        columns.add(colTitle);
        columns.add(colPerformer);
        columns.add(colAccompanimentPerformer);
        columns.add(colAuthor);
        columns.add(colArrangementAuthor);
        columns.add(colConductor);
        columns.add(colSoloist);
        columns.add(colDuration);
        columns.add(colSource);
        columns.add(colFile);
        columns.add(colEthnographicRegion);
        columns.add(colNote);

        return columns;
    }

    private List<Button> createActionButtons() {
        Button btnAdd = new Button();
        btnAdd.setText(resourceBundle.getString(BTN_ADD));
        btnAdd.setMaxWidth(Double.MAX_VALUE);
        btnAdd.setOnAction(this::handleBtnAddAction);

        Button btnRemove = new Button();
        btnRemove.setText(resourceBundle.getString(BTN_REMOVE));
        btnRemove.setMaxWidth(Double.MAX_VALUE);
        btnRemove.setOnAction(this::handleBtnRemoveAction);

        Button btnCopy = new Button();
        btnCopy.setText(resourceBundle.getString(BTN_COPY));
        btnCopy.setMaxWidth(Double.MAX_VALUE);
        btnCopy.setOnAction(this::handleBtnCopyAction);

        Button btnClear = new Button();
        btnClear.setText(resourceBundle.getString(BTN_CLEAR));
        btnClear.setMaxWidth(Double.MAX_VALUE);
        btnClear.setOnAction(this::handleBtnClearAction);

        Button btnLoadAlbumTracks = new Button();
        btnLoadAlbumTracks.setText(resourceBundle.getString(BTN_LOAD_ALBUM_TRACKS));
        btnLoadAlbumTracks.setMaxWidth(Double.MAX_VALUE);
        btnLoadAlbumTracks.setOnAction(this::handleBtnLoadAlbumTracksAction);

        List<Button> buttons = new ArrayList<>();

        buttons.add(btnAdd);
        buttons.add(btnRemove);
        buttons.add(btnCopy);
        buttons.add(btnClear);
        buttons.add(btnLoadAlbumTracks);

        return buttons;
    }

    private void refreshPieceOrder() {
        for (int i = 0; i < table.getItems().size(); i++) {
            table.getItems().get(i).setAlbumTrackOrder(i + 1);
        }
    }

    private void handleBtnAddAction(ActionEvent event) {
        table.getItems().add(new FolklorePieceProperties());
        refreshPieceOrder();
    }

    private void handleBtnRemoveAction(ActionEvent event) {
        int selectedIndex = table.getSelectionModel().getSelectedIndex();
        if (selectedIndex >= 0) {
            table.getItems().remove(selectedIndex);
            refreshPieceOrder();
        }
    }

    private void handleBtnCopyAction(ActionEvent event) {
        int selectedIndex = table.getSelectionModel().getSelectedIndex();
        if (selectedIndex >= 0) {
            table.getItems().add(table.getItems().get(selectedIndex).clone());
            refreshPieceOrder();
        }
    }

    private void handleBtnClearAction(ActionEvent event) {
        table.getItems().clear();
    }

    private void handleBtnLoadAlbumTracksAction(ActionEvent event) {
        Optional<List<File>> files = selectFiles();
        if (files.isPresent()) {
            for (File file : files.get()) {
                FolklorePieceProperties piece = new FolklorePieceProperties();
                piece.setFromFile(file);
                table.getItems().add(piece);
            }
            refreshPieceOrder();
        }
    }

    private Optional<List<File>> selectFiles() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle(resourceBundle.getString(SELECT_AUDIO_FILES));
        FileChooser.ExtensionFilter flacFilter = new FileChooser.ExtensionFilter(
                resourceBundle.getString(FLAC_FILTER_NAME), resourceBundle.getString(FLAC_FILTER_EXT));
        fileChooser.getExtensionFilters().add(flacFilter);
        List<File> files = fileChooser.showOpenMultipleDialog(
                ApplicationContext.getInstance().getApplicationSettings().getScene().getWindow());
        return files != null ? Optional.of(files) : Optional.empty();
    }

}
