package org.yankov.mso.database.folklore;

import org.yankov.mso.database.generic.EntityCollections;
import org.yankov.mso.datamodel.folklore.EthnographicRegion;
import org.yankov.mso.datamodel.folklore.FolklorePiece;
import org.yankov.mso.datamodel.generic.Album;
import org.yankov.mso.datamodel.generic.Artist;
import org.yankov.mso.datamodel.generic.Instrument;
import org.yankov.mso.datamodel.generic.SourceType;

import java.util.*;

public class FolkloreEntityCollections extends EntityCollections<FolklorePiece> {

    private Set<EthnographicRegion> ethnographicRegions;

    public FolkloreEntityCollections() {
        super();
        this.ethnographicRegions = new HashSet<>();
    }

    public Set<EthnographicRegion> getEthnographicRegions() {
        return ethnographicRegions;
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

        initializeEntityCollection(Album.class, getAlbums(), Collections.emptySet());

        initializeEntityCollection(EthnographicRegion.class, getEthnographicRegions(),
                                   FolkloreEntityCollectionFactory.createEthnographicRegions());

        initializeEntityCollection(FolklorePiece.class, getPieces(), Collections.emptyList());
    }

    @Override
    public void saveEntityCollections() {
        saveCollectionToDatabase(getSourceTypes());
        saveCollectionToDatabase(getInstruments());
        saveCollectionToDatabase(getArtists());
        saveCollectionToDatabase(getAlbums());
        saveCollectionToDatabase(getEthnographicRegions());
        saveCollectionToDatabase(getPieces());
    }

}
