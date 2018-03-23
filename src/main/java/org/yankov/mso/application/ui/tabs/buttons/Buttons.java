package org.yankov.mso.application.ui.tabs.buttons;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.scene.Cursor;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import org.yankov.mso.application.ApplicationContext;
import org.yankov.mso.application.UserInterfaceControls;
import org.yankov.mso.application.utils.FlacPlayer;
import org.yankov.mso.application.utils.FxUtils;
import org.yankov.mso.database.DatabaseOperations;
import org.yankov.mso.database.EntityCollections;
import org.yankov.mso.datamodel.Piece;
import org.yankov.mso.datamodel.PieceProperties;
import org.yankov.mso.datamodel.PiecePropertiesUtils;

import javax.sound.sampled.LineEvent;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.logging.Level;
import java.util.stream.Stream;

public class Buttons<PropertiesType extends PieceProperties, EntityType extends Piece>
        implements UserInterfaceControls {

    private static final String CLASS_NAME = Buttons.class.getName();

    public static final String BTN_ADD = CLASS_NAME + "-btn-add";
    public static final String BTN_REMOVE = CLASS_NAME + "-btn-remove";
    public static final String BTN_CLONE = CLASS_NAME + "-btn-duplicate";
    public static final String BTN_COPY_PROPERTIES = CLASS_NAME + "-btn-copy-properties";
    public static final String BTN_APPLY_PROPERTIES = CLASS_NAME + "-btn-apply-properties";
    public static final String BTN_CLEAR = CLASS_NAME + "-btn-clear";
    public static final String BTN_LOAD_ALBUM_TRACKS = CLASS_NAME + "-btn-load-album-tracks";
    public static final String BTN_EDIT_PROPERTIES = CLASS_NAME + "-btn-edit-properties";
    public static final String BTN_PLAYER_RUN = CLASS_NAME + "-btn-player-run";
    public static final String BTN_PLAYER_STOP = CLASS_NAME + "-btn-player-stop";
    public static final String BTN_UPLOAD = CLASS_NAME + "-btn-upload";
    public static final String BTN_UPDATE = CLASS_NAME + "-btn-update";
    public static final String BTN_EXPORT = CLASS_NAME + "-btn-export";
    public static final String UPLOAD_STARTED = CLASS_NAME + "-upload-started";
    public static final String UPLOAD_COMPLETED = CLASS_NAME + "-upload-completed";
    public static final String UNABLE_WRITE_FILE = CLASS_NAME + "-unable-write-file";
    public static final String ERROR_UPDATE_DATA_MODEL = CLASS_NAME + "-error-update-data-model";

    private static final String DEFAULT_TRACK_ORDER = "0";
    private static final int ICON_SIZE = 32;

    private ResourceBundle resourceBundle;
    private Consumer<TableView<PropertiesType>> editPieceCommand;
    private EntityCollections<EntityType> entityCollections;
    private Function<PropertiesType, EntityType> entityCreator;
    private Function<Map<String, Button>, List<Button>> selectButtons;
    private TableView<PropertiesType> table;
    private Supplier<PropertiesType> propertiesCreator;
    private BiConsumer<PropertiesType, PropertiesType> propertiesCopier;

    private Map<String, Button> allButtons;
    private PropertiesType copiedProperties;

    private VBox container;

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

    public void setPropertiesCopier(BiConsumer<PropertiesType, PropertiesType> propertiesCopier) {
        this.propertiesCopier = propertiesCopier;
    }

    @Override
    public void layout() {
        allButtons = createAllButtons();
        List<Button> selectedButtons = selectButtons.apply(allButtons);
        ToolBar toolBar = new ToolBar();
        selectedButtons.forEach(button -> toolBar.getItems().add(button));
        container.getChildren().add(toolBar);
    }

    @Override
    public Pane getContainer() {
        return container;
    }

    private Map<String, Button> createAllButtons() {
        Map<String, Button> buttons = new HashMap<>();

        Button btnEdit = new Button();
        btnEdit.setTooltip(new Tooltip(resourceBundle.getString(BTN_EDIT_PROPERTIES)));
        btnEdit.setGraphic(getIcon("edit"));
        btnEdit.setOnAction(event -> editPieceCommand.accept(table));
        buttons.put(BTN_EDIT_PROPERTIES, btnEdit);

        Button btnPlay = new Button();
        btnPlay.setTooltip(new Tooltip(resourceBundle.getString(BTN_PLAYER_RUN)));
        btnPlay.setGraphic(getIcon("play"));
        btnPlay.setOnAction(this::handlePlay);
        buttons.put(BTN_PLAYER_RUN, btnPlay);

        Button btnUpload = new Button();
        btnUpload.setTooltip(new Tooltip(resourceBundle.getString(BTN_UPLOAD)));
        btnUpload.setGraphic(getIcon("upload"));
        btnUpload.setOnAction(this::handleBtnUpload);
        buttons.put(BTN_UPLOAD, btnUpload);

        Button btnAdd = new Button();
        btnAdd.setTooltip(new Tooltip(resourceBundle.getString(BTN_ADD)));
        btnAdd.setGraphic(getIcon("add"));
        btnAdd.setOnAction(this::handleBtnAdd);
        buttons.put(BTN_ADD, btnAdd);

        Button btnRemove = new Button();
        btnRemove.setTooltip(new Tooltip(resourceBundle.getString(BTN_REMOVE)));
        btnRemove.setGraphic(getIcon("remove"));
        btnRemove.setOnAction(this::handleBtnRemove);
        buttons.put(BTN_REMOVE, btnRemove);

        Button btnClone = new Button();
        btnClone.setTooltip(new Tooltip(resourceBundle.getString(BTN_CLONE)));
        btnClone.setGraphic(getIcon("clone"));
        btnClone.setOnAction(this::handleBtnClone);
        buttons.put(BTN_CLONE, btnClone);

        Button btnCopyProperties = new Button();
        btnCopyProperties.setTooltip(new Tooltip(resourceBundle.getString(BTN_COPY_PROPERTIES)));
        btnCopyProperties.setGraphic(getIcon("copy-properties"));
        btnCopyProperties.setOnAction(this::handleBtnCopyProperties);
        buttons.put(BTN_COPY_PROPERTIES, btnCopyProperties);

        Button btnApplyProperties = new Button();
        btnApplyProperties.setTooltip(new Tooltip(resourceBundle.getString(BTN_APPLY_PROPERTIES)));
        btnApplyProperties.setGraphic(getIcon("apply-properties"));
        btnApplyProperties.setOnAction(this::handleBtnApplyProperties);
        btnApplyProperties.setDisable(true);
        buttons.put(BTN_APPLY_PROPERTIES, btnApplyProperties);

        Button btnClear = new Button();
        btnClear.setTooltip(new Tooltip(resourceBundle.getString(BTN_CLEAR)));
        btnClear.setGraphic(getIcon("clear"));
        btnClear.setOnAction(this::handleBtnClear);
        buttons.put(BTN_CLEAR, btnClear);

        Button btnLoadAlbumTracks = new Button();
        btnLoadAlbumTracks.setTooltip(new Tooltip(resourceBundle.getString(BTN_LOAD_ALBUM_TRACKS)));
        btnLoadAlbumTracks.setGraphic(getIcon("select-files"));
        btnLoadAlbumTracks.setOnAction(this::handleBtnLoadAlbumTracks);
        buttons.put(BTN_LOAD_ALBUM_TRACKS, btnLoadAlbumTracks);

        Button btnExport = new Button();
        btnExport.setTooltip(new Tooltip(resourceBundle.getString(BTN_EXPORT)));
        btnExport.setGraphic(getIcon("export"));
        btnExport.setOnAction(this::handleBtnExport);
        buttons.put(BTN_EXPORT, btnExport);

        Button btnUpdate = new Button();
        btnUpdate.setTooltip(new Tooltip(resourceBundle.getString(BTN_UPDATE)));
        btnUpdate.setGraphic(getIcon("update"));
        btnUpdate.setOnAction(this::handleBtnUpdate);
        buttons.put(BTN_UPDATE, btnUpdate);

        return buttons;
    }

    private ImageView getIcon(String name) {
        URL url = getClass().getResource("/icons/buttons/" + name + ".png");
        Image image = new Image(url.toString(), ICON_SIZE, ICON_SIZE, true, true);
        return new ImageView(image);
    }

    private void handlePlay(ActionEvent event) {
        if (table.getSelectionModel().getSelectedIndex() < 0 || table.getSelectionModel().getSelectedItem()
                                                                     .getRecord() == null) {
            return;
        }

        FlacPlayer flacPlayer = FlacPlayer.getInstance();
        if (flacPlayer.isPlaying()) {
            flacPlayer.stop();
        } else {
            flacPlayer.addListener(e -> Platform.runLater(() -> updatePlayButton((Button) event.getSource(), e)));
            flacPlayer.setBytes(table.getSelectionModel().getSelectedItem().getRecord().getBytes());
            Thread thread = new Thread(flacPlayer::play);
            thread.start();
        }
    }

    private void updatePlayButton(Button button, LineEvent event) {
        if (event.getType() == LineEvent.Type.START) {
            button.setTooltip(new Tooltip(resourceBundle.getString(BTN_PLAYER_STOP)));
            button.setGraphic(getIcon("stop"));
        } else if (event.getType() == LineEvent.Type.STOP) {
            button.setTooltip(new Tooltip(resourceBundle.getString(BTN_PLAYER_RUN)));
            button.setGraphic(getIcon("play"));
        }
    }

    private void handleBtnUpload(ActionEvent event) {
        updateDataModel((properties, piece) -> entityCollections.addPiece(piece));
        save();
        clearTable();
    }

    private void handleBtnUpdate(ActionEvent event) {
        if (FxUtils.confirmOverwrite()) {
            updateDataModel(PiecePropertiesUtils::setPropertiesToPiece);
            save();
            clearTable();
        }
    }

    private void save() {
        Task task = new Task() {

            @Override
            protected Object call() {
                ApplicationContext context = ApplicationContext.getInstance();
                context.getPrimaryStage().getScene().setCursor(Cursor.WAIT);
                context.getLogger().log(Level.INFO, resourceBundle.getString(UPLOAD_STARTED));
                DatabaseOperations.saveToDatabase(entityCollections);
                context.getLogger().log(Level.INFO, resourceBundle.getString(UPLOAD_COMPLETED));
                context.getPrimaryStage().getScene().setCursor(Cursor.DEFAULT);
                return null;
            }

        };

        new Thread(task).start();
    }

    private void updateDataModel(BiConsumer<PropertiesType, EntityType> operation) {
        for (PropertiesType properties : table.getItems()) {
            EntityType piece = null;
            if (properties.getId() == null) {
                piece = entityCreator.apply(properties);
            } else {
                Stream<EntityType> pieces = entityCollections.getPieces().stream();
                Optional<EntityType> optPiece = pieces.filter(p -> p.getId().equals(properties.getId())).findFirst();
                if (optPiece.isPresent()) {
                    piece = optPiece.get();
                }
            }

            if (piece != null) {
                operation.accept(properties, piece);
            } else {
                String msg = resourceBundle.getString(ERROR_UPDATE_DATA_MODEL);
                ApplicationContext.getInstance().getLogger().log(Level.SEVERE, msg);
            }
        }
    }

    private void handleBtnAdd(ActionEvent event) {
        PropertiesType item = propertiesCreator.get();
        item.setAlbumTrackOrder(DEFAULT_TRACK_ORDER);
        table.getItems().add(item);
    }

    private void handleBtnRemove(ActionEvent event) {
        int selectedIndex = table.getSelectionModel().getSelectedIndex();
        if (selectedIndex >= 0) {
            table.getItems().remove(selectedIndex);
        }
    }

    private void handleBtnClone(ActionEvent event) {
        int selectedIndex = table.getSelectionModel().getSelectedIndex();
        if (selectedIndex < 0) {
            return;
        }
        PropertiesType item = propertiesCreator.get();
        propertiesCopier.accept(table.getItems().get(selectedIndex), item);
        table.getItems().add(item);
    }

    private void handleBtnCopyProperties(ActionEvent event) {
        int selectedIndex = table.getSelectionModel().getSelectedIndex();
        if (selectedIndex < 0) {
            return;
        }
        copiedProperties = propertiesCreator.get();
        propertiesCopier.accept(table.getItems().get(selectedIndex), copiedProperties);
        allButtons.get(BTN_APPLY_PROPERTIES).setDisable(false);
        table.getSelectionModel().clearSelection();
        table.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
    }

    private void handleBtnApplyProperties(ActionEvent event) {
        ObservableList<Integer> selected = FXCollections.observableArrayList();
        selected.addAll(table.getSelectionModel().getSelectedIndices());
        if (copiedProperties == null || selected.isEmpty()) {
            return;
        }
        for (Integer index : selected) {
            PropertiesType item = table.getItems().get(index);
            propertiesCopier.accept(copiedProperties, item);
            table.getItems().set(index, item);
        }
        copiedProperties = null;
        allButtons.get(BTN_APPLY_PROPERTIES).setDisable(true);
        table.getSelectionModel().clearSelection();
        table.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
    }

    private void handleBtnClear(ActionEvent event) {
        clearTable();
    }

    private void handleBtnLoadAlbumTracks(ActionEvent event) {
        Optional<List<File>> files = FxUtils.selectFlacFiles(false);
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

    private void handleBtnExport(ActionEvent event) {
        Optional<File> directory = FxUtils.selectDirectory();
        if (directory.isPresent()) {
            for (PropertiesType item : table.getItems()) {
                StringBuilder outputFileName = new StringBuilder();
                outputFileName.append(directory.get().getAbsolutePath());
                outputFileName.append(File.separator);
                outputFileName.append(item.getPerformer().getName());
                outputFileName.append(" - ");
                outputFileName.append(item.getTitle());
                outputFileName.append(".");
                outputFileName.append(item.getRecord().getDataFormat().toLowerCase());
                try {
                    FileOutputStream out = new FileOutputStream(outputFileName.toString());
                    out.write(item.getRecord().getBytes());
                    out.close();
                } catch (IOException e) {
                    String msg = resourceBundle.getString(UNABLE_WRITE_FILE) + outputFileName.toString();
                    ApplicationContext.getInstance().getLogger().log(Level.SEVERE, msg, e);
                }
            }
        }
    }

}
