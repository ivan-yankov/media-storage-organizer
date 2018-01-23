package org.yankov.mso.application.ui.input;

import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.util.StringConverter;
import org.yankov.mso.application.ApplicationContext;
import org.yankov.mso.application.ui.controls.LabeledComboBox;
import org.yankov.mso.application.ui.controls.LabeledTextField;
import org.yankov.mso.application.ui.converters.ArtistStringConverter;
import org.yankov.mso.application.ui.converters.InstrumentStringConverter;
import org.yankov.mso.datamodel.generic.Artist;
import org.yankov.mso.datamodel.generic.ArtistMission;
import org.yankov.mso.datamodel.generic.Instrument;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.stream.Collectors;

public class ArtistInputControls extends ArtifactInputControls<Artist> {

    private static final String CLASS_NAME = ArtistInputControls.class.getName();

    public static final String NAME = CLASS_NAME + "-name";
    public static final String NOTE = CLASS_NAME + "-note";
    public static final String MISSIONS = CLASS_NAME + "-missions";
    public static final String SINGER = CLASS_NAME + "-singer";
    public static final String INSTRUMENT_PLAYER = CLASS_NAME + "-instrument-player";
    public static final String COMPOSER = CLASS_NAME + "-composer";
    public static final String CONDUCTOR = CLASS_NAME + "-conductor";
    public static final String ORCHESTRA = CLASS_NAME + "-orchestra";
    public static final String CHOIR = CLASS_NAME + "-choir";
    public static final String ENSEMBLE = CLASS_NAME + "-ensemble";
    public static final String CHAMBER_GROUP = CLASS_NAME + "-chamber-group";
    public static final String ARTIST_NAME_UNDEFINED = CLASS_NAME + "-artist-name-undefined";
    public static final String ARTIST_INSTRUMENT_UNDEFINED = CLASS_NAME + "-artist-instrument-undefined";
    public static final String NO_ARTIST_MISSION_SELECTED = CLASS_NAME + "-no-artist-mission-selected";
    public static final String INSTRUMENT = CLASS_NAME + "-instrument";

    private LabeledTextField nameField;
    private LabeledTextField noteField;
    private LabeledComboBox<Instrument> instrument;
    private List<CheckBox> missions;
    private ObservableList<Instrument> observableInstruments;

    private final Map<ArtistMission, String> missionLabels;

    public ArtistInputControls(String id) {
        super(id);

        this.missionLabels = new HashMap<>();
        this.missionLabels.put(ArtistMission.SINGER, getResourceBundle().getString(SINGER));
        this.missionLabels.put(ArtistMission.INSTRUMENT_PLAYER, getResourceBundle().getString(INSTRUMENT_PLAYER));
        this.missionLabels.put(ArtistMission.COMPOSER, getResourceBundle().getString(COMPOSER));
        this.missionLabels.put(ArtistMission.CONDUCTOR, getResourceBundle().getString(CONDUCTOR));
        this.missionLabels.put(ArtistMission.ORCHESTRA, getResourceBundle().getString(ORCHESTRA));
        this.missionLabels.put(ArtistMission.CHOIR, getResourceBundle().getString(CHOIR));
        this.missionLabels.put(ArtistMission.ENSEMBLE, getResourceBundle().getString(ENSEMBLE));
        this.missionLabels.put(ArtistMission.CHAMBER_GROUP, getResourceBundle().getString(CHAMBER_GROUP));

        this.observableInstruments = FXCollections.observableArrayList();
    }

    @Override
    protected Pane createActionsControls() {
        nameField = new LabeledTextField(getResourceBundle().getString(NAME), "", null);
        nameField.layout();

        noteField = new LabeledTextField(getResourceBundle().getString(NOTE), "", null);
        noteField.layout();

        HBox missionsContainer = new HBox();
        missionsContainer.setSpacing(getWhiteSpace());
        missions = createMissions();
        missions.forEach(mission -> missionsContainer.getChildren().add(mission));

        instrument = createInstrumentControl();
        instrument.layout();
        enableInstrument();

        VBox actionControlsContainer = new VBox();
        actionControlsContainer.setSpacing(getWhiteSpace());
        actionControlsContainer.getChildren().add(nameField.getContainer());
        actionControlsContainer.getChildren().add(noteField.getContainer());
        TitledPane missionsPane = new TitledPane(getResourceBundle().getString(MISSIONS), missionsContainer);
        missionsPane.setCollapsible(false);
        actionControlsContainer.getChildren().add(missionsPane);
        actionControlsContainer.getChildren().add(instrument.getContainer());

        return actionControlsContainer;
    }

