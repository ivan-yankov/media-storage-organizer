package org.yankov.mso.datamodel.generic;

import javax.persistence.*;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

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

    @ElementCollection(targetClass = ArtistMission.class)
    @Enumerated(EnumType.STRING)
    @Column(name = "missions")
    private Set<ArtistMission> missions;

    public Artist() {
        this.missions = new HashSet<>();
    }

    public Artist(String name) {
        this();
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

    public void addMission(ArtistMission mission) {
        missions.add(mission);
    }

    public Set<ArtistMission> getMissions() {
        return Collections.unmodifiableSet(missions);
    }

    public void clearMissions() {
        missions.clear();
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
