package org.yankov.mso.datamodel.generic;

import javax.persistence.*;

@Entity
@Table(name = "RECORD")
public class Record {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Lob
    @Column(name = "data")
    private byte[] bytes;

    @Column(name="data_format")
    private String dataFormat;

    public Record() {
    }

    public Integer getId() {
        return id;
    }

    public byte[] getBytes() {
        return bytes;
    }

    public void setBytes(byte[] bytes) {
        this.bytes = bytes;
    }

    public String getDataFormat() {
        return dataFormat;
    }

    public void setDataFormat(String dataFormat) {
        this.dataFormat = dataFormat;
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
