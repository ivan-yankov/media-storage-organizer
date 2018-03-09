package org.yankov.mso.application.ui.tabs.buttons;

import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.scene.Cursor;
import javafx.scene.control.Button;
import javafx.scene.control.TableView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import org.yankov.mso.application.ApplicationContext;
import org.yankov.mso.application.UserInterfaceControls;
import org.yankov.mso.application.utils.FlacPlayer;
import org.yankov.mso.application.utils.FxUtils;
import org.yankov.mso.database.EntityCollections;
import org.yankov.mso.datamodel.Piece;
import org.yankov.mso.datamodel.PieceProperties;
import org.yankov.mso.datamodel.PiecePropertiesUtils;

import javax.sound.sampled.LineEvent;
import java.io.File;
import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.function.UnaryOperator;
import java.util.logging.Level;
import java.util.stream.Stream;

public class Buttons<PropertiesType extends PieceProperties, EntityType extends Piece>
        implements UserInterfaceControls {

    private static final String CLASS_NAME = Buttons.class.getName();

    public static final String BTN_ADD = CLASS_NAME + "-btn-add";
    public static final String BTN_REMOVE = CLASS_NAME + "-btn-remove";
    public static final String BTN_COPY = CLASS_NAME + "-btn-copy";
    public static final String BTN_CLEAR = CLASS_NAME + "-btn-clear";
    public static final String BTN_LOAD_ALBUM_TRACKS = CLASS_NAME + "-btn-load-album-tracks";
    public static final String BTN_EDIT_PROPERTIES = CLASS_NAME + "-btn-edit-properties";
    public static final String BTN_PLAYER_RUN = CLASS_NAME + "-btn-player-run";
    public static final String BTN_PLAYER_STOP = CLASS_NAME + "-btn-player-stop";
    public static final String BTN_UPLOAD = CLASS_NAME + "-btn-upload";

    public static final String UPLOAD_COMPLETED = CLASS_NAME + "-upload-completed";
    public static final String SELECT_AUDIO_FILES = CLASS_NAME + "-select-audio-files";
    public static final String FLAC_FILTER_NAME = CLASS_NAME + "-flac-filter-name";
    public static final String FLAC_FILTER_EXT = CLASS_NAME + "-flac-filter-ext";

    private ResourceBundle resourceBundle;
    private Consumer<TableView<PropertiesType>> editPieceCommand;
    private EntityCollections<EntityType> entityCollections;
    private Function<PropertiesType, EntityType> entityCreator;
    private Function<Map<String, Button>, List<Button>> selectButtons;
    private TableView<PropertiesType> table;
    private Supplier<PropertiesType> propertiesCreator;
    private UnaryOperator<PropertiesType> propertiesCopier;

    private VBox container;

    private static final Double SPACE = 25.0;
    private static final Insets INSETS = new Insets(25.0);
    private static final Double MIN_WIDTH = 250.0;

    public Buttons() {
        this.container = new VBox();
    }

    public void setResourceBundle(ResourceBundle resourceBundle) {
        this.resourceBundle = resourceBundle;
    }

    public void setEditPieceCommand(Consumer<TableView<PropertiesType>> editPieceCommand) {
        this.editPieceCommand = editPieceCommand;
    }

    public void setEntityCollections(EntityCollections<EntityType> entityCollections) {
        this.entityCollections = entityCollections;
    }

    public void setEntityCreator(Function<PropertiesType, EntityType> entityCreator) {
        this.entityCreator = entityCreator;
    }

    public void setSelectButtons(Function<Map<String, Button>, List<Button>> selectButtons) {
        this.selectButtons = selectButtons;
    }

    public void setTable(TableView<PropertiesType> table) {
        this.table = table;
    }

    public void setPropertiesCreator(Supplier<PropertiesType> propertiesCreator) {
        this.propertiesCreator = propertiesCreator;
    }

    public void setPropertiesCopier(UnaryOperator<PropertiesType> propertiesCopier) {
        this.propertiesCopier = propertiesCopier;
    }

    @Override
    public void layout() {
        container.setPadding(INSETS);
        container.setSpacing(SPACE);
        container.setMinWidth(MIN_WIDTH);
        container.getChildren().addAll(selectButtons.apply(createAllButtons()));
    }

    @Override
    public Pane getContainer() {
        return container;
    }

    private Map<String, Button> createAllButtons() {
        Map<String, Button> buttons = new HashMap<>();

        Button btnEdit = new Button();
        btnEdit.setText(resourceBundle.getString(BTN_EDIT_PROPERTIES));
        btnEdit.setMaxWidth(Double.MAX_VALUE);
        btnEdit.setOnAction(event -> editPieceCommand.accept(table));
        buttons.put(BTN_EDIT_PROPERTIES, btnEdit);

        Button btnPlay = new Button();
        btnPlay.setText(resourceBundle.getString(BTN_PLAYER_RUN));
        btnPlay.setMaxWidth(Double.MAX_VALUE);
        btnPlay.setOnAction(this::handlePlayAction);
        buttons.put(BTN_PLAYER_RUN, btnPlay);

        Button btnUpload = new Button();
        btnUpload.setText(resourceBundle.getString(BTN_UPLOAD));
        btnUpload.setMaxWidth(Double.MAX_VALUE);
        btnUpload.setOnAction(this::handleUploadAction);
        buttons.put(BTN_UPLOAD, btnUpload);

        Button btnAdd = new Button();
        btnAdd.setText(resourceBundle.getString(BTN_ADD));
        btnAdd.setMaxWidth(Double.MAX_VALUE);
        btnAdd.setOnAction(this::handleBtnAddAction);
        buttons.put(BTN_ADD, btnAdd);

        Button btnRemove = new Button();
        btnRemove.setText(resourceBundle.getString(BTN_REMOVE));
        btnRemove.setMaxWidth(Double.MAX_VALUE);
        btnRemove.setOnAction(this::handleBtnRemoveAction);
        buttons.put(BTN_REMOVE, btnRemove);

        Button btnCopy = new Button();
        btnCopy.setText(resourceBundle.getString(BTN_COPY));
        btnCopy.setMaxWidth(Double.MAX_VALUE);
        btnCopy.setOnAction(this::handleBtnCopyAction);
        buttons.put(BTN_COPY, btnCopy);

        Button btnClear = new Button();
        btnClear.setText(resourceBundle.getString(BTN_CLEAR));
        btnClear.setMaxWidth(Double.MAX_VALUE);
        btnClear.setOnAction(this::handleBtnClearAction);
        buttons.put(BTN_CLEAR, btnClear);

        Button btnLoadAlbumTracks = new Button();
        btnLoadAlbumTracks.setText(resourceBundle.getString(BTN_LOAD_ALBUM_TRACKS));
        btnLoadAlbumTracks.setMaxWidth(Double.MAX_VALUE);
        btnLoadAlbumTracks.setOnAction(this::handleBtnLoadAlbumTracksAction);
        buttons.put(BTN_LOAD_ALBUM_TRACKS, btnLoadAlbumTracks);

        return buttons;
    }

    private void handlePlayAction(ActionEvent event) {
        if (table.getSelectionModel().getSelectedIndex() < 0 || table.getSelectionModel().getSelectedItem()
                                                                     .getRecord() == null) {
            return;
        }

        FlacPlayer flacPlayer = FlacPlayer.getInstance();
        if (flacPlayer.isPlaying()) {
            flacPlayer.stop();
        } else {
            flacPlayer.addListener(e -> Platform.runLater(() -> updateButtonText((Button) event.getSource(), e)));
            flacPlayer.setBytes(table.getSelectionModel().getSelectedItem().getRecord().getBytes());
            Thread thread = new Thread(flacPlayer::play);
            thread.start();
        }
    }

    private void updateButtonText(Button button, LineEvent event) {
        if (event.getType() == LineEvent.Type.START) {
            button.setText(resourceBundle.getString(BTN_PLAYER_STOP));
        } else if (event.getType() == LineEvent.Type.STOP) {
            button.setText(resourceBundle.getString(BTN_PLAYER_RUN));
        }
    }

    private void handleUploadAction(ActionEvent event) {
        updateDataModel();

        Task task = new Task() {

            @Override
            protected Object call() {
                ApplicationContext context = ApplicationContext.getInstance();
                context.getPrimaryStage().getScene().setCursor(Cursor.WAIT);
                entityCollections.saveEntityCollections();
                context.getLogger().log(Level.INFO, resourceBundle.getString(UPLOAD_COMPLETED));
                context.getPrimaryStage().getScene().setCursor(Cursor.DEFAULT);
                return null;
            }

        };

        new Thread(task).start();

        clearTable();
    }

    private void updateDataModel() {
        for (PropertiesType item : table.getItems()) {
            if (item.getId() == null) {
                EntityType piece = entityCreator.apply(item);
                entityCollections.addPiece(piece);
            } else {
                Stream<EntityType> pieces = entityCollections.getPieces().stream();
                Optional<EntityType> piece = pieces.filter(p -> p.getId().equals(item.getId())).findFirst();
                piece.ifPresent(p -> PiecePropertiesUtils.setPropertiesToPiece(item, p));
            }
        }
    }

    private void handleBtnAddAction(ActionEvent event) {
        table.getItems().add(propertiesCreator.get());
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
            table.getItems().add(propertiesCopier.apply(table.getItems().get(selectedIndex)));
        }
    }

    private void handleBtnClearAction(ActionEvent event) {
        clearTable();
    }

    private void handleBtnLoadAlbumTracksAction(ActionEvent event) {
        FileChooser.ExtensionFilter filter = new FileChooser.ExtensionFilter(resourceBundle.getString(FLAC_FILTER_NAME),
                                                                             resourceBundle.getString(FLAC_FILTER_EXT));
        Optional<List<File>> files = FxUtils.selectFiles(resourceBundle.getString(SELECT_AUDIO_FILES), false, filter);

        if (files.isPresent()) {
            for (File file : files.get()) {
                table.getItems().add(PiecePropertiesUtils.createPropertiesFromFile(propertiesCreator, file));
            }
        }
    }

    private void clearTable() {
        if (FlacPlayer.getInstance().isPlaying()) {
            FlacPlayer.getInstance().stop();
        }
        table.getItems().clear();
    }

}
