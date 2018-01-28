package org.yankov.mso.application.ui.input;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import org.yankov.mso.application.ApplicationContext;
import org.yankov.mso.application.UserInterfaceControls;
import org.yankov.mso.application.ui.controls.LabeledComboBox;
import org.yankov.mso.application.ui.converters.StringStringConverter;

import java.util.*;

public class FolkloreInputArtifactsTab implements UserInterfaceControls {

    private static final String CLASS_NAME = FolkloreInputArtifactsTab.class.getName();

    private static final int WHITE_SPACE = 20;
    private static final Map<String, String> ARTIFACT_IDS;
    private static final ResourceBundle resourceBundle = ApplicationContext.getInstance().getFolkloreResourceBundle();

    public static final String ARTIFACT_TYPE = CLASS_NAME + "-artifact-type";
    public static final String SOURCE = CLASS_NAME + "-source";
    public static final String INSTRUMENT = CLASS_NAME + "-instrument";
    public static final String ARTIST = CLASS_NAME + "-artist";
    public static final String ALBUM = CLASS_NAME + "-album";
    public static final String ETHNOGRAPHIC_REGION = CLASS_NAME + "-ethnographic-region";

    private VBox container;
    private StackPane artifactsContainer;

    static {
        ARTIFACT_IDS = new HashMap<>();
        ARTIFACT_IDS.put(resourceBundle.getString(SOURCE), SOURCE);
        ARTIFACT_IDS.put(resourceBundle.getString(INSTRUMENT), INSTRUMENT);
        ARTIFACT_IDS.put(resourceBundle.getString(ARTIST), ARTIST);
        ARTIFACT_IDS.put(resourceBundle.getString(ALBUM), ALBUM);
        ARTIFACT_IDS.put(resourceBundle.getString(ETHNOGRAPHIC_REGION), ETHNOGRAPHIC_REGION);
    }

    public FolkloreInputArtifactsTab() {
        this.container = new VBox();
        this.artifactsContainer = new StackPane();
    }

    @Override
    public void layout() {
        ObservableList<String> artifacts = createArtifacts();
        String defaultArtifact = artifacts.get(0);

        UserInterfaceControls artifactType = new LabeledComboBox<>(resourceBundle.getString(ARTIFACT_TYPE), artifacts,
                                                                   defaultArtifact, this::artifactTypeSelected,
                                                                   new StringStringConverter(), false);
        artifactType.layout();

        List<UserInterfaceControls> inputControls = createInputControls();
        inputControls.forEach(controls -> {
            controls.layout();
            artifactsContainer.getChildren().add(controls.getContainer());
        });

        container.setPadding(new Insets(WHITE_SPACE));
        container.getChildren().add(artifactType.getContainer());
        container.getChildren().add(artifactsContainer);

        refreshArtifactContainer(defaultArtifact);
    }

    @Override
    public Pane getContainer() {
        return container;
    }

    private ObservableList<String> createArtifacts() {
        ObservableList<String> artifacts = FXCollections.observableArrayList();

        artifacts.add(resourceBundle.getString(SOURCE));
        artifacts.add(resourceBundle.getString(INSTRUMENT));
        artifacts.add(resourceBundle.getString(ARTIST));
        artifacts.add(resourceBundle.getString(ALBUM));
        artifacts.add(resourceBundle.getString(ETHNOGRAPHIC_REGION));

        return artifacts.sorted();
    }

    private void artifactTypeSelected(String type) {
        refreshArtifactContainer(type);
    }

    private void refreshArtifactContainer(String type) {
        artifactsContainer.getChildren().forEach(node -> node.setVisible(node.getId().equals(ARTIFACT_IDS.get(type))));
    }

    private List<UserInterfaceControls> createInputControls() {
        List<UserInterfaceControls> inputControls = new ArrayList<>();

        inputControls.add(new SourceInputControls(SOURCE));
        inputControls.add(new InstrumentInputControls(INSTRUMENT));
        inputControls.add(new ArtistInputControls(ARTIST));
        inputControls.add(new AlbumInputControls(ALBUM));
        inputControls.add(new EthnographicRegionInputControls(ETHNOGRAPHIC_REGION));

        return inputControls;
    }

}