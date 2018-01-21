package org.yankov.mso.application.ui.input;

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

public class InstrumentInputControls extends ArtifactInputControls {

    private static final String CLASS_NAME = SourceInputControls.class.getName();

    public static final String INSTRUMENT = CLASS_NAME + "-instrument";

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
    protected List<String> collectExistingItems() {
        List<String> items = new ArrayList<>();
        StringConverter converter = new InstrumentStringConverter();
        ApplicationContext.getInstance().getFolkloreEntityCollections().getInstruments()
                          .forEach(instrument -> items.add(converter.toString(instrument)));
        return items;
    }

    @Override
    protected void handleBtnAddArtifactClick(ActionEvent event) {
        String instrumentName = instrumentField.getTextField().getText();
        if (instrumentName.isEmpty()) {
            return;
        }
        Instrument instrument = new Instrument(instrumentName);
        ApplicationContext.getInstance().getFolkloreEntityCollections().addInstrument(instrument);
    }

}
