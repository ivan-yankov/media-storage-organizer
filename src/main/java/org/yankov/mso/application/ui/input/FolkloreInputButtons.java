package org.yankov.mso.application.ui.input;

import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.TableView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import org.yankov.mso.application.ApplicationContext;
import org.yankov.mso.application.ui.FolklorePieceProperties;
import org.yankov.mso.application.ui.UserInterfaceControls;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

public class FolkloreInputButtons implements UserInterfaceControls {

    private static final String CLASS_NAME = FolkloreInputButtons.class.getName();

    public static final String BTN_ADD = CLASS_NAME + "-btn-add";
    public static final String BTN_REMOVE = CLASS_NAME + "-btn-remove";
    public static final String BTN_COPY = CLASS_NAME + "-btn-copy";
    public static final String BTN_CLEAR = CLASS_NAME + "-btn-clear";
    public static final String BTN_LOAD_ALBUM_TRACKS = CLASS_NAME + "-btn-load-album-tracks";

    public static final String SELECT_AUDIO_FILES = CLASS_NAME + "-select-audio-files";
    public static final String FLAC_FILTER_NAME = CLASS_NAME + "-flac-filter-name";
    public static final String FLAC_FILTER_EXT = CLASS_NAME + "-flac-filter-ext";

    private static final Double BUTTONS_SPACE = 25.0;
    private static final Insets BUTTONS_INSETS = new Insets(25.0);
    private static final Double BUTTONS_MIN_WIDTH = 250.0;

    private final ResourceBundle resourceBundle = ApplicationContext.getInstance().getFolkloreResourceBundle();

    private final TableView<FolklorePieceProperties> table;
    private VBox container;

    public FolkloreInputButtons(TableView<FolklorePieceProperties> table) {
        this.table = table;
        this.container = new VBox();
    }

    @Override
    public void layout() {
        container.setPadding(BUTTONS_INSETS);
        container.setSpacing(BUTTONS_SPACE);
        container.setMinWidth(BUTTONS_MIN_WIDTH);
        container.getChildren().addAll(createActionButtons());
    }

    @Override
    public Pane getContainer() {
        return container;
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

    private void handleBtnAddAction(ActionEvent event) {
        table.getItems().add(new FolklorePieceProperties());
    }

    private void handleBtnRemoveAction(ActionEvent event) {
        int selectedIndex = table.getSelectionModel().getSelectedIndex();
        if (selectedIndex >= 0) {
            table.getItems().remove(selectedIndex);
        }
    }

    private void handleBtnCopyAction(ActionEvent event) {
        int selectedIndex = table.getSelectionModel().getSelectedIndex();
        if (selectedIndex >= 0) {
            table.getItems().add(table.getItems().get(selectedIndex).clone());
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
