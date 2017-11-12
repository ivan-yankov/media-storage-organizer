package org.yankov.mso.datamodel.folklore;

import org.yankov.mso.datamodel.generic.Piece;

public class FolklorePiece extends Piece {

    private EthnographicRegion ethnographicRegion;

    public FolklorePiece(Integer id) {
        super(id);
    }

    public EthnographicRegion getEthnographicRegion() {
        return ethnographicRegion;
    }

    public void setEthnographicRegion(EthnographicRegion ethnographicRegion) {
        this.ethnographicRegion = ethnographicRegion;
    }

}
