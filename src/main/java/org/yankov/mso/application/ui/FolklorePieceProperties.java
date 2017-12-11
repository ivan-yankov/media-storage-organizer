package org.yankov.mso.application.ui;

import javafx.beans.property.SimpleStringProperty;

public class FolklorePieceProperties extends PieceProperties {

    private final SimpleStringProperty ethnographicRegion;

    public FolklorePieceProperties() {
        super();
        this.ethnographicRegion = new SimpleStringProperty();
    }

    public String getEthnographicRegion() {
        return ethnographicRegion.get();
    }

    public SimpleStringProperty ethnographicRegionProperty() {
        return ethnographicRegion;
    }

    public void setEthnographicRegion(String ethnographicRegion) {
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
        newPiece.setFileName(getFileName());
        newPiece.setEthnographicRegion(getEthnographicRegion());

        return newPiece;
    }

}
