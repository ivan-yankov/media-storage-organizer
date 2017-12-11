package org.yankov.mso.application.ui;

import javafx.beans.property.SimpleStringProperty;

public class PieceProperties {

    private final SimpleStringProperty album;
    private final SimpleStringProperty albumTrackOrder;
    private final SimpleStringProperty title;
    private final SimpleStringProperty performer;
    private final SimpleStringProperty accompanimentPerformer;
    private final SimpleStringProperty author;
    private final SimpleStringProperty arrangementAuthor;
    private final SimpleStringProperty conductor;
    private final SimpleStringProperty soloist;
    private final SimpleStringProperty duration;
    private final SimpleStringProperty note;
    private final SimpleStringProperty source;
    private final SimpleStringProperty fileName;

    public PieceProperties() {
        this.album = new SimpleStringProperty();
        this.albumTrackOrder = new SimpleStringProperty();
        this.title = new SimpleStringProperty();
        this.performer = new SimpleStringProperty();
        this.accompanimentPerformer = new SimpleStringProperty();
        this.author = new SimpleStringProperty();
        this.arrangementAuthor = new SimpleStringProperty();
        this.conductor = new SimpleStringProperty();
        this.soloist = new SimpleStringProperty();
        this.duration = new SimpleStringProperty();
        this.note = new SimpleStringProperty();
        this.source = new SimpleStringProperty();
        this.fileName = new SimpleStringProperty();
    }

    public String getAlbum() {
        return album.get();
    }

    public SimpleStringProperty albumProperty() {
        return album;
    }

    public void setAlbum(String album) {
        this.album.set(album);
    }

    public String getAlbumTrackOrder() {
        return albumTrackOrder.get();
    }

    public SimpleStringProperty albumTrackOrderProperty() {
        return albumTrackOrder;
    }

    public void setAlbumTrackOrder(String albumTrackOrder) {
        this.albumTrackOrder.set(albumTrackOrder);
    }

    public String getTitle() {
        return title.get();
    }

    public SimpleStringProperty titleProperty() {
        return title;
    }

    public void setTitle(String title) {
        this.title.set(title);
    }

    public String getPerformer() {
        return performer.get();
    }

    public SimpleStringProperty performerProperty() {
        return performer;
    }

    public void setPerformer(String performer) {
        this.performer.set(performer);
    }

    public String getAccompanimentPerformer() {
        return accompanimentPerformer.get();
    }

    public SimpleStringProperty accompanimentPerformerProperty() {
        return accompanimentPerformer;
    }

    public void setAccompanimentPerformer(String accompanimentPerformer) {
        this.accompanimentPerformer.set(accompanimentPerformer);
    }

    public String getAuthor() {
        return author.get();
    }

    public SimpleStringProperty authorProperty() {
        return author;
    }

    public void setAuthor(String author) {
        this.author.set(author);
    }

    public String getArrangementAuthor() {
        return arrangementAuthor.get();
    }

    public SimpleStringProperty arrangementAuthorProperty() {
        return arrangementAuthor;
    }

    public void setArrangementAuthor(String arrangementAuthor) {
        this.arrangementAuthor.set(arrangementAuthor);
    }

    public String getConductor() {
        return conductor.get();
    }

    public SimpleStringProperty conductorProperty() {
        return conductor;
    }

    public void setConductor(String conductor) {
        this.conductor.set(conductor);
    }

    public String getSoloist() {
        return soloist.get();
    }

    public SimpleStringProperty soloistProperty() {
        return soloist;
    }

    public void setSoloist(String soloist) {
        this.soloist.set(soloist);
    }

    public String getDuration() {
        return duration.get();
    }

    public SimpleStringProperty durationProperty() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration.set(duration);
    }

    public String getNote() {
        return note.get();
    }

    public SimpleStringProperty noteProperty() {
        return note;
    }

    public void setNote(String note) {
        this.note.set(note);
    }

    public String getSource() {
        return source.get();
    }

    public SimpleStringProperty sourceProperty() {
        return source;
    }

    public void setSource(String source) {
        this.source.set(source);
    }

    public String getFileName() {
        return fileName.get();
    }

    public SimpleStringProperty fileNameProperty() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName.set(fileName);
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
        newPiece.setFileName(getFileName());

        return newPiece;
    }

}