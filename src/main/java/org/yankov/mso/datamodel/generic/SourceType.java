package org.yankov.mso.datamodel.generic;

import javax.persistence.*;

@Entity
@Table(name = "SOURCE_TYPE")
public class SourceType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "name")
    private String name;

    public SourceType() {
    }

    public SourceType(String name) {
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
        if (!(obj instanceof SourceType)) {
            return false;
        }
        SourceType other = (SourceType) obj;
        return this.name.toLowerCase().trim().equals(other.name.toLowerCase().trim());
    }

}
