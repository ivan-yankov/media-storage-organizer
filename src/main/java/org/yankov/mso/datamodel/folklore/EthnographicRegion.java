package org.yankov.mso.datamodel.folklore;

import javax.persistence.*;

@Entity
@Table(name = "ETHNOGRAPHIC_REGION")
public class EthnographicRegion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "name")
    private String name;

    public EthnographicRegion() {
    }

    public EthnographicRegion(String name) {
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
        if (!(obj instanceof EthnographicRegion)) {
            return false;
        }
        EthnographicRegion other = (EthnographicRegion) obj;
        return this.name.toLowerCase().trim().equals(other.name.toLowerCase().trim());
    }

}
