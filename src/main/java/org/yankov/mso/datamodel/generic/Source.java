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

    public Source(SourceType type, String signature) {
        this.type = type;
        this.signature = signature;
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
        return toString().toLowerCase().trim().hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Source)) {
            return false;
        }
        Source other = (Source) obj;
        return this.toString().toLowerCase().trim().equals(other.toString().toLowerCase().trim());
    }

    @Override
    public String toString() {
        StringBuilder representation = new StringBuilder();

        representation.append(type.getName());
        if (signature != null && !signature.isEmpty()) {
            representation.append("/");
            representation.append(signature);
        }

        return representation.toString();
    }

}
