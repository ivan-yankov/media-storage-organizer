package org.yankov.mso.datamodel.generic;

public class Instrument {

    private Integer id;
    private String name;
    private String note;

    public Instrument(Integer id, String name) {
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

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    @Override
    public int hashCode() {
        return id;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Instrument)) {
            return false;
        }
        Instrument other = (Instrument) obj;
        return this.id == other.id;
    }

}
