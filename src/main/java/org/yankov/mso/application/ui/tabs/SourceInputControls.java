package org.yankov.mso.application.ui.tabs;

import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.util.StringConverter;
import org.yankov.mso.application.ApplicationContext;
import org.yankov.mso.application.ui.controls.FolkloreComboBoxFactory;
import org.yankov.mso.application.ui.controls.LabeledComboBox;
import org.yankov.mso.application.ui.controls.LabeledTextField;
import org.yankov.mso.application.ui.converters.SourceStringConverter;
import org.yankov.mso.datamodel.Source;
import org.yankov.mso.datamodel.SourceType;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

public class SourceInputControls extends ArtifactInputControls<Source> {

    private static final String CLASS_NAME = SourceInputControls.class.getName();

    public static final String SIGNATURE = CLASS_NAME + "-signature";
    public static final String SIGNATURE_UNDEFINED = CLASS_NAME + "-signature-undefined";

    private LabeledComboBox<SourceType> sourceType;
    private LabeledTextField sourceSignature;

    public SourceInputControls(String id) {
        super(id);
    }

    @Override
    protected Pane createControls() {
        sourceType = FolkloreComboBoxFactory.createSourceTypeInput();
        sourceType.layout();

        sourceSignature = new LabeledTextField(getResourceBundle().getString(SIGNATURE), "");
        sourceSignature.layout();

        VBox actionControlsContainer = new VBox();
        actionControlsContainer.setSpacing(getWhiteSpace());
        actionControlsContainer.getChildren().add(sourceType.getContainer());
        actionControlsContainer.getChildren().add(sourceSignature.getContainer());

        return actionControlsContainer;
    }

    @Override
    protected List<Source> getExistingArtifacts() {
        return new ArrayList<>(ApplicationContext.getInstance().getFolkloreEntityCollections().getSources());
    }

    @Override
    protected StringConverter<Source> getStringConverter() {
        return new SourceStringConverter();
    }

    @Override
    protected boolean validateUserInput() {
        if (sourceSignature.getTextField().getText().isEmpty()) {
            ApplicationContext.getInstance().getLogger()
                .log(Level.SEVERE, getResourceBundle().getString(SIGNATURE_UNDEFINED));
            return false;
        }

        return true;
    }

    @Override
    protected Source createArtifact() {
        return new Source();
    }

    @Override
    protected boolean addArtifact(Source artifact) {
        return ApplicationContext.getInstance().getFolkloreEntityCollections().addSource(artifact);
    }

    @Override
    protected void cleanup() {
        sourceSignature.getTextField().setText("");
    }

    @Override
    protected void setArtifactProperties(Source artifact) {
        artifact.setType(sourceType.getValue());
        artifact.setSignature(sourceSignature.getTextField().getText());
    }

    @Override
    protected void extractArtifactProperties(Source artifact) {
        sourceType.setValue(artifact.getType());
        sourceSignature.getTextField().setText(artifact.getSignature());
    }

}
