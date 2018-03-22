package org.yankov.mso.application.ui.tabs.buttons;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.scene.Cursor;
import javafx.scene.control.Button;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableView;
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
    public static final String BTN_DUPLICATE = CLASS_NAME + "-btn-duplicate";
    public static final String BTN_COPY_PROPERTIES = CLASS_NAME + "-btn-copy-properties";
    public static final String BTN_APPLY_PROPERTIES = CLASS_NAME + "-btn-apply-properties";
    public static final String BTN_CLEAR = CLASS_NAME + "-btn-clear";
    public static final String BTN_LOAD_ALBUM_TRACKS = CLASS_NAME + "-btn-load-album-tracks";
    public static final String BTN_EDIT_PROPERTIES = CLASS_NAME + "-btn-edit-properties";
    public static final String BTN_PLAYER_RUN = CLASS_NAME + "-btn-player-run";
    public static final String BTN_PLAYER_STOP = CLASS_NAME + "-btn-player-stop";
    public static final String BTN_SAVE = CLASS_NAME + "-btn-upload";
    public static final String BTN_UPDATE = CLASS_NAME + "-btn-update";
    public static final String BTN_EXPORT = CLASS_NAME + "-btn-export";
    public static final String UPLOAD_STARTED = CLASS_NAME + "-upload-started";
    public static final String UPLOAD_COMPLETED = CLASS_NAME + "-upload-completed";
    public static final String UNABLE_WRITE_FILE = CLASS_NAME + "-unable-write-file";
    public static final String ERROR_UPDATE_DATA_MODEL = CLASS_NAME + "-error-update-data-model";

    private static final String DEFAULT_TRACK_ORDER = "0";

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

    private static final Double SPACE = 25.0;
    private static final Insets INSETS = new Insets(25.0);
    private static final Double MIN_WIDTH = 200.0;

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
        container.setPadding(INSETS);
        container.setSpacing(SPACE);
        container.setMinWidth(MIN_WIDTH);
        allButtons = createAllButtons();
        container.getChildren().addAll(selectButtons.apply(allButtons));
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
        btnPlay.setOnAction(this::handlePlay);
        buttons.put(BTN_PLAYER_RUN, btnPlay);

        Button btnSave = new Button();
        btnSave.setText(resourceBundle.getString(BTN_SAVE));
        btnSave.setMaxWidth(Double.MAX_VALUE);
        btnSave.setOnAction(this::handleBtnSave);
        buttons.put(BTN_SAVE, btnSave);

        Button btnAdd = new Button();
        btnAdd.setText(resourceBundle.getString(BTN_ADD));
        btnAdd.setMaxWidth(Double.MAX_VALUE);
        btnAdd.setOnAction(this::handleBtnAdd);
        buttons.put(BTN_ADD, btnAdd);

        Button btnRemove = new Button();
        btnRemove.setText(resourceBundle.getString(BTN_REMOVE));
        btnRemove.setMaxWidth(Double.MAX_VALUE);
        btnRemove.setOnAction(this::handleBtnRemove);
        buttons.put(BTN_REMOVE, btnRemove);

        Button btnDuplicate = new Button();
        btnDuplicate.setText(resourceBundle.getString(BTN_DUPLICATE));
        btnDuplicate.setMaxWidth(Double.MAX_VALUE);
        btnDuplicate.setOnAction(this::handleBtnDuplicate);
        buttons.put(BTN_DUPLICATE, btnDuplicate);

        Button btnCopyProperties = new Button();
        btnCopyProperties.setText(resourceBundle.getString(BTN_COPY_PROPERTIES));
        btnCopyProperties.setMaxWidth(Double.MAX_VALUE);
        btnCopyProperties.setOnAction(this::handleBtnCopyProperties);
        buttons.put(BTN_COPY_PROPERTIES, btnCopyProperties);

        Button btnApplyProperties = new Button();
        btnApplyProperties.setText(resourceBundle.getString(BTN_APPLY_PROPERTIES));
        btnApplyProperties.setMaxWidth(Double.MAX_VALUE);
        btnApplyProperties.setOnAction(this::handleBtnApplyProperties);
        btnApplyProperties.setDisable(true);
        buttons.put(BTN_APPLY_PROPERTIES, btnApplyProperties);

        Button btnClear = new Button();
        btnClear.setText(resourceBundle.getString(BTN_CLEAR));
        btnClear.setMaxWidth(Double.MAX_VALUE);
        btnClear.setOnAction(this::handleBtnClear);
        buttons.put(BTN_CLEAR, btnClear);

        Button btnLoadAlbumTracks = new Button();
        btnLoadAlbumTracks.setText(resourceBundle.getString(BTN_LOAD_ALBUM_TRACKS));
        btnLoadAlbumTracks.setMaxWidth(Double.MAX_VALUE);
        btnLoadAlbumTracks.setOnAction(this::handleBtnLoadAlbumTracks);
        buttons.put(BTN_LOAD_ALBUM_TRACKS, btnLoadAlbumTracks);

        Button btnExport = new Button();
        btnExport.setText(resourceBundle.getString(BTN_EXPORT));
        btnExport.setMaxWidth(Double.MAX_VALUE);
        btnExport.setOnAction(this::handleBtnExport);
        buttons.put(BTN_EXPORT, btnExport);

        Button btnUpdate = new Button();
        btnUpdate.setText(resourceBundle.getString(BTN_UPDATE));
        btnUpdate.setMaxWidth(Double.MAX_VALUE);
        btnUpdate.setOnAction(this::handleBtnUpdate);
        buttons.put(BTN_UPDATE, btnUpdate);

        return buttons;
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

    private void handleBtnSave(ActionEvent event) {
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

    private void handleBtnDuplicate(ActionEvent event) {
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
        // System.out.println(selected.size());
        for (Integer index : selected) {
            // System.out.println(index);
            PropertiesType item = table.getItems().get(index);
            propertiesCopier.accept(copiedProperties, item);
            table.getItems().set(index, item);
            // System.out.println(selected.size());
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
