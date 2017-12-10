package org.yankov.mso.application.folklore;

import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import org.yankov.mso.application.generic.ApplicationContext;
import org.yankov.mso.application.generic.UserInterfaceControls;

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
        ResourceBundle bundle = ApplicationContext.getInstance().getFolkloreResourceBundle();

        TableColumn colAlbum = new TableColumn(bundle.getString(COL_ALBUM));
        TableColumn colAlbumTrackOrder = new TableColumn(bundle.getString(COL_ALBUM_TRACK_ORDER));
        TableColumn colTitle = new TableColumn(bundle.getString(COL_TITLE));
        TableColumn colPerformer = new TableColumn(bundle.getString(COL_PERFORMER));
        TableColumn colAccompanimentPerformer = new TableColumn(bundle.getString(COL_ACCOMPANIMENT_PERFORMER));
        TableColumn colAuthor = new TableColumn(bundle.getString(COL_AUTHOR));
        TableColumn colArrangementAuthor = new TableColumn(bundle.getString(COL_ARRANGEMENT_AUTHOR));
        TableColumn colConductor = new TableColumn(bundle.getString(COL_CONDUCTOR));
        TableColumn colSoloist = new TableColumn(bundle.getString(COL_SOLOIST));
        TableColumn colDuration = new TableColumn(bundle.getString(COL_DURATION));
        TableColumn colSource = new TableColumn(bundle.getString(COL_SOURCE));
        TableColumn colEthnographicRegion = new TableColumn(bundle.getString(COL_ETHNOGRAPHIC_REGION));
        TableColumn colRecord = new TableColumn(bundle.getString(COL_FILE));
        TableColumn colNote = new TableColumn(bundle.getString(COL_NOTE));

        TableView table = new TableView();
        table.setEditable(false);
        table.getColumns()
             .addAll(colAlbum, colAlbumTrackOrder, colTitle, colPerformer, colAccompanimentPerformer, colAuthor,
                     colArrangementAuthor, colConductor, colSoloist, colDuration, colSource, colEthnographicRegion,
                     colRecord, colNote);

        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setContent(table);
        scrollPane.setFitToHeight(true);
        scrollPane.setFitToWidth(true);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);

        return scrollPane;
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
