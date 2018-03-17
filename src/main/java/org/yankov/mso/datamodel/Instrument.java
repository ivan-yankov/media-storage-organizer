package org.yankov.mso.datamodel;

import javax.persistence.*;

@Entity
@Table(name = "INSTRUMENT")
public class Instrument {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "name")
    private String name;

    public Instrument() {
    }

    public Instrument(String name) {
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

    @Override
    public int hashCode() {
        return name.toLowerCase().trim().hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Instrument)) {
            return false;
        }
        Instrument other = (Instrument) obj;
        return this.name.toLowerCase().trim().equals(other.name.toLowerCase().trim());
    }

}