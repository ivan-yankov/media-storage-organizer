package org.yankov.mso.application.ui.input;

import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
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

public abstract class ArtifactInputControls<T> implements UserInterfaceControls, PropertyChangeListener {

    private static final String CLASS_NAME = ArtifactInputControls.class.getName();

    public static final String EXISTING_ARTIFACTS_LABEL = CLASS_NAME + "-existing-artifacts-label";
    public static final String BTN_ADD_ARTIFACT = CLASS_NAME + "-btn-add-artifact";
    public static final String ARTIFACT_EXISTS = CLASS_NAME + "-artifact-exists";

    private String id;
    private VBox existingArtifactsContainer;
    private Label existingArtifactsLabel;
    private ListView<T> existingArtifacts;
    private Button btnAddArtifact;
    private HBox container;

    private ObservableList<T> existingItems;

    private final int whiteSpace = 20;

    private final ResourceBundle resourceBundle = ApplicationContext.getInstance().getFolkloreResourceBundle();

    protected abstract Pane createActionsControls();

    protected abstract List<T> collectExistingArtifacts();

    protected abstract StringConverter<T> getStringConverter();

    protected abstract void handleBtnAddArtifactClick(ActionEvent event);

    protected abstract void handleExistingArtifactSelected(ObservableValue<? extends T> observable, T oldValue,
                                                           T newValue);

    protected abstract void dataModelChanged();

    public ArtifactInputControls(String id) {
        this.id = id;
        this.existingArtifactsContainer = new VBox();
        this.existingArtifactsLabel = new Label(resourceBundle.getString(EXISTING_ARTIFACTS_LABEL));
        this.existingArtifacts = new ListView<>();
        this.btnAddArtifact = new Button(resourceBundle.getString(BTN_ADD_ARTIFACT));
        this.existingItems = FXCollections.observableArrayList();
        this.container = new HBox();
    }

    public String getId() {
        return id;
    }

    protected ResourceBundle getResourceBundle() {
        return resourceBundle;
    }

    protected int getWhiteSpace() {
        return whiteSpace;
    }

    @Override
    public void layout() {
        ApplicationContext.getInstance().getFolkloreEntityCollections().addPropertyChangeListener(this);

        btnAddArtifact.setOnAction(this::handleBtnAddArtifactClick);

        existingArtifacts.setItems(existingItems);
        existingArtifacts.getSelectionModel().selectedItemProperty().addListener(this::handleExistingArtifactSelected);
        existingArtifacts.setCellFactory(listView -> {
            TextFieldListCell<T> cell = new TextFieldListCell<>();
            cell.setConverter(getStringConverter());
            return cell;
        });

        existingArtifactsContainer.getChildren().add(existingArtifactsLabel);
        existingArtifactsContainer.getChildren().add(existingArtifacts);
        refreshExistingArtifacts();

        VBox actionControlsContainer = new VBox();
        actionControlsContainer.getChildren().add(createActionsControls());

        VBox btnAddArtifactContainer = new VBox();
        btnAddArtifactContainer.setPadding(new Insets(getWhiteSpace(), 0, getWhiteSpace(), 0));
        btnAddArtifactContainer.getChildren().add(btnAddArtifact);
        actionControlsContainer.getChildren().add(btnAddArtifactContainer);

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

    private void refreshExistingArtifacts() {
        existingItems.clear();
        List<T> newArtifacts = collectExistingArtifacts();
        newArtifacts.sort((a1, a2) -> getStringConverter().toString(a1)
                                                          .compareToIgnoreCase(getStringConverter().toString(a2)));
        existingItems.addAll(newArtifacts);
    }

}
