package org.yankov.mso.application.ui.input;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import org.yankov.mso.application.ApplicationContext;
import org.yankov.mso.application.ui.datamodel.FolklorePieceProperties;
import org.yankov.mso.application.UserInterfaceControls;
import org.yankov.mso.application.ui.font.CustomFont;
import org.yankov.mso.application.ui.font.FontFamily;
import org.yankov.mso.application.ui.font.FontStyle;
import org.yankov.mso.application.ui.font.FontWeight;
import org.yankov.mso.application.utils.FxUtils;
import org.yankov.mso.datamodel.folklore.EthnographicRegion;
import org.yankov.mso.datamodel.generic.Artist;
import org.yankov.mso.datamodel.generic.Source;

import java.io.File;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class FolkloreInputTable implements UserInterfaceControls {

    private static final String CLASS_NAME = FolkloreInputTable.class.getName();

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

    private static final String DURATION_FORMAT = "%02d:%02d";

    private static final CustomFont TABLE_FONT = new CustomFont(FontFamily.SANS_SERIF, FontWeight.NORMAL,
                                                                FontStyle.NORMAL, 12);

    private final ResourceBundle resourceBundle = ApplicationContext.getInstance().getFolkloreResourceBundle();

    private TableView<FolklorePieceProperties> table;
    private StackPane container;

    public FolkloreInputTable() {
        this.table = new TableView<>();
        this.container = new StackPane(table);
    }

    public TableView<FolklorePieceProperties> getTableView() {
        return table;
    }

    @Override
    public void layout() {
        table.setEditable(false);
        table.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        table.getColumns().addAll(createTableColumns());
        table.getColumns().forEach(column -> column.setStyle(TABLE_FONT.toCssRepresentation()));

        ObservableList<FolklorePieceProperties> items = FXCollections.observableArrayList();
        items.addListener(this::handleTableChange);
        table.setItems(items);
    }

    @Override
    public Pane getContainer() {
        return container;
    }

    private List<TableColumn<FolklorePieceProperties, String>> createTableColumns() {
        TableColumn<FolklorePieceProperties, String> colAlbum = new TableColumn<>(resourceBundle.getString(COL_ALBUM));
        colAlbum.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getAlbum()));
        colAlbum.setCellFactory(column -> FxUtils.createTextCellAligned(column, Pos.BASELINE_RIGHT));

        TableColumn<FolklorePieceProperties, String> colAlbumTrackOrder = new TableColumn<>(
                resourceBundle.getString(COL_ALBUM_TRACK_ORDER));
        colAlbumTrackOrder.setCellValueFactory(
                param -> new SimpleStringProperty(Integer.toString(param.getValue().getAlbumTrackOrder())));
        colAlbumTrackOrder.setCellFactory(column -> FxUtils.createTextCellAligned(column, Pos.BASELINE_RIGHT));

        TableColumn<FolklorePieceProperties, String> colTitle = new TableColumn<>(resourceBundle.getString(COL_TITLE));
        colTitle.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getTitle()));

        TableColumn<FolklorePieceProperties, String> colPerformer = new TableColumn<>(
                resourceBundle.getString(COL_PERFORMER));
        colPerformer.setCellValueFactory(param -> {
            Artist performer = param.getValue().getPerformer();
            String s = performer != null ? performer.getName() : null;
            return new SimpleStringProperty(s);
        });

        TableColumn<FolklorePieceProperties, String> colAccompanimentPerformer = new TableColumn<>(
                resourceBundle.getString(COL_ACCOMPANIMENT_PERFORMER));
        colAccompanimentPerformer.setCellValueFactory(param -> {
            Artist accompanimentPerformer = param.getValue().getAccompanimentPerformer();
            String s = accompanimentPerformer != null ? accompanimentPerformer.getName() : null;
            return new SimpleStringProperty(s);
        });

        TableColumn<FolklorePieceProperties, String> colAuthor = new TableColumn<>(
                resourceBundle.getString(COL_AUTHOR));
        colAuthor.setCellValueFactory(param -> {
            Artist author = param.getValue().getAuthor();
            String s = author != null ? author.getName() : null;
            return new SimpleStringProperty(s);
        });

        TableColumn<FolklorePieceProperties, String> colArrangementAuthor = new TableColumn<>(
                resourceBundle.getString(COL_ARRANGEMENT_AUTHOR));
        colArrangementAuthor.setCellValueFactory(param -> {
            Artist arrangementAuthor = param.getValue().getArrangementAuthor();
            String s = arrangementAuthor != null ? arrangementAuthor.getName() : null;
            return new SimpleStringProperty(s);
        });

        TableColumn<FolklorePieceProperties, String> colConductor = new TableColumn<>(
                resourceBundle.getString(COL_CONDUCTOR));
        colConductor.setCellValueFactory(param -> {
            Artist conductor = param.getValue().getConductor();
            String s = conductor != null ? conductor.getName() : null;
            return new SimpleStringProperty(s);
        });

        TableColumn<FolklorePieceProperties, String> colSoloist = new TableColumn<>(
                resourceBundle.getString(COL_SOLOIST));
        colSoloist.setCellValueFactory(param -> {
            Artist soloist = param.getValue().getSoloist();
            String s = soloist != null ? soloist.getName() : null;
            return new SimpleStringProperty(s);
        });

        TableColumn<FolklorePieceProperties, String> colDuration = new TableColumn<>(
                resourceBundle.getString(COL_DURATION));
        colDuration.setCellValueFactory(param -> {
            Duration d = param.getValue().getDuration();
            String s = d != null ? String.format(DURATION_FORMAT, d.toMinutesPart(), d.toSecondsPart()) : null;
            return new SimpleStringProperty(s);
        });
        colDuration.setCellFactory(column -> FxUtils.createTextCellAligned(column, Pos.BASELINE_RIGHT));

        TableColumn<FolklorePieceProperties, String> colSource = new TableColumn<>(
                resourceBundle.getString(COL_SOURCE));
        colSoloist.setCellValueFactory(param -> {
            Source source = param.getValue().getSource();
            String s = source != null ? source.getType() + "/" + source.getSignature() : null;
            return new SimpleStringProperty(s);
        });

        TableColumn<FolklorePieceProperties, String> colFile = new TableColumn<>(resourceBundle.getString(COL_FILE));
        colFile.setCellValueFactory(param -> {
            File file = param.getValue().getFile();
            String s = file != null ? file.getName() : null;
            return new SimpleStringProperty(s);
        });

        TableColumn<FolklorePieceProperties, String> colEthnographicRegion = new TableColumn<>(
                resourceBundle.getString(COL_ETHNOGRAPHIC_REGION));
        colEthnographicRegion.setCellValueFactory(param -> {
            EthnographicRegion ethnographicRegion = param.getValue().getEthnographicRegion();
            String s = ethnographicRegion != null ? ethnographicRegion.getName() : null;
            return new SimpleStringProperty(s);
        });

        TableColumn<FolklorePieceProperties, String> colNote = new TableColumn<>(resourceBundle.getString(COL_NOTE));
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

    private void handleTableChange(ListChangeListener.Change<? extends FolklorePieceProperties> change) {
        refreshPieceOrder();
        resizeColumnsToFit();
    }

    private void refreshPieceOrder() {
        for (int i = 0; i < table.getItems().size(); i++) {
            table.getItems().get(i).setAlbumTrackOrder(i + 1);
        }
    }

    private void resizeColumnsToFit() {
        table.getColumns().forEach(column -> {
            double width = FxUtils.calculateTextWidth(column.getText(), TABLE_FONT);
            for (int i = 0; i < table.getItems().size(); i++) {
                ObservableValue observableValue = column.getCellObservableValue(i);
                if (observableValue != null) {
                    String columnValue = (String) observableValue.getValue();
                    width = Math.max(width, FxUtils.calculateTextWidth(columnValue, TABLE_FONT));
                }
            }
            column.setPrefWidth(width);
        });
    }

}
