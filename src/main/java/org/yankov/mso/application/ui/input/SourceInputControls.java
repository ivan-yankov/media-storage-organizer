package org.yankov.mso.application.ui.input;

import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.util.StringConverter;
import org.yankov.mso.application.ApplicationContext;
import org.yankov.mso.application.ui.controls.LabeledComboBox;
import org.yankov.mso.application.ui.controls.LabeledTextField;
import org.yankov.mso.application.ui.converters.SourceStringConverter;
import org.yankov.mso.application.ui.converters.SourceTypeStringConverter;
import org.yankov.mso.datamodel.generic.Source;
import org.yankov.mso.datamodel.generic.SourceType;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

public class SourceInputControls extends ArtifactInputControls<Source> {

    private static final String CLASS_NAME = SourceInputControls.class.getName();

    public static final String SOURCE_TYPE = CLASS_NAME + "-source-type";
    public static final String SIGNATURE = CLASS_NAME + "-signature";
    public static final String SIGNATURE_UNDEFINED = CLASS_NAME + "-signature-undefined";

    private LabeledComboBox<SourceType> sourceType;
    private LabeledTextField sourceSignature;

    public SourceInputControls(String id) {
        super(id);
    }

    @Override
    public Pane createActionsControls() {
        ObservableList<SourceType> sourceTypes = FXCollections.observableArrayList();
        sourceTypes.addAll(ApplicationContext.getInstance().getFolkloreEntityCollections().getSourceTypes());
        SourceType defaultSourceType = sourceTypes.get(0);
        sourceType = new LabeledComboBox<>(getResourceBundle().getString(SOURCE_TYPE), sourceTypes, defaultSourceType,
                                           null, new SourceTypeStringConverter(), false);
        sourceType.layout();

        sourceSignature = new LabeledTextField(getResourceBundle().getString(SIGNATURE), "", null);
        sourceSignature.layout();

        VBox actionControlsContainer = new VBox();
        actionControlsContainer.setSpacing(getWhiteSpace());
        actionControlsContainer.getChildren().add(sourceType.getContainer());
        actionControlsContainer.getChildren().add(sourceSignature.getContainer());

        return actionControlsContainer;
    }

    @Override
    public List<Source> collectExistingArtifacts() {
        return new ArrayList<>(ApplicationContext.getInstance().getFolkloreEntityCollections().getSources());
    }

    @Override
    protected StringConverter<Source> getStringConverter() {
        return new SourceStringConverter();
    }

    @Override
    public void handleBtnAddArtifactClick(ActionEvent event) {
        String signature = sourceSignature.getTextField().getText();
        if (signature.isEmpty()) {
            ApplicationContext.getInstance().getLogger()
                              .log(Level.SEVERE, getResourceBundle().getString(SIGNATURE_UNDEFINED));
            return;
        }

        SourceType type = sourceType.getComboBox().getValue();

        Source source = new Source(type, signature);
        if (!ApplicationContext.getInstance().getFolkloreEntityCollections().addSource(source)) {
            ApplicationContext.getInstance().getLogger()
                              .log(Level.INFO, getResourceBundle().getString(ARTIFACT_EXISTS));
        } else {
            sourceSignature.getTextField().setText("");
        }
    }

    @Override
    protected void handleExistingArtifactSelected(ObservableValue<? extends Source> observable, Source oldValue,
                                                  Source newValue) {
        if (newValue == null) {
            return;
        }

        sourceType.getComboBox().setValue(newValue.getType());
        sourceSignature.getTextField().setText(newValue.getSignature());
    }

    @Override
    protected void dataModelChanged() {
    }

}
