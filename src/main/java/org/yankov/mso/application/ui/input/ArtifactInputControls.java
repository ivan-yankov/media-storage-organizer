package org.yankov.mso.application.ui.input;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import org.yankov.mso.application.ApplicationContext;
import org.yankov.mso.application.UserInterfaceControls;
import org.yankov.mso.datamodel.generic.Source;

import java.util.ResourceBundle;

public abstract class ArtifactInputControls implements UserInterfaceControls {

    private static final String CLASS_NAME = ArtifactInputControls.class.getName();

    public static final String EXISTING_ARTIFACTS_LABEL = CLASS_NAME + "-existing-artifacts-label";
    public static final String BTN_ADD_ARTIFACT = CLASS_NAME + "-btn-add-artifact";

    private String id;
    private VBox existingArtifactsContainer;
    private Label existingArtifactsLabel;
    private ListView existingArtifacts;
    private Button btnAddArtifact;

    private ObservableList<String> items;

    private final int whiteSpace = 20;

    private final ResourceBundle resourceBundle = ApplicationContext.getInstance().getFolkloreResourceBundle();

    public abstract void refreshExistingArtifacts(ObservableList<String> items);

    public ArtifactInputControls(String id) {
        this.id = id;
        this.existingArtifactsContainer = new VBox();
        this.existingArtifactsLabel = new Label(resourceBundle.getString(EXISTING_ARTIFACTS_LABEL));
        this.existingArtifacts = new ListView();
        this.btnAddArtifact = new Button(resourceBundle.getString(BTN_ADD_ARTIFACT));
        this.items = FXCollections.observableArrayList();
    }

    public String getId() {
        return id;
    }

    public Button getBtnAddArtifact() {
        return btnAddArtifact;
    }

    public ResourceBundle getResourceBundle() {
        return resourceBundle;
    }

    public int getWhiteSpace() {
        return whiteSpace;
    }

    @Override
    public void layout() {
        existingArtifacts.setItems(items);

        existingArtifactsContainer.getChildren().add(existingArtifactsLabel);
        existingArtifactsContainer.getChildren().add(existingArtifacts);
        refreshExistingArtifacts(items);
    }

    @Override
    public Pane getContainer() {
        return existingArtifactsContainer;
    }

}
