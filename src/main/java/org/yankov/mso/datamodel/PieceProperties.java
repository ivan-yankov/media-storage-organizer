package org.yankov.mso.datamodel;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import org.yankov.mso.application.ApplicationContext;
import org.yankov.mso.application.utils.FlacProcessor;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.time.Duration;
import java.util.Optional;
import java.util.logging.Level;

public class PieceProperties implements PropertyChangeListener {

    private static final String PROPERTY_NAME_FILE = "file";

    private final SimpleObjectProperty<Integer> id;
    private final SimpleObjectProperty<Album> album;
    private final SimpleStringProperty albumTrackOrder;
    private final SimpleStringProperty title;
    private final SimpleObjectProperty<Artist> performer;
    private final SimpleObjectProperty<Artist> accompanimentPerformer;
    private final SimpleObjectProperty<Artist> author;
    private final SimpleObjectProperty<Artist> arrangementAuthor;
    private final SimpleObjectProperty<Artist> conductor;
    private final SimpleObjectProperty<Artist> soloist;
    private final SimpleObjectProperty<Duration> duration;
    private final SimpleStringProperty note;
    private final SimpleObjectProperty<Source> source;
    private final SimpleObjectProperty<File> file;
    private final SimpleObjectProperty<Record> record;

    private final PropertyChangeSupport propertyChangeSupport;

    public PieceProperties() {
        this.id = new SimpleObjectProperty<>();
        this.album = new SimpleObjectProperty<>();
        this.albumTrackOrder = new SimpleStringProperty();
        this.title = new SimpleStringProperty();
        this.performer = new SimpleObjectProperty<>();
        this.accompanimentPerformer = new SimpleObjectProperty<>();
        this.author = new SimpleObjectProperty<>();
        this.arrangementAuthor = new SimpleObjectProperty<>();
        this.conductor = new SimpleObjectProperty<>();
        this.soloist = new SimpleObjectProperty<>();
        this.duration = new SimpleObjectProperty<>();
        this.note = new SimpleStringProperty();
        this.source = new SimpleObjectProperty<>();
        this.file = new SimpleObjectProperty<>();
        this.record = new SimpleObjectProperty<>();

        this.propertyChangeSupport = new PropertyChangeSupport(this);
        this.propertyChangeSupport.addPropertyChangeListener(this);
    }

    public Integer getId() {
        return id.get();
    }

    public void setId(Integer id) {
        this.id.set(id);
    }

    public Album getAlbum() {
        return album.get();
    }

    public void setAlbum(Album album) {
        this.album.set(album);
    }

    public String getAlbumTrackOrder() {
        return albumTrackOrder.get();
    }

    public void setAlbumTrackOrder(String albumTrackOrder) {
        this.albumTrackOrder.set(albumTrackOrder);
    }

    public String getTitle() {
        return title.get();
    }

    public void setTitle(String title) {
        this.title.set(title);
    }

    public Artist getPerformer() {
        return performer.get();
    }

    public void setPerformer(Artist performer) {
        this.performer.set(performer);
    }

    public Artist getAccompanimentPerformer() {
        return accompanimentPerformer.get();
    }

    public void setAccompanimentPerformer(Artist accompanimentPerformer) {
        this.accompanimentPerformer.set(accompanimentPerformer);
    }

    public Artist getAuthor() {
        return author.get();
    }

    public void setAuthor(Artist author) {
        this.author.set(author);
    }

    public Artist getArrangementAuthor() {
        return arrangementAuthor.get();
    }

    public void setArrangementAuthor(Artist arrangementAuthor) {
        this.arrangementAuthor.set(arrangementAuthor);
    }

    public Artist getConductor() {
        return conductor.get();
    }

    public void setConductor(Artist conductor) {
        this.conductor.set(conductor);
    }

    public Artist getSoloist() {
        return soloist.get();
    }

    public void setSoloist(Artist soloist) {
        this.soloist.set(soloist);
    }

    public Duration getDuration() {
        return duration.get();
    }

    public void setDuration(Duration duration) {
        this.duration.set(duration);
    }

    public String getNote() {
        return note.get();
    }

    public void setNote(String note) {
        this.note.set(note);
    }

    public Source getSource() {
        return source.get();
    }

    public void setSource(Source source) {
        this.source.set(source);
    }

    public File getFile() {
        return file.get();
    }

    public void setFile(File file) {
        File oldValue = this.file.get();
        this.file.set(file);
        propertyChangeSupport.firePropertyChange(PROPERTY_NAME_FILE, oldValue, file);
    }

    public Record getRecord() {
        return record.get();
    }

    public void setRecord(Record record) {
        this.record.set(record);
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        FlacProcessor flacProcessor = new FlacProcessor(file.get());
        flacProcessor.detectDuration().ifPresent(this::setDuration);
        Optional<Record> record = flacProcessor.loadRecordFromFile();
        record.ifPresent(this::setRecord);
    }

}
