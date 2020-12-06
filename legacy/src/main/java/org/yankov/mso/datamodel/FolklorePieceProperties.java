package org.yankov.mso.datamodel;

import javafx.beans.property.SimpleObjectProperty;

public class FolklorePieceProperties extends PieceProperties {

    private final SimpleObjectProperty<EthnographicRegion> ethnographicRegion;

    public FolklorePieceProperties() {
        super();
        this.ethnographicRegion = new SimpleObjectProperty<>();
    }

    public EthnographicRegion getEthnographicRegion() {
        return ethnographicRegion.get();
    }

    public void setEthnographicRegion(EthnographicRegion ethnographicRegion) {
        this.ethnographicRegion.set(ethnographicRegion);
    }

}
