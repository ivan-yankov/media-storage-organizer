package org.yankov.mso.application.ui.input;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import org.yankov.mso.application.ApplicationContext;
import org.yankov.mso.application.UserInterfaceControls;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Collections;
import java.util.List;
import java.util.ResourceBundle;

public abstract class ArtifactInputControls implements UserInterfaceControls, PropertyChangeListener {

    private static final String CLASS_NAME = ArtifactInputControls.class.getName();

    public static final String EXISTING_ARTIFACTS_LABEL = CLASS_NAME + "-existing-artifacts-label";
    public static final String BTN_ADD_ARTIFACT = CLASS_NAME + "-btn-add-artifact";

    private String id;
    private VBox existingArtifactsContainer;
    private Label existingArtifactsLabel;
    private ListView existingArtifacts;
    private Button btnAddArtifact;
    private HBox container;

    private ObservableList<String> items;

    private final int whiteSpace = 20;

    private final ResourceBundle resourceBundle = ApplicationContext.getInstance().getFolkloreResourceBundle();

    protected abstract Pane createInputControls();

    protected abstract List<String> collectExistingItems();

    protected abstract void handleBtnAddArtifactClick(ActionEvent event);

    public ArtifactInputControls(String id) {
        this.id = id;
        this.existingArtifactsContainer = new VBox();
        this.existingArtifactsLabel = new Label(resourceBundle.getString(EXISTING_ARTIFACTS_LABEL));
        this.existingArtifacts = new ListView();
        this.btnAddArtifact = new Button(resourceBundle.getString(BTN_ADD_ARTIFACT));
        this.items = FXCollections.observableArrayList();
        this.container = new HBox();
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
        ApplicationContext.getInstance().getFolkloreEntityCollections().addPropertyChangeListener(this);

        btnAddArtifact.setOnAction(this::handleBtnAddArtifactClick);

        existingArtifacts.setItems(items);
        existingArtifactsContainer.getChildren().add(existingArtifactsLabel);
        existingArtifactsContainer.getChildren().add(existingArtifacts);
        refreshExistingArtifacts();

        container.setId(getId());
        container.setPadding(new Insets(getWhiteSpace(), 0, getWhiteSpace(), 0));
        container.setSpacing(getWhiteSpace());
        container.getChildren().add(existingArtifactsContainer);
        container.getChildren().add(createInputControls());
    }

    @Override
    public Pane getContainer() {
        return container;
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        refreshExistingArtifacts();
    }

    private void refreshExistingArtifacts() {
        items.clear();
        List<String> newItems = collectExistingItems();
        Collections.sort(newItems);
        items.addAll(newItems);
    }

}
