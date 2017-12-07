package org.yankov.mso.database.folklore;

import org.yankov.mso.datamodel.folklore.EthnographicRegion;
import org.yankov.mso.datamodel.folklore.FolkloreResources;
import org.yankov.mso.datamodel.generic.Instrument;
import org.yankov.mso.datamodel.generic.SourceType;

import java.util.HashSet;
import java.util.ResourceBundle;
import java.util.Set;

public class FolkloreEntityCollectionFactory {

    private static ResourceBundle resourceBundle = FolkloreResources.FOLKLORE_RESOURCES_BG;

    public static Set<SourceType> createSourceTypes() {
        Set<SourceType> sourceTypes = new HashSet<>();
        sourceTypes.add(new SourceType(resourceBundle.getString(FolkloreResources.SOURCE_TYPE_TAPE)));
        sourceTypes.add(new SourceType(resourceBundle.getString(FolkloreResources.SOURCE_TYPE_CARTRIDGE)));
        sourceTypes.add(new SourceType(resourceBundle.getString(FolkloreResources.SOURCE_TYPE_GRAMOPHONE_RECORD)));
        sourceTypes.add(new SourceType(resourceBundle.getString(FolkloreResources.SOURCE_TYPE_CD)));
        sourceTypes.add(new SourceType(resourceBundle.getString(FolkloreResources.SOURCE_TYPE_VIDEO)));
        sourceTypes.add(new SourceType(resourceBundle.getString(FolkloreResources.SOURCE_TYPE_INTERNET)));
        return sourceTypes;
    }

    public static Set<Instrument> createInstruments() {
        Set<Instrument> instruments = new HashSet<>();
        instruments.add(new Instrument(resourceBundle.getString(FolkloreResources.INSTRUMENT_KAVAL)));
        instruments.add(new Instrument(resourceBundle.getString(FolkloreResources.INSTRUMENT_GAIDA)));
        instruments.add(new Instrument(resourceBundle.getString(FolkloreResources.INSTRUMENT_GADULKA)));
        instruments.add(new Instrument(resourceBundle.getString(FolkloreResources.INSTRUMENT_TAMBURA)));
        instruments.add(new Instrument(resourceBundle.getString(FolkloreResources.INSTRUMENT_TAPAN)));
        return instruments;
    }

    public static Set<EthnographicRegion> createEthnographicRegions() {
        Set<EthnographicRegion> ethnographicRegions = new HashSet<>();
        ethnographicRegions.add(new EthnographicRegion(resourceBundle.getString(FolkloreResources.REGION_TRAKIA)));
        ethnographicRegions.add(new EthnographicRegion(resourceBundle.getString(FolkloreResources.REGION_STRANDZHA)));
        ethnographicRegions.add(new EthnographicRegion(resourceBundle.getString(FolkloreResources.REGION_RODOPI)));
        ethnographicRegions.add(new EthnographicRegion(resourceBundle.getString(FolkloreResources.REGION_PIRIN)));
        ethnographicRegions.add(new EthnographicRegion(resourceBundle.getString(FolkloreResources.REGION_SHOPSKI)));
        ethnographicRegions.add(new EthnographicRegion(resourceBundle.getString(FolkloreResources.REGION_MIZIA)));
        ethnographicRegions.add(new EthnographicRegion(resourceBundle.getString(FolkloreResources.REGION_DOBRUDZHA)));
        ethnographicRegions.add(new EthnographicRegion(resourceBundle.getString(FolkloreResources.REGION_MACEDONIA)));
        ethnographicRegions.add(new EthnographicRegion(resourceBundle.getString(FolkloreResources.REGION_AUTHORITY)));
        return ethnographicRegions;
    }

}
