package org.yankov.mso.application.ui.input;

import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.util.StringConverter;
import org.yankov.mso.application.ApplicationContext;
import org.yankov.mso.application.ui.controls.LabeledTextField;
import org.yankov.mso.application.ui.converters.EthnographicRegionStringConverter;
import org.yankov.mso.datamodel.folklore.EthnographicRegion;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

public class EthnographicRegionInputControls extends ArtifactInputControls<EthnographicRegion> {

    private static final String CLASS_NAME = EthnographicRegionInputControls.class.getName();

    public static final String NAME = CLASS_NAME + "-name";
    public static final String NAME_UNDEFINED = CLASS_NAME + "-name-undefined";

    private LabeledTextField ethnographicRegion;

    public EthnographicRegionInputControls(String id) {
        super(id);
    }

    @Override
    protected Pane createControls() {
        ethnographicRegion = new LabeledTextField(getResourceBundle().getString(NAME), "", null);
        ethnographicRegion.layout();

        VBox actionControlsContainer = new VBox();
        actionControlsContainer.setSpacing(getWhiteSpace());
        actionControlsContainer.getChildren().add(ethnographicRegion.getContainer());

        return actionControlsContainer;
    }

    @Override
    protected List<EthnographicRegion> getExistingArtifacts() {
        return new ArrayList<>(
                ApplicationContext.getInstance().getFolkloreEntityCollections().getEthnographicRegions());
    }

    @Override
    protected StringConverter<EthnographicRegion> getStringConverter() {
        return new EthnographicRegionStringConverter();
    }

    @Override
    protected boolean validateUserInput() {
        if (ethnographicRegion.getTextField().getText().isEmpty()) {
            ApplicationContext.getInstance().getLogger()
                              .log(Level.SEVERE, getResourceBundle().getString(NAME_UNDEFINED));
            return false;
        }

        return true;
    }

    @Override
    protected EthnographicRegion createArtifact() {
        return new EthnographicRegion();

    }

    @Override
    protected boolean addArtifact(EthnographicRegion artifact) {
        return ApplicationContext.getInstance().getFolkloreEntityCollections().addEthnographicRegion(artifact);
    }

    @Override
    protected void cleanup() {
        ethnographicRegion.getTextField().setText("");
    }

    @Override
    protected void setArtifactProperties(EthnographicRegion artifact) {
        artifact.setName(ethnographicRegion.getTextField().getText());
    }

    @Override
    protected void extractArtifactProperties(EthnographicRegion artifact) {
        ethnographicRegion.getTextField().setText(artifact.getName());
    }

    @Override
    protected void dataModelChanged() {
    }

}
