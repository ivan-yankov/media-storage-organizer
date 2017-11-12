package org.yankov.mso.datamodel.generic;

public class SourceType {

    private Integer id;
    private String name;

    public SourceType(Integer id, String name) {
        this.id = id;
        this.name = name;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public int hashCode() {
        return id;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof SourceType)) {
            return false;
        }
        SourceType other = (SourceType) obj;
        return this.id == other.id;
    }

}
