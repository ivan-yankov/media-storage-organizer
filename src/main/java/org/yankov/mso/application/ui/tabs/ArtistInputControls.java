package org.yankov.mso.application.ui.tabs;

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
import org.yankov.mso.datamodel.Artist;
import org.yankov.mso.datamodel.ArtistMission;
import org.yankov.mso.datamodel.Instrument;

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

    private LabeledTextField name;
    private LabeledTextField note;
    private LabeledComboBox<Instrument> instrument;
    private List<CheckBox> missions;

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
    }

    @Override
    public void onVisibilityChange(boolean visible) {
        if (visible) {
            ObservableList<Instrument> instruments = FXCollections.observableArrayList(
                    ApplicationContext.getInstance().getFolkloreEntityCollections().getInstruments());
            instrument.setItems(instruments.sorted(instrument.getItemComparator()));
        }
    }

    @Override
    protected Pane createControls() {
        name = new LabeledTextField(getResourceBundle().getString(NAME), "", null);
        name.layout();

        note = new LabeledTextField(getResourceBundle().getString(NOTE), "", null);
        note.layout();

        HBox missionsContainer = new HBox();
        missionsContainer.setSpacing(getWhiteSpace());
        missions = createMissions();
        missions.forEach(mission -> missionsContainer.getChildren().add(mission));

        instrument = createInstrumentControl();
        instrument.layout();
        enableInstrument();

        VBox actionControlsContainer = new VBox();
        actionControlsContainer.setSpacing(getWhiteSpace());
        actionControlsContainer.getChildren().add(name.getContainer());
        actionControlsContainer.getChildren().add(note.getContainer());
        TitledPane missionsPane = new TitledPane(getResourceBundle().getString(MISSIONS), missionsContainer);
        missionsPane.setCollapsible(false);
        actionControlsContainer.getChildren().add(missionsPane);
        actionControlsContainer.getChildren().add(instrument.getContainer());

        return actionControlsContainer;
    }

    @Override
    protected List<Artist> getExistingArtifacts() {
        return new ArrayList<>(ApplicationContext.getInstance().getFolkloreEntityCollections().getArtists());
    }

    @Override
    protected StringConverter<Artist> getStringConverter() {
        return new ArtistStringConverter();
    }

    @Override
    protected boolean validateUserInput() {
        if (name.getTextField().getText().isEmpty()) {
            ApplicationContext.getInstance().getLogger()
                              .log(Level.SEVERE, getResourceBundle().getString(ARTIST_NAME_UNDEFINED));
            return false;
        }

        if (getSelectedMissions().isEmpty()) {
            ApplicationContext.getInstance().getLogger()
                              .log(Level.SEVERE, getResourceBundle().getString(NO_ARTIST_MISSION_SELECTED));
            return false;
        }

        if (getSelectedMissions().contains(ArtistMission.INSTRUMENT_PLAYER) && instrument.getComboBox()
                                                                                         .getValue() == null) {
            ApplicationContext.getInstance().getLogger()
                              .log(Level.SEVERE, getResourceBundle().getString(ARTIST_INSTRUMENT_UNDEFINED));
            return false;
        }

        return true;
    }

    @Override
    protected Artist createArtifact() {
        return new Artist();
    }

    @Override
    protected boolean addArtifact(Artist artifact) {
        return ApplicationContext.getInstance().getFolkloreEntityCollections().addArtist(artifact);
    }

    @Override
    protected void cleanup() {
        name.getTextField().setText("");
        note.getTextField().setText("");
        missions.forEach(mission -> mission.setSelected(false));
        instrument.getComboBox().setValue(null);
        enableInstrument();
    }

    @Override
    protected void setArtifactProperties(Artist artifact) {
        artifact.setName(name.getTextField().getText());
        artifact.setNote(note.getTextField().getText());

        if (getSelectedMissions().contains(ArtistMission.INSTRUMENT_PLAYER)) {
            artifact.setInstrument(instrument.getComboBox().getValue());
        } else {
            artifact.setInstrument(null);
        }

        artifact.clearMissions();
        getSelectedMissions().forEach(artifact::addMission);
    }

    @Override
    protected void extractArtifactProperties(Artist artifact) {
        name.getTextField().setText(artifact.getName());
        note.getTextField().setText(artifact.getNote());
        for (CheckBox mission : missions) {
            ArtistMission artistMission = (ArtistMission) mission.getUserData();
            mission.setSelected(artifact.getMissions().contains(artistMission));
        }
        instrument.getComboBox().setValue(artifact.getInstrument());
        enableInstrument();
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
        ObservableList<Instrument> instruments = FXCollections
                .observableArrayList(ApplicationContext.getInstance().getFolkloreEntityCollections().getInstruments());
        return new LabeledComboBox<>(getResourceBundle().getString(INSTRUMENT), instruments, null, null,
                                     new InstrumentStringConverter(), false, true);
    }

    private void enableInstrument() {
        instrument.getComboBox().setDisable(!getSelectedMissions().contains(ArtistMission.INSTRUMENT_PLAYER));
    }

}
