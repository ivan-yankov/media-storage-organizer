package org.yankov.mso.database;

import org.yankov.mso.application.ui.controls.ProgressMonitor;
import org.yankov.mso.datamodel.*;

import java.util.Collections;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

public class FolkloreEntityCollections extends EntityCollections<FolklorePiece> {

    private static final String PROPERTY_ETHNOGRAPHIC_REGIONS = "ethnographicRegions";

    private Set<EthnographicRegion> ethnographicRegions;

    public FolkloreEntityCollections(ProgressMonitor progressMonitor) {
        super(progressMonitor);
        this.ethnographicRegions = new HashSet<>();
    }

    public Set<EthnographicRegion> getEthnographicRegions() {
        return Collections.unmodifiableSet(ethnographicRegions);
    }

    public Optional<EthnographicRegion> getEthnographicRegion(String name) {
        return ethnographicRegions.stream().filter(entity -> entity.getName().toLowerCase().trim()
                                                                   .equals(name.toLowerCase().trim())).findFirst();
    }

    public boolean addEthnographicRegion(EthnographicRegion ethnographicRegion) {
        Set<EthnographicRegion> oldValue = new HashSet<>();
        oldValue.addAll(ethnographicRegions);
        boolean result = ethnographicRegions.add(ethnographicRegion);
        propertyChangeSupport.firePropertyChange(PROPERTY_ETHNOGRAPHIC_REGIONS, oldValue, ethnographicRegions);
        return result;
    }

    public void addEthnographicRegions(Set<EthnographicRegion> ethnographicRegions) {
        Set<EthnographicRegion> oldValue = new HashSet<>();
        oldValue.addAll(this.ethnographicRegions);
        this.ethnographicRegions.addAll(ethnographicRegions);
        propertyChangeSupport.firePropertyChange(PROPERTY_ETHNOGRAPHIC_REGIONS, oldValue, this.ethnographicRegions);
    }

    @Override
    public void initializeEntityCollections() {
        sourceTypes.addAll(FolkloreEntityCollectionFactory.createSourceTypes());

        initializeEntityCollection(Source.class, sources,
                                   FolkloreEntityCollectionFactory.createSources(getSourceTypes()));

        initializeEntityCollection(Instrument.class, instruments, FolkloreEntityCollectionFactory.createInstruments());

        initializeEntityCollection(Artist.class, artists, Collections.emptySet());

        initializeEntityCollection(Album.class, albums, Collections.emptySet());

        initializeEntityCollection(EthnographicRegion.class, ethnographicRegions,
                                   FolkloreEntityCollectionFactory.createEthnographicRegions());

        initializeEntityCollection(FolklorePiece.class, pieces, Collections.emptyList());
    }

    @Override
    public void saveEntityCollectionsOperations() {
        saveCollectionToDatabase(getSources(), Source.class.getName());
        saveCollectionToDatabase(getInstruments(), Instrument.class.getName());
        saveCollectionToDatabase(getArtists(), Artist.class.getName());
        saveCollectionToDatabase(getAlbums(), Album.class.getName());
        saveCollectionToDatabase(getEthnographicRegions(), EthnographicRegion.class.getName());
        saveCollectionToDatabase(getPieces(), FolklorePiece.class.getName());
    }

}
