package org.yankov.mso.application.ui.edit;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.yankov.mso.application.ApplicationContext;
import org.yankov.mso.application.Form;
import org.yankov.mso.application.UserInterfaceControls;
import org.yankov.mso.application.ui.datamodel.FolklorePieceProperties;
import org.yankov.mso.application.ui.input.FolkloreInputTable;
import org.yankov.mso.datamodel.folklore.EthnographicRegion;
import org.yankov.mso.datamodel.generic.Artist;
import org.yankov.mso.datamodel.generic.Source;

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
    private FolklorePieceProperties piece;

    public FolklorePieceEditor(FolklorePieceProperties piece) {
        this.stage = new Stage();
        this.piece = piece;
    }

    @Override
    public void createControls() {
        UserInterfaceControls title = new LabeledTextField(resourceBundle.getString(FolkloreInputTable.COL_TITLE));
        title.layout();

        UserInterfaceControls album = new LabeledTextField(resourceBundle.getString(FolkloreInputTable.COL_ALBUM));
        album.layout();

        UserInterfaceControls performer = new LabeledComboBox<Artist>(
                resourceBundle.getString(FolkloreInputTable.COL_PERFORMER));
        performer.layout();

        UserInterfaceControls accompanimentPerformer = new LabeledComboBox<Artist>(
                resourceBundle.getString(FolkloreInputTable.COL_ACCOMPANIMENT_PERFORMER));
        accompanimentPerformer.layout();

        UserInterfaceControls arrangementAuthor = new LabeledComboBox<Artist>(
                resourceBundle.getString(FolkloreInputTable.COL_ARRANGEMENT_AUTHOR));
        arrangementAuthor.layout();

        UserInterfaceControls conductor = new LabeledComboBox<Artist>(
                resourceBundle.getString(FolkloreInputTable.COL_CONDUCTOR));
        conductor.layout();

        UserInterfaceControls author = new LabeledComboBox<Artist>(
                resourceBundle.getString(FolkloreInputTable.COL_AUTHOR));
        author.layout();

        UserInterfaceControls soloist = new LabeledComboBox<Artist>(
                resourceBundle.getString(FolkloreInputTable.COL_SOLOIST));
        soloist.layout();

        UserInterfaceControls ethnographicRegion = new LabeledComboBox<EthnographicRegion>(
                resourceBundle.getString(FolkloreInputTable.COL_ETHNOGRAPHIC_REGION));
        ethnographicRegion.layout();

        UserInterfaceControls source = new LabeledComboBox<Source>(
                resourceBundle.getString(FolkloreInputTable.COL_SOURCE));
        source.layout();

        UserInterfaceControls note = new LabeledTextField(resourceBundle.getString(FolkloreInputTable.COL_NOTE));
        note.layout();

        UserInterfaceControls fileSelectionField = new FileSelectionField(
                resourceBundle.getString(FolkloreInputTable.COL_FILE));
        fileSelectionField.layout();

        GridPane gridPane = new GridPane();
        gridPane.setPadding(MARGINS);
        gridPane.setAlignment(Pos.BASELINE_LEFT);
        gridPane.setHgap(HORIZONTAL_SPACE);
        gridPane.setVgap(VERTICAL_SPACE);

        gridPane.add(title.getContainer(), 1, 0, 2, 1);
        gridPane.add(album.getContainer(), 0, 0);
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
        stage.show();
    }

    private void initializeStage() {
        ApplicationContext.getInstance().getApplicationSettings().getIcon().ifPresent(stage.getIcons()::add);
        stage.initOwner(ApplicationContext.getInstance().getPrimaryStage());
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setTitle(resourceBundle.getString(STAGE_TITLE) + String.format(" %2d", piece.getAlbumTrackOrder()));
    }

    private HBox createFormButtons() {
        Button btnOk = new Button();
        btnOk.setText(resourceBundle.getString(BTN_OK));
        btnOk.setPrefWidth(FORM_BUTTON_WIDTH);
        btnOk.setDefaultButton(true);

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
