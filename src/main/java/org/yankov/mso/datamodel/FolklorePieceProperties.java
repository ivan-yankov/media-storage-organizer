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

    public FolklorePieceProperties clone() {
        FolklorePieceProperties newPiece = new FolklorePieceProperties();

        newPiece.setAlbum(getAlbum());
        newPiece.setAlbumTrackOrder(getAlbumTrackOrder());
        newPiece.setTitle(getTitle());
        newPiece.setPerformer(getPerformer());
        newPiece.setAccompanimentPerformer(getAccompanimentPerformer());
        newPiece.setAuthor(getAuthor());
        newPiece.setArrangementAuthor(getArrangementAuthor());
        newPiece.setConductor(getConductor());
        newPiece.setSoloist(getSoloist());
        newPiece.setDuration(getDuration());
        newPiece.setNote(getNote());
        newPiece.setSource(getSource());
        newPiece.setFile(getFile());
        newPiece.setEthnographicRegion(getEthnographicRegion());

        return newPiece;
    }

}
