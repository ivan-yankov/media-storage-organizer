package org.yankov.mso.application.ui.input;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import org.yankov.mso.application.ApplicationContext;
import org.yankov.mso.application.ui.controls.LabeledComboBox;
import org.yankov.mso.application.ui.controls.LabeledTextField;
import org.yankov.mso.application.ui.converters.SourceStringConverter;
import org.yankov.mso.application.ui.converters.SourceTypeStringConverter;
import org.yankov.mso.datamodel.generic.Source;
import org.yankov.mso.datamodel.generic.SourceType;

import java.util.ArrayList;
import java.util.List;

public class SourceTypeInputControls extends ArtifactInputControls {

    private static final String CLASS_NAME = SourceTypeInputControls.class.getName();

    public static final String SOURCE_TYPE = CLASS_NAME + "-source-type";
    public static final String SIGNATURE = CLASS_NAME + "-signature";

    private LabeledComboBox<SourceType> sourceType;
    private LabeledTextField sourceSignature;

    public SourceTypeInputControls(String id) {
        super(id);
    }

    @Override
    public Pane createInputControls() {
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
        actionControlsContainer.getChildren().add(getBtnAddArtifact());

        return actionControlsContainer;
    }

    @Override
    public List<String> collectExistingItems() {
        List<String> items = new ArrayList<>();
        SourceStringConverter converter = new SourceStringConverter();
        ApplicationContext.getInstance().getFolkloreEntityCollections().getSources()
                          .forEach(source -> items.add(converter.toString(source)));
        return items;
    }

    @Override
    public void handleBtnAddArtifactClick(ActionEvent event) {
        String signature = sourceSignature.getTextField().getText();
        if (signature.isEmpty()) {
            return;
        }
        SourceType type = sourceType.getComboBox().getValue();
        Source source = new Source(type, signature);
        ApplicationContext.getInstance().getFolkloreEntityCollections().addSource(source);
    }

}
