package org.yankov.mso.application.ui.input;

import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.util.StringConverter;
import org.yankov.mso.application.ApplicationContext;
import org.yankov.mso.application.ui.controls.LabeledTextField;
import org.yankov.mso.application.ui.converters.InstrumentStringConverter;
import org.yankov.mso.datamodel.generic.Instrument;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.logging.Level;

public class InstrumentInputControls extends ArtifactInputControls<Instrument> {

    private static final String CLASS_NAME = SourceInputControls.class.getName();

    public static final String INSTRUMENT = CLASS_NAME + "-instrument";
    public static final String INSTRUMENT_NAME_UNDEFINED = CLASS_NAME + "-instrument-name-undefined";

    private LabeledTextField instrument;

    public InstrumentInputControls(String id) {
        super(id);
    }

    @Override
    protected Pane createControls() {
        instrument = new LabeledTextField(getResourceBundle().getString(INSTRUMENT), "", null);
        instrument.layout();

        VBox actionControlsContainer = new VBox();
        actionControlsContainer.getChildren().add(instrument.getContainer());

        return actionControlsContainer;
    }

    @Override
    protected List<Instrument> getExistingArtifacts() {
        return new ArrayList<>(ApplicationContext.getInstance().getFolkloreEntityCollections().getInstruments());
    }

    @Override
    protected StringConverter<Instrument> getStringConverter() {
        return new InstrumentStringConverter();
    }

    @Override
    protected boolean validateUserInput() {
        if (instrument.getTextField().getText().isEmpty()) {
            ApplicationContext.getInstance().getLogger()
                              .log(Level.SEVERE, getResourceBundle().getString(INSTRUMENT_NAME_UNDEFINED));
            return false;
        }

        return true;
    }

    @Override
    protected Instrument createArtifact() {
        return new Instrument();
    }

    @Override
    protected boolean addArtifact(Instrument artifact) {
        return ApplicationContext.getInstance().getFolkloreEntityCollections().addInstrument(artifact);
    }

    @Override
    protected void cleanup() {
        instrument.getTextField().setText("");
    }

    @Override
    protected void setArtifactProperties(Instrument artifact) {
        artifact.setName(instrument.getTextField().getText());
    }

    @Override
    protected void extractArtifactProperties(Instrument artifact) {
        instrument.getTextField().setText(artifact.getName());
    }

    @Override
    protected void dataModelChanged() {
    }

}
