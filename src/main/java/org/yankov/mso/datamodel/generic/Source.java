package org.yankov.mso.datamodel.generic;

import javax.persistence.*;

@Entity
@Table(name = "SOURCE")
public class Source {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @ManyToOne(cascade = CascadeType.ALL)
    private SourceType type;

    @Column(name = "signature")
    private String signature;

    public Source() {
    }

    public Source(SourceType type) {
        this.type = type;
    }

    public Integer getId() {
        return id;
    }

    public SourceType getType() {
        return type;
    }

    public void setType(SourceType type) {
        this.type = type;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    @Override
    public int hashCode() {
        return id;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Source)) {
            return false;
        }
        Source other = (Source) obj;
        return this.id == other.id;
    }

}
