package org.yankov.mso.application.ui.input;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import org.yankov.mso.application.ApplicationContext;
import org.yankov.mso.application.UserInterfaceControls;
import org.yankov.mso.application.ui.controls.LabeledComboBox;
import org.yankov.mso.application.ui.controls.LabeledTextField;
import org.yankov.mso.application.ui.converters.SourceStringConverter;
import org.yankov.mso.application.ui.converters.SourceTypeStringConverter;
import org.yankov.mso.datamodel.generic.SourceType;

public class SourceTypeInputControls extends ArtifactInputControls {

    private static final String CLASS_NAME = SourceTypeInputControls.class.getName();

    public static final String SOURCE_TYPE = CLASS_NAME + "-source-type";
    public static final String SIGNATURE = CLASS_NAME + "-signature";

    private HBox container;

    public SourceTypeInputControls(String id) {
        super(id);
        this.container = new HBox();
    }

    @Override
    public void layout() {
        super.layout();

        container.setId(getId());
        container.setPadding(new Insets(getWhiteSpace(), 0, getWhiteSpace(), 0));
        container.setSpacing(getWhiteSpace());
        container.getChildren().add(super.getContainer());

        ObservableList<SourceType> sourceTypes = FXCollections.observableArrayList();
        sourceTypes.addAll(ApplicationContext.getInstance().getFolkloreEntityCollections().getSourceTypes());
        SourceType defaultSourceType = sourceTypes.get(0);
        UserInterfaceControls sourceType = new LabeledComboBox<>(getResourceBundle().getString(SOURCE_TYPE),
                                                                 sourceTypes, defaultSourceType, null,
                                                                 new SourceTypeStringConverter(), false);
        sourceType.layout();

        UserInterfaceControls sourceSignature = new LabeledTextField(getResourceBundle().getString(SIGNATURE), "",
                                                                     null);
        sourceSignature.layout();

        VBox actionControlsContainer = new VBox();
        actionControlsContainer.setSpacing(getWhiteSpace());
        actionControlsContainer.getChildren().add(sourceType.getContainer());
        actionControlsContainer.getChildren().add(sourceSignature.getContainer());
        actionControlsContainer.getChildren().add(getBtnAddArtifact());

        container.getChildren().add(actionControlsContainer);
    }

    @Override
    public Pane getContainer() {
        return container;
    }

    @Override
    public void refreshExistingArtifacts(ObservableList<String> items) {
        items.clear();
        SourceStringConverter converter = new SourceStringConverter();
        ApplicationContext.getInstance().getFolkloreEntityCollections().getSources()
                          .forEach(source -> items.add(converter.toString(source)));
    }

}
