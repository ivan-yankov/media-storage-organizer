package org.yankov.mso.application.ui.input;

import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.util.StringConverter;
import org.yankov.mso.application.ApplicationContext;
import org.yankov.mso.application.ui.controls.LabeledTextField;
import org.yankov.mso.application.ui.converters.InstrumentStringConverter;
import org.yankov.mso.datamodel.generic.Instrument;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

public class InstrumentInputControls extends ArtifactInputControls<Instrument> {

    private static final String CLASS_NAME = SourceInputControls.class.getName();

    public static final String INSTRUMENT = CLASS_NAME + "-instrument";
    public static final String INSTRUMENT_NAME_UNDEFINED = CLASS_NAME + "-instrument-name-undefined";

    private LabeledTextField instrumentField;

    public InstrumentInputControls(String id) {
        super(id);
    }

    @Override
    protected Pane createActionsControls() {
        instrumentField = new LabeledTextField(getResourceBundle().getString(INSTRUMENT), "", null);
        instrumentField.layout();

        VBox actionControlsContainer = new VBox();
        actionControlsContainer.getChildren().add(instrumentField.getContainer());

        return actionControlsContainer;
    }

    @Override
    protected List<Instrument> collectExistingArtifacts() {
        return new ArrayList<>(ApplicationContext.getInstance().getFolkloreEntityCollections().getInstruments());
    }

    @Override
    protected StringConverter<Instrument> getStringConverter() {
        return new InstrumentStringConverter();
    }

    @Override
    protected void handleBtnAddArtifactClick(ActionEvent event) {
        String instrumentName = instrumentField.getTextField().getText();
        if (instrumentName.isEmpty()) {
            ApplicationContext.getInstance().getLogger()
                              .log(Level.SEVERE, getResourceBundle().getString(INSTRUMENT_NAME_UNDEFINED));
            return;
        }

        Instrument instrument = new Instrument(instrumentName);
        if (!ApplicationContext.getInstance().getFolkloreEntityCollections().addInstrument(instrument)) {
            ApplicationContext.getInstance().getLogger()
                              .log(Level.INFO, getResourceBundle().getString(ARTIFACT_EXISTS));
        } else {
            instrumentField.getTextField().setText("");
        }
    }

    @Override
    protected void handleExistingArtifactSelected(ObservableValue<? extends Instrument> observable, Instrument oldValue,
                                                  Instrument newValue) {
        if (newValue == null) {
            return;
        }

        instrumentField.getTextField().setText(newValue.getName());
    }

    @Override
    protected void dataModelChanged() {
    }

}