    @Override
    protected List<Artist> collectExistingArtifacts() {
        return new ArrayList<>(ApplicationContext.getInstance().getFolkloreEntityCollections().getArtists());
    }

    @Override
    protected StringConverter<Artist> getStringConverter() {
        return new ArtistStringConverter();
    }

    @Override
    protected void handleBtnAddArtifactClick(ActionEvent event) {
        String name = nameField.getTextField().getText();
        if (name.isEmpty()) {
            ApplicationContext.getInstance().getLogger()
                              .log(Level.SEVERE, getResourceBundle().getString(ARTIST_NAME_UNDEFINED));
            return;
        }

        if (getSelectedMissions().isEmpty()) {
            ApplicationContext.getInstance().getLogger()
                              .log(Level.SEVERE, getResourceBundle().getString(NO_ARTIST_MISSION_SELECTED));
            return;
        }

        if (getSelectedMissions().contains(ArtistMission.INSTRUMENT_PLAYER) && instrument.getComboBox()
                                                                                         .getValue() == null) {
            ApplicationContext.getInstance().getLogger()
                              .log(Level.SEVERE, getResourceBundle().getString(ARTIST_INSTRUMENT_UNDEFINED));
            return;
        }

        Artist artist = new Artist();
        artist.setName(name);
        artist.setNote(noteField.getTextField().getText());

        if (getSelectedMissions().contains(ArtistMission.INSTRUMENT_PLAYER)) {
            artist.setInstrument(instrument.getComboBox().getValue());
        }

        getSelectedMissions().forEach(artist::addMission);

        if (!ApplicationContext.getInstance().getFolkloreEntityCollections().addArtist(artist)) {
            ApplicationContext.getInstance().getLogger()
                              .log(Level.INFO, getResourceBundle().getString(ARTIFACT_EXISTS));
        } else {
            nameField.getTextField().setText("");
            noteField.getTextField().setText("");
            missions.forEach(mission -> mission.setSelected(false));
            instrument.getComboBox().setValue(null);
            enableInstrument();
        }
    }

    @Override
    protected void handleExistingArtifactSelected(ObservableValue<? extends Artist> observable, Artist oldValue,
                                                  Artist newValue) {
        if (newValue == null) {
            return;
        }

        nameField.getTextField().setText(newValue.getName());
        noteField.getTextField().setText(newValue.getNote());
        for (CheckBox mission : missions) {
            ArtistMission artistMission = (ArtistMission) mission.getUserData();
            mission.setSelected(newValue.getMissions().contains(artistMission));
        }
        instrument.getComboBox().setValue(newValue.getInstrument());
        enableInstrument();
    }

    @Override
    protected void dataModelChanged() {
        refreshInstruments();
    }

    private List<CheckBox> createMissions() {
        List<CheckBox> missions = new ArrayList<>();

        for (ArtistMission mission : missionLabels.keySet()) {
            CheckBox cb = new CheckBox();
            cb.setText(missionLabels.get(mission));
            cb.setUserData(mission);
            cb.setOnAction(this::missionSelected);
            missions.add(cb);
        }

        missions.sort((cb1, cb2) -> cb1.getText().compareToIgnoreCase(cb2.getText()));

        return missions;
    }

    private void missionSelected(ActionEvent event) {
        enableInstrument();
    }

    private List<ArtistMission> getSelectedMissions() {
        return missions.stream().filter(CheckBox::isSelected).map(cb -> (ArtistMission) cb.getUserData())
                       .collect(Collectors.toList());
    }

    private LabeledComboBox<Instrument> createInstrumentControl() {
        refreshInstruments();
        return new LabeledComboBox<>(getResourceBundle().getString(INSTRUMENT), observableInstruments, null, null,
                                     new InstrumentStringConverter(), false);
    }

    private void refreshInstruments() {
        observableInstruments.clear();
        observableInstruments.addAll(ApplicationContext.getInstance().getFolkloreEntityCollections().getInstruments());
        observableInstruments.sort((i1, i2) -> i1.getName().compareToIgnoreCase(i2.getName()));
    }

    private void enableInstrument() {
        instrument.getComboBox().setDisable(!getSelectedMissions().contains(ArtistMission.INSTRUMENT_PLAYER));
    }

}
