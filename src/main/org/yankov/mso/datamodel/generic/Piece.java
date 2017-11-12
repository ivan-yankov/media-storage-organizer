package org.yankov.mso.datamodel.generic;

import java.time.Duration;

public class Piece {

    private Integer id;

    private Disc disc;
    private Short cdTrackOrder;

    private String title;
    private Artist performer;
    private Artist accompanimentPerformer;
    private Artist author;
    private Artist arrangementAuthor;
    private Artist conductor;
    private Artist soloist;
    private Duration duration;
    private String note;
    private Source source;

    private Record record;

    public Piece(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Disc getDisc() {
        return disc;
    }

    public void setDisc(Disc disc) {
        this.disc = disc;
    }

    public Short getCdTrackOrder() {
        return cdTrackOrder;
    }

    public void setCdTrackOrder(Short cdTrackOrder) {
        this.cdTrackOrder = cdTrackOrder;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Artist getPerformer() {
        return performer;
    }

    public void setPerformer(Artist performer) {
        this.performer = performer;
    }

    public Artist getAccompanimentPerformer() {
        return accompanimentPerformer;
    }

    public void setAccompanimentPerformer(Artist accompanimentPerformer) {
        this.accompanimentPerformer = accompanimentPerformer;
    }

    public Artist getAuthor() {
        return author;
    }

    public void setAuthor(Artist author) {
        this.author = author;
    }

    public Artist getArrangementAuthor() {
        return arrangementAuthor;
    }

    public void setArrangementAuthor(Artist arrangementAuthor) {
        this.arrangementAuthor = arrangementAuthor;
    }

    public Artist getConductor() {
        return conductor;
    }

    public void setConductor(Artist conductor) {
        this.conductor = conductor;
    }

    public Artist getSoloist() {
        return soloist;
    }

    public void setSoloist(Artist soloist) {
        this.soloist = soloist;
    }

    public Duration getDuration() {
        return duration;
    }

    public void setDuration(Duration duration) {
        this.duration = duration;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public Source getSource() {
        return source;
    }

    public void setSource(Source source) {
        this.source = source;
    }

    public Record getRecord() {
        return record;
    }

    public void setRecord(Record record) {
        this.record = record;
    }

    @Override
    public int hashCode() {
        return id;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Piece)) {
            return false;
        }
        Piece other = (Piece) obj;
        return this.id == other.id;
    }

}
