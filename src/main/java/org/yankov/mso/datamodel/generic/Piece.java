package org.yankov.mso.datamodel.generic;

import javax.persistence.*;
import java.time.Duration;

@MappedSuperclass
public class Piece {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @ManyToOne(cascade = CascadeType.ALL)
    private Disc disc;

    @Column(name = "cd_track_order")
    private Integer cdTrackOrder;

    @Column(name = "title")
    private String title;

    @ManyToOne(cascade = CascadeType.ALL)
    private Artist performer;

    @ManyToOne(cascade = CascadeType.ALL)
    private Artist accompanimentPerformer;

    @ManyToOne(cascade = CascadeType.ALL)
    private Artist author;

    @ManyToOne(cascade = CascadeType.ALL)
    private Artist arrangementAuthor;

    @ManyToOne(cascade = CascadeType.ALL)
    private Artist conductor;

    @ManyToOne(cascade = CascadeType.ALL)
    private Artist soloist;

    @Convert(converter = DurationConverter.class)
    private Duration duration;

    @Column(name = "note")
    private String note;

    @OneToOne(cascade = CascadeType.ALL)
    private Source source;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Record record;

    public Piece() {
    }

    public Integer getId() {
        return id;
    }

    public Disc getDisc() {
        return disc;
    }

    public void setDisc(Disc disc) {
        this.disc = disc;
    }

    public Integer getCdTrackOrder() {
        return cdTrackOrder;
    }

    public void setCdTrackOrder(Integer cdTrackOrder) {
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
