package org.yankov.mso.application.ui;

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
import org.yankov.mso.application.ApplicationContext;
import org.yankov.mso.datamodel.folklore.FolklorePiece;

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

    public FolkloreInputTabControls() {
        this.content = new VBox();
    }

    @Override
    public Node getContent() {
        return content;
    }

    @Override
    public void layout() {
        HBox tableContainer = new HBox();
        Node table = createTable();
        Node actionButtons = createActionButtons();
        tableContainer.getChildren().add(table);
        tableContainer.getChildren().add(actionButtons);
        HBox.setHgrow(table, Priority.ALWAYS);

        content.getChildren().add(tableContainer);
    }

    private Node createTable() {
        TableView<FolklorePiece> table = new TableView<>();
        table.setEditable(false);
        table.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        table.getColumns().addAll(createTableColumns());

        StackPane container = new StackPane();
        container.getChildren().add(table);

        return container;
    }

    private List<TableColumn<FolklorePiece, String>> createTableColumns() {
        ResourceBundle bundle = ApplicationContext.getInstance().getFolkloreResourceBundle();

        TableColumn<FolklorePiece, String> colAlbum = new TableColumn<>(bundle.getString(COL_ALBUM));
        TableColumn<FolklorePiece, String> colAlbumTrackOrder = new TableColumn<>(
                bundle.getString(COL_ALBUM_TRACK_ORDER));
        TableColumn<FolklorePiece, String> colTitle = new TableColumn<>(bundle.getString(COL_TITLE));
        TableColumn<FolklorePiece, String> colPerformer = new TableColumn<>(bundle.getString(COL_PERFORMER));
        TableColumn<FolklorePiece, String> colAccompanimentPerformer = new TableColumn<>(
                bundle.getString(COL_ACCOMPANIMENT_PERFORMER));
        TableColumn<FolklorePiece, String> colAuthor = new TableColumn<>(bundle.getString(COL_AUTHOR));
        TableColumn<FolklorePiece, String> colArrangementAuthor = new TableColumn<>(
                bundle.getString(COL_ARRANGEMENT_AUTHOR));
        TableColumn<FolklorePiece, String> colConductor = new TableColumn<>(bundle.getString(COL_CONDUCTOR));
        TableColumn<FolklorePiece, String> colSoloist = new TableColumn<>(bundle.getString(COL_SOLOIST));
        TableColumn<FolklorePiece, String> colDuration = new TableColumn<>(bundle.getString(COL_DURATION));
        TableColumn<FolklorePiece, String> colSource = new TableColumn<>(bundle.getString(COL_SOURCE));
        TableColumn<FolklorePiece, String> colEthnographicRegion = new TableColumn<>(
                bundle.getString(COL_ETHNOGRAPHIC_REGION));
        TableColumn<FolklorePiece, String> colFile = new TableColumn<>(bundle.getString(COL_FILE));
        TableColumn<FolklorePiece, String> colNote = new TableColumn<>(bundle.getString(COL_NOTE));

        List<TableColumn<FolklorePiece, String>> columns = new ArrayList<>();
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
        columns.add(colEthnographicRegion);
        columns.add(colFile);
        columns.add(colNote);

        return columns;
    }

    private Node createActionButtons() {
        VBox container = new VBox();
        container.setPadding(BUTTONS_INSETS);
        container.setSpacing(BUTTONS_SPACE);

        ResourceBundle bundle = ApplicationContext.getInstance().getFolkloreResourceBundle();

        Button btnAdd = new Button();
        btnAdd.setText(bundle.getString(BTN_ADD));
        btnAdd.setMaxWidth(Double.MAX_VALUE);
        container.getChildren().add(btnAdd);

        Button btnRemove = new Button();
        btnRemove.setText(bundle.getString(BTN_REMOVE));
        btnRemove.setMaxWidth(Double.MAX_VALUE);
        container.getChildren().add(btnRemove);

        Button btnCopy = new Button();
        btnCopy.setText(bundle.getString(BTN_COPY));
        btnCopy.setMaxWidth(Double.MAX_VALUE);
        container.getChildren().add(btnCopy);

        Button btnClear = new Button();
        btnClear.setText(bundle.getString(BTN_CLEAR));
        btnClear.setMaxWidth(Double.MAX_VALUE);
        container.getChildren().add(btnClear);

        Button btnLoadAlbumTracks = new Button();
        btnLoadAlbumTracks.setText(bundle.getString(BTN_LOAD_ALBUM_TRACKS));
        btnLoadAlbumTracks.setMaxWidth(Double.MAX_VALUE);
        container.getChildren().add(btnLoadAlbumTracks);

        return container;
    }

}
