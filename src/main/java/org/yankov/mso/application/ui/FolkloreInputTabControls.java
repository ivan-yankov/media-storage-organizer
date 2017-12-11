package org.yankov.mso.application.ui;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import org.yankov.mso.application.ApplicationContext;

import java.util.ArrayList;
import java.util.List;
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

    private static final Double BUTTONS_SPACE = 25.0;
    private static final Insets BUTTONS_INSETS = new Insets(25.0);

    private VBox content;
    private TableView<FolklorePieceProperties> table;

    public FolkloreInputTabControls() {
        this.content = new VBox();
        this.table = new TableView<>();
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

    private List<TableColumn<FolklorePieceProperties, ?>> createTableColumns() {
        ResourceBundle bundle = ApplicationContext.getInstance().getFolkloreResourceBundle();

        TableColumn<FolklorePieceProperties, String> colAlbum = new TableColumn<>(bundle.getString(COL_ALBUM));
        colAlbum.setCellValueFactory(new PropertyValueFactory<>(PieceProperties.PROPERTY_ALBUM));

        TableColumn<FolklorePieceProperties, Integer> colAlbumTrackOrder = new TableColumn<>(
                bundle.getString(COL_ALBUM_TRACK_ORDER));
        colAlbumTrackOrder.setCellValueFactory(new PropertyValueFactory<>(PieceProperties.PROPERTY_ALBUM_TRACK_ORDER));

        TableColumn<FolklorePieceProperties, String> colTitle = new TableColumn<>(bundle.getString(COL_TITLE));
        colTitle.setCellValueFactory(new PropertyValueFactory<>(PieceProperties.PROPERTY_TITLE));

        TableColumn<FolklorePieceProperties, String> colPerformer = new TableColumn<>(bundle.getString(COL_PERFORMER));
        colPerformer.setCellValueFactory(new PropertyValueFactory<>(PieceProperties.PROPERTY_PERFORMER));

        TableColumn<FolklorePieceProperties, String> colAccompanimentPerformer = new TableColumn<>(
                bundle.getString(COL_ACCOMPANIMENT_PERFORMER));
        colAccompanimentPerformer
                .setCellValueFactory(new PropertyValueFactory<>(PieceProperties.PROPERTY_ACCOMPANIMENT_PERFORMER));

        TableColumn<FolklorePieceProperties, String> colAuthor = new TableColumn<>(bundle.getString(COL_AUTHOR));
        colAuthor.setCellValueFactory(new PropertyValueFactory<>(PieceProperties.PROPERTY_AUTHOR));

        TableColumn<FolklorePieceProperties, String> colArrangementAuthor = new TableColumn<>(
                bundle.getString(COL_ARRANGEMENT_AUTHOR));
        colArrangementAuthor
                .setCellValueFactory(new PropertyValueFactory<>(PieceProperties.PROPERTY_ARRANGEMENT_AUTHOR));

        TableColumn<FolklorePieceProperties, String> colConductor = new TableColumn<>(bundle.getString(COL_CONDUCTOR));
        colConductor.setCellValueFactory(new PropertyValueFactory<>(PieceProperties.PROPERTY_CONDUCTOR));

        TableColumn<FolklorePieceProperties, String> colSoloist = new TableColumn<>(bundle.getString(COL_SOLOIST));
        colSoloist.setCellValueFactory(new PropertyValueFactory<>(PieceProperties.PROPERTY_SOLOIST));

        TableColumn<FolklorePieceProperties, String> colDuration = new TableColumn<>(bundle.getString(COL_DURATION));
        colDuration.setCellValueFactory(new PropertyValueFactory<>(PieceProperties.PROPERTY_DURATION));

        TableColumn<FolklorePieceProperties, String> colSource = new TableColumn<>(bundle.getString(COL_SOURCE));
        colSoloist.setCellValueFactory(new PropertyValueFactory<>(PieceProperties.PROPERTY_SOURCE));

        TableColumn<FolklorePieceProperties, String> colFileName = new TableColumn<>(bundle.getString(COL_FILE));
        colFileName.setCellValueFactory(new PropertyValueFactory<>(PieceProperties.PROPERTY_FILE_NAME));

        TableColumn<FolklorePieceProperties, String> colNote = new TableColumn<>(bundle.getString(COL_NOTE));
        colNote.setCellValueFactory(new PropertyValueFactory<>(PieceProperties.PROPERTY_NOTE));

        TableColumn<FolklorePieceProperties, String> colEthnographicRegion = new TableColumn<>(
                bundle.getString(COL_ETHNOGRAPHIC_REGION));
        colEthnographicRegion
                .setCellValueFactory(new PropertyValueFactory<>(FolklorePieceProperties.PROPERTY_ETHNOGRAPHIC_REGION));

        List<TableColumn<FolklorePieceProperties, ?>> columns = new ArrayList<>();

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
        columns.add(colFileName);
        columns.add(colNote);
        columns.add(colEthnographicRegion);

        return columns;
    }

    private List<Button> createActionButtons() {
        ResourceBundle bundle = ApplicationContext.getInstance().getFolkloreResourceBundle();

        Button btnAdd = new Button();
        btnAdd.setText(bundle.getString(BTN_ADD));
        btnAdd.setMaxWidth(Double.MAX_VALUE);
        btnAdd.setOnAction(this::handleBtnAddAction);

        Button btnRemove = new Button();
        btnRemove.setText(bundle.getString(BTN_REMOVE));
        btnRemove.setMaxWidth(Double.MAX_VALUE);
        btnRemove.setOnAction(this::handleBtnRemoveAction);

        Button btnCopy = new Button();
        btnCopy.setText(bundle.getString(BTN_COPY));
        btnCopy.setMaxWidth(Double.MAX_VALUE);
        btnCopy.setOnAction(this::handleBtnCopyAction);

        Button btnClear = new Button();
        btnClear.setText(bundle.getString(BTN_CLEAR));
        btnClear.setMaxWidth(Double.MAX_VALUE);
        btnClear.setOnAction(this::handleBtnClearAction);

        Button btnLoadAlbumTracks = new Button();
        btnLoadAlbumTracks.setText(bundle.getString(BTN_LOAD_ALBUM_TRACKS));
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
    }

}
