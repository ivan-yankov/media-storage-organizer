package org.yankov.mso.application.ui;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import org.yankov.mso.application.utils.FileUtils;
import org.yankov.mso.datamodel.generic.Artist;
import org.yankov.mso.datamodel.generic.Source;

import java.io.File;
import java.time.Duration;

public class PieceProperties {

    private final SimpleStringProperty album;
    private final SimpleIntegerProperty albumTrackOrder;
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

    public PieceProperties() {
        this.album = new SimpleStringProperty();
        this.albumTrackOrder = new SimpleIntegerProperty();
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
    }

    public String getAlbum() {
        return album.get();
    }

    public void setAlbum(String album) {
        this.album.set(album);
    }

    public int getAlbumTrackOrder() {
        return albumTrackOrder.get();
    }

    public void setAlbumTrackOrder(int albumTrackOrder) {
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
        this.file.set(file);
    }

    public PieceProperties clone() {
        PieceProperties newPiece = new PieceProperties();

        newPiece.setAlbum(getAlbum());
        newPiece.setAlbumTrackOrder(getAlbumTrackOrder());
        newPiece.setTitle(getTitle());
        newPiece.setPerformer(getPerformer());
        newPiece.setAccompanimentPerformer(getAccompanimentPerformer());
        newPiece.setAuthor(getAuthor());
        newPiece.setArrangementAuthor(getArrangementAuthor());
        newPiece.setConductor(getConductor());
        newPiece.setSoloist(getSoloist());
        newPiece.setDuration(getDuration());
        newPiece.setNote(getNote());
        newPiece.setSource(getSource());
        newPiece.setFile(getFile());

        return newPiece;
    }

    public void setFromFile(File file) {
        setAlbum(file.getParentFile().getName());
        setFile(file);
        FileUtils.detectAudioFileDuration(file).ifPresent(this::setDuration);
    }

}
