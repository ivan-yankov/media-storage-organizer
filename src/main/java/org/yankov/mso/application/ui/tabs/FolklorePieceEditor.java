package org.yankov.mso.application.ui.tabs;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.yankov.mso.application.ApplicationContext;
import org.yankov.mso.application.Form;
import org.yankov.mso.application.UserInterfaceControls;
import org.yankov.mso.application.ui.controls.FileSelectionField;
import org.yankov.mso.application.ui.controls.FolkloreComboBoxFactory;
import org.yankov.mso.application.ui.controls.LabeledTextField;
import org.yankov.mso.datamodel.FolklorePieceProperties;
import org.yankov.mso.datamodel.PiecePropertiesUtils;

import java.util.ResourceBundle;

public class FolklorePieceEditor implements Form {

    private static final String CLASS_NAME = FolklorePieceEditor.class.getName();

    public static final String STAGE_TITLE = CLASS_NAME + "-stage-title";
    public static final String BTN_OK = CLASS_NAME + "-btn-ok";
    public static final String BTN_CANCEL = CLASS_NAME + "-btn-cancel";

    private static final Insets MARGINS = new Insets(25.0);
    private static final Double HORIZONTAL_SPACE = 25.0;
    private static final Double VERTICAL_SPACE = 25.0;
    private static final Double FORM_BUTTON_WIDTH = 150.0;

    private final ResourceBundle resourceBundle = ApplicationContext.getInstance().getFolkloreResourceBundle();

    private Stage stage;
    private TableView<FolklorePieceProperties> table;
    private FolklorePieceProperties piece;
    private int selectedIndex;

    public FolklorePieceEditor(TableView<FolklorePieceProperties> table, int selectedIndex) {
        this.stage = new Stage();
        this.table = table;
        this.piece = new FolklorePieceProperties();
        this.selectedIndex = selectedIndex;
        PiecePropertiesUtils.copyFolklorePieceProperties(table.getItems().get(selectedIndex), this.piece);
    }

    @Override
    public void createControls() {
        LabeledTextField albumTrackOrder = new LabeledTextField(
            resourceBundle.getString(FolklorePieceTable.COL_ALBUM_TRACK_ORDER), piece.getAlbumTrackOrder());
        albumTrackOrder.setNewValueConsumer(piece::setAlbumTrackOrder);
        albumTrackOrder.setNewValueValidator(value -> value.matches("[0-9]+"));
        albumTrackOrder.layout();

        UserInterfaceControls album = FolkloreComboBoxFactory.createAlbum(piece);
        album.layout();

        LabeledTextField title = new LabeledTextField(resourceBundle.getString(FolklorePieceTable.COL_TITLE),
            piece.getTitle());
        title.setNewValueConsumer(piece::setTitle);
        title.layout();

        UserInterfaceControls performer = FolkloreComboBoxFactory.createPerformer(piece);
        performer.layout();

        UserInterfaceControls accompanimentPerformer = FolkloreComboBoxFactory.createAccompanimentPerformer(piece);
        accompanimentPerformer.layout();

        UserInterfaceControls arrangementAuthor = FolkloreComboBoxFactory.createArrangementAuthor(piece);
        arrangementAuthor.layout();

        UserInterfaceControls conductor = FolkloreComboBoxFactory.createConductor(piece);
        conductor.layout();

        UserInterfaceControls author = FolkloreComboBoxFactory.createAuthor(piece);
        author.layout();

        UserInterfaceControls soloist = FolkloreComboBoxFactory.createSoloist(piece);
        soloist.layout();

        UserInterfaceControls ethnographicRegion = FolkloreComboBoxFactory.createEthnographicRegion(piece);
        ethnographicRegion.layout();

        UserInterfaceControls source = FolkloreComboBoxFactory.createSource(piece);
        source.layout();

        LabeledTextField note = new LabeledTextField(resourceBundle.getString(FolklorePieceTable.COL_NOTE),
            piece.getNote());
        note.setNewValueConsumer(piece::setNote);
        note.layout();

        UserInterfaceControls fileSelectionField = new FileSelectionField(
            resourceBundle.getString(FolklorePieceTable.COL_FILE), piece.getFile(), piece::setFile);
        fileSelectionField.layout();

        GridPane gridPane = new GridPane();
        gridPane.setPadding(MARGINS);
        gridPane.setAlignment(Pos.BASELINE_LEFT);
        gridPane.setHgap(HORIZONTAL_SPACE);
        gridPane.setVgap(VERTICAL_SPACE);

        gridPane.add(albumTrackOrder.getContainer(), 0, 0);
        gridPane.add(album.getContainer(), 1, 0);
        gridPane.add(title.getContainer(), 2, 0, 2, 1);
        gridPane.add(performer.getContainer(), 0, 1);
        gridPane.add(accompanimentPerformer.getContainer(), 1, 1);
        gridPane.add(arrangementAuthor.getContainer(), 2, 1);
        gridPane.add(conductor.getContainer(), 3, 1);
        gridPane.add(author.getContainer(), 0, 2);
        gridPane.add(soloist.getContainer(), 1, 2);
        gridPane.add(ethnographicRegion.getContainer(), 0, 3);
        gridPane.add(source.getContainer(), 1, 3);
        gridPane.add(note.getContainer(), 2, 3);
        gridPane.add(fileSelectionField.getContainer(), 3, 3);

        VBox container = new VBox();
        container.getChildren().add(gridPane);
        container.getChildren().add(createFormButtons());

        initializeStage();

        stage.setScene(new Scene(new StackPane(container)));
    }

    @Override
    public void show() {
        stage.showAndWait();
    }

    @Override
    public void close() {
        stage.close();
    }

    @Override
    public void disable(boolean value) {
    }

    private void initializeStage() {
        ApplicationContext.getInstance().getApplicationSettings().getIcon().ifPresent(stage.getIcons()::add);
        stage.initOwner(ApplicationContext.getInstance().getPrimaryStage());
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setTitle(resourceBundle.getString(STAGE_TITLE));
    }

    private HBox createFormButtons() {
        Button btnOk = new Button();
        btnOk.setText(resourceBundle.getString(BTN_OK));
        btnOk.setPrefWidth(FORM_BUTTON_WIDTH);
        btnOk.setDefaultButton(true);
        btnOk.setOnAction(event -> {
            table.getItems().set(selectedIndex, piece);
            table.getSelectionModel().select(selectedIndex);
            stage.close();
        });

        Button btnCancel = new Button();
        btnCancel.setText(resourceBundle.getString(BTN_CANCEL));
        btnCancel.setPrefWidth(FORM_BUTTON_WIDTH);
        btnCancel.setCancelButton(true);
        btnCancel.setOnAction(event -> stage.close());

        HBox formButtonsContainer = new HBox();
        formButtonsContainer.setSpacing(HORIZONTAL_SPACE);
        formButtonsContainer.setPadding(MARGINS);
        formButtonsContainer.getChildren().add(btnOk);
        formButtonsContainer.getChildren().add(btnCancel);
        formButtonsContainer.setAlignment(Pos.CENTER);

        return formButtonsContainer;
    }

}
