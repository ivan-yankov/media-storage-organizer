package org.yankov.mso.datamodel;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "FOLKLORE_PIECE")
public class FolklorePiece extends Piece {

    @ManyToOne(cascade = CascadeType.ALL)
    private EthnographicRegion ethnographicRegion;

    public FolklorePiece() {
    }

    public EthnographicRegion getEthnographicRegion() {
        return ethnographicRegion;
    }

    public void setEthnographicRegion(EthnographicRegion ethnographicRegion) {
        this.ethnographicRegion = ethnographicRegion;
    }

}
