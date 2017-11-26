package org.yankov.mso.database.org.yankov.mso.database.folklore;

import org.yankov.mso.database.org.yankov.mso.database.generic.EntityCollections;
import org.yankov.mso.datamodel.folklore.EthnographicRegion;
import org.yankov.mso.datamodel.folklore.FolklorePiece;
import org.yankov.mso.datamodel.generic.Artist;
import org.yankov.mso.datamodel.generic.Disc;
import org.yankov.mso.datamodel.generic.Instrument;
import org.yankov.mso.datamodel.generic.SourceType;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.*;

public class FolkloreEntityCollections extends EntityCollections {

    private Set<EthnographicRegion> ethnographicRegions;
    private List<FolklorePiece> pieces;

    public FolkloreEntityCollections() {
        super();
        this.ethnographicRegions = new HashSet<>();
        this.pieces = new ArrayList<>();
    }

    public Set<EthnographicRegion> getEthnographicRegions() {
        return ethnographicRegions;
    }

    public List<FolklorePiece> getPieces() {
        return pieces;
    }

    public void addPiece(FolklorePiece piece) {
        pieces.add(piece);
    }

    public Optional<EthnographicRegion> getEthnographicRegion(String name) {
        return ethnographicRegions.stream().filter(entity -> entity.getName().toLowerCase().trim()
                .equals(name.toLowerCase().trim())).findFirst();
    }

    public boolean addEthnographicRegion(String name) {
        return ethnographicRegions.add(new EthnographicRegion(name));
    }

    @Override
    public void initializeEntityCollections() {
        initializeEntityCollection(SourceType.class, getSourceTypes(),
                FolkloreEntityCollectionFactory.createSourceTypes());

        initializeEntityCollection(Instrument.class, getInstruments(),
                FolkloreEntityCollectionFactory.createInstruments());

        initializeEntityCollection(Artist.class, getArtists(), Collections.emptySet());

        initializeEntityCollection(Disc.class, getDiscs(), Collections.emptySet());

        initializeEntityCollection(EthnographicRegion.class, getEthnographicRegions(),
                FolkloreEntityCollectionFactory.createEthnographicRegions());

        initializeEntityCollection(FolklorePiece.class, getPieces(), Collections.emptyList());
    }

    @Override
    public void saveEntityCollections() {
        saveCollectionToDatabase(getSourceTypes());
        saveCollectionToDatabase(getInstruments());
        saveCollectionToDatabase(getArtists());
        saveCollectionToDatabase(getDiscs());
        saveCollectionToDatabase(getEthnographicRegions());
        saveCollectionToDatabase(getPieces());
    }

}
