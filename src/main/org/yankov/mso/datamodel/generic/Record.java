package org.yankov.mso.datamodel.generic;

public class Record {

    private Integer id;
    private Byte[] bytes;

    public Record(Integer id, Byte[] bytes) {
        this.id = id;
        this.bytes = bytes;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Byte[] getBytes() {
        return bytes;
    }

    public void setBytes(Byte[] bytes) {
        this.bytes = bytes;
    }

    @Override
    public int hashCode() {
        return id;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Record)) {
            return false;
        }
        Record other = (Record) obj;
        return this.id == other.id;
    }

}
