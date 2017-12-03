package org.yankov.mso.datamodel.generic;

import javax.persistence.*;
import java.time.Duration;

@Entity
@Table(name = "ALBUM")
public class Album {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "title")
    private String title;

    @Convert(converter = DurationConverter.class)
    private Duration duration;

    @Column(name = "production_signature")
    private String productionSignature;

    @Column(name = "collection_signature")
    private String collectionSignature;

    @Column(name = "note")
    private String note;

    public Album() {
    }

    public Album(String collectionSignature) {
        this.collectionSignature = collectionSignature;
    }

    public Integer getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Duration getDuration() {
        return duration;
    }

    public void setDuration(Duration duration) {
        this.duration = duration;
    }

    public String getProductionSignature() {
        return productionSignature;
    }

    public void setProductionSignature(String productionSignature) {
        this.productionSignature = productionSignature;
    }

    public String getCollectionSignature() {
        return collectionSignature;
    }

    public void setCollectionSignature(String collectionSignature) {
        this.collectionSignature = collectionSignature;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    @Override
    public int hashCode() {
        return collectionSignature.toLowerCase().trim().hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Album)) {
            return false;
        }
        Album other = (Album) obj;
        return this.collectionSignature.toLowerCase().trim().equals(other.collectionSignature.toLowerCase().trim());
    }

}
