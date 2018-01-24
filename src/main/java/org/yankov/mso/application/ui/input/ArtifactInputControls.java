package org.yankov.mso.application.ui.input;

import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.cell.TextFieldListCell;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.util.StringConverter;
import org.yankov.mso.application.ApplicationContext;
import org.yankov.mso.application.UserInterfaceControls;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;

public abstract class ArtifactInputControls<T> implements UserInterfaceControls, PropertyChangeListener {

    private static final String CLASS_NAME = ArtifactInputControls.class.getName();

    public static final String EXISTING_ARTIFACTS_LABEL = CLASS_NAME + "-existing-artifacts-label";
    public static final String BTN_ADD_ARTIFACT = CLASS_NAME + "-btn-save-artifact";
    public static final String BTN_SAVE_ARTIFACT = CLASS_NAME + "-btn-add-artifact";
    public static final String ARTIFACT_EXISTS = CLASS_NAME + "-artifact-exists";

    private String id;
    private VBox existingArtifactsContainer;
    private Label existingArtifactsLabel;
    private ListView<T> existingArtifacts;
    private Button btnAddArtifact;
    private Button btnSaveArtifact;
    private HBox container;

    private T selectedArtifact;
    private ObservableList<T> existingItems;

    private final double whiteSpace = 20.0;

    private final ResourceBundle resourceBundle = ApplicationContext.getInstance().getFolkloreResourceBundle();

    protected abstract Pane createControls();

    protected abstract List<T> collectExistingArtifacts();

    protected abstract StringConverter<T> getStringConverter();

    protected abstract boolean validateUserInput();

    protected abstract boolean addNewArtifact();

    protected abstract void cleanup();

    protected abstract void setArtifactProperties(T artifact);

    protected abstract void extractArtifactProperties(T artifact);

    protected abstract void dataModelChanged();

    public ArtifactInputControls(String id) {
        this.id = id;
        this.existingArtifactsContainer = new VBox();
        this.existingArtifactsLabel = new Label(resourceBundle.getString(EXISTING_ARTIFACTS_LABEL));
        this.existingArtifacts = new ListView<>();
        this.btnAddArtifact = new Button(resourceBundle.getString(BTN_ADD_ARTIFACT));
        this.btnSaveArtifact = new Button(resourceBundle.getString(BTN_SAVE_ARTIFACT));
        this.existingItems = FXCollections.observableArrayList();
        this.container = new HBox();
    }

    public String getId() {
        return id;
    }

    protected ResourceBundle getResourceBundle() {
        return resourceBundle;
    }

    protected double getWhiteSpace() {
        return whiteSpace;
    }

    @Override
    public void layout() {
        ApplicationContext.getInstance().getFolkloreEntityCollections().addPropertyChangeListener(this);

        btnAddArtifact.setOnAction(this::handleBtnAddArtifactClick);
        btnSaveArtifact.setOnAction(this::handleBtnSaveArtifactClick);

        existingArtifacts.setItems(existingItems);
        existingArtifacts.getSelectionModel().selectedItemProperty().addListener(this::handleExistingArtifactSelected);
        existingArtifacts.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        existingArtifacts.setCellFactory(listView -> {
            TextFieldListCell<T> cell = new TextFieldListCell<>();
            cell.setConverter(getStringConverter());
            return cell;
        });

        existingArtifactsContainer.getChildren().add(existingArtifactsLabel);
        existingArtifactsContainer.getChildren().add(existingArtifacts);
        refreshExistingArtifacts();

        HBox btnContainer = new HBox();
        btnContainer.setPadding(new Insets(getWhiteSpace(), 0, getWhiteSpace(), 0));
        btnContainer.setSpacing(getWhiteSpace());
        btnContainer.getChildren().add(btnAddArtifact);
        btnContainer.getChildren().add(btnSaveArtifact);

        VBox actionControlsContainer = new VBox();
        actionControlsContainer.getChildren().add(createControls());
        actionControlsContainer.getChildren().add(btnContainer);

        container.setId(getId());
        container.setPadding(new Insets(getWhiteSpace(), 0, getWhiteSpace(), 0));
        container.setSpacing(getWhiteSpace());
        container.getChildren().add(existingArtifactsContainer);
        container.getChildren().add(actionControlsContainer);
    }

    @Override
    public Pane getContainer() {
        return container;
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        refreshExistingArtifacts();
        dataModelChanged();
    }

    private void handleBtnAddArtifactClick(ActionEvent event) {
        if (!validateUserInput()) {
            return;
        }

        if (addNewArtifact()) {
            cleanup();
            selectedArtifact = null;
        } else {
            ApplicationContext.getInstance().getLogger()
                              .log(Level.INFO, getResourceBundle().getString(ARTIFACT_EXISTS));
        }
    }

    private void handleBtnSaveArtifactClick(ActionEvent event) {
        if (selectedArtifact != null && validateUserInput()) {
            setArtifactProperties(selectedArtifact);
            refreshExistingArtifacts();
        }
    }

    private void handleExistingArtifactSelected(ObservableValue<? extends T> observable, T oldValue,
                                                           T newValue) {
        if (newValue == null) {
            return;
        }

        selectedArtifact = newValue;
        extractArtifactProperties(selectedArtifact);
    }

    private void refreshExistingArtifacts() {
        existingItems.clear();
        List<T> newArtifacts = collectExistingArtifacts();
        newArtifacts.sort((a1, a2) -> getStringConverter().toString(a1)
                                                          .compareToIgnoreCase(getStringConverter().toString(a2)));
        existingItems.addAll(newArtifacts);
    }

}
