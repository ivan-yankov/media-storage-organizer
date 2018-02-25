package org.yankov.mso.application.ui.tabs.buttons;

import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.TableView;
import javafx.stage.FileChooser;
import org.yankov.mso.application.utils.FxUtils;
import org.yankov.mso.datamodel.PieceProperties;
import org.yankov.mso.datamodel.PiecePropertiesUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class InputTabButtons<T extends PieceProperties> extends EditButtons<T> {

    private static final String CLASS_NAME = InputTabButtons.class.getName();

    public static final String BTN_ADD = CLASS_NAME + "-btn-add";
    public static final String BTN_REMOVE = CLASS_NAME + "-btn-remove";
    public static final String BTN_COPY = CLASS_NAME + "-btn-copy";
    public static final String BTN_CLEAR = CLASS_NAME + "-btn-clear";
    public static final String BTN_LOAD_ALBUM_TRACKS = CLASS_NAME + "-btn-load-album-tracks";

    public static final String SELECT_AUDIO_FILES = CLASS_NAME + "-select-audio-files";
    public static final String FLAC_FILTER_NAME = CLASS_NAME + "-flac-filter-name";
    public static final String FLAC_FILTER_EXT = CLASS_NAME + "-flac-filter-ext";

    public InputTabButtons(TableView<T> table) {
        super(table);
    }

    @Override
    protected List<Button> createButtons() {
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
        buttons.addAll(super.createButtons());

        return buttons;
    }

    private void handleBtnAddAction(ActionEvent event) {
        table.getItems().add(itemCreator.get());
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
            table.getItems().add(itemCopier.apply(table.getItems().get(selectedIndex)));
        }
    }

    private void handleBtnClearAction(ActionEvent event) {
        table.getItems().clear();
    }

    private void handleBtnLoadAlbumTracksAction(ActionEvent event) {
        FileChooser.ExtensionFilter filter = new FileChooser.ExtensionFilter(resourceBundle.getString(FLAC_FILTER_NAME),
                                                                             resourceBundle.getString(FLAC_FILTER_EXT));
        Optional<List<File>> files = FxUtils.selectFiles(resourceBundle.getString(SELECT_AUDIO_FILES), false, filter);

        if (files.isPresent()) {
            for (File file : files.get()) {
                table.getItems().add(PiecePropertiesUtils.createPropertiesFromFile(itemCreator, file));
            }
        }
    }

}
