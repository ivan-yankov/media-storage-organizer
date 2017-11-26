package org.yankov.mso.datamodel.generic;

import javax.persistence.*;

@Entity
@Table(name = "ARTIST")
public class Artist {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "name")
    private String name;

    @ManyToOne(cascade = CascadeType.ALL)
    private Instrument instrument;

    @Column(name = "note")
    private String note;

    public Artist() {
    }

    public Artist(String name) {
        this.name = name;
    }

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Instrument getInstrument() {
        return instrument;
    }

    public void setInstrument(Instrument instrument) {
        this.instrument = instrument;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    @Override
    public int hashCode() {
        return name.toLowerCase().trim().hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Artist)) {
            return false;
        }
        Artist other = (Artist) obj;
        return this.name.toLowerCase().trim().equals(other.name.toLowerCase().trim());
    }

}
