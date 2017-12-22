package org.yankov.mso.database.folklore;

import org.yankov.mso.application.ApplicationContext;
import org.yankov.mso.datamodel.folklore.EthnographicRegion;
import org.yankov.mso.datamodel.generic.Instrument;
import org.yankov.mso.datamodel.generic.Source;
import org.yankov.mso.datamodel.generic.SourceType;

import java.util.HashSet;
import java.util.ResourceBundle;
import java.util.Set;

public class FolkloreEntityCollectionFactory {

    private static final String CLASS_NAME = FolkloreEntityCollectionFactory.class.getName();

    public static final String REGION_TRAKIA = CLASS_NAME + "-region-trakia";
    public static final String REGION_STRANDZHA = CLASS_NAME + "-region-strandzha";
    public static final String REGION_RODOPI = CLASS_NAME + "-region-rodopi";
    public static final String REGION_PIRIN = CLASS_NAME + "-region-pirin";
    public static final String REGION_SHOPSKI = CLASS_NAME + "-region-shopski";
    public static final String REGION_MIZIA = CLASS_NAME + "-region-mizia";
    public static final String REGION_DOBRUDZHA = CLASS_NAME + "-region-dobrudzha";
    public static final String REGION_MACEDONIA = CLASS_NAME + "-region-vardarska-makedonia";
    public static final String REGION_AUTHORITY = CLASS_NAME + "-region-authority";

    public static final String INSTRUMENT_KAVAL = CLASS_NAME + "-instrument-kaval";
    public static final String INSTRUMENT_GAIDA = CLASS_NAME + "-instrument-gaida";
    public static final String INSTRUMENT_GADULKA = CLASS_NAME + "-instrument-gadulka";
    public static final String INSTRUMENT_TAMBURA = CLASS_NAME + "-instrument-tambura";
    public static final String INSTRUMENT_TAPAN = CLASS_NAME + "-instrument-tapan";

    public static final String SOURCE_TYPE_TAPE = CLASS_NAME + "-source-type-tape";
    public static final String SOURCE_TYPE_CARTRIDGE = CLASS_NAME + "-source-type-cartridge";
    public static final String SOURCE_TYPE_GRAMOPHONE_RECORD = CLASS_NAME + "-source-type-gramophone-record";
    public static final String SOURCE_TYPE_CD = CLASS_NAME + "-source-type-cd";
    public static final String SOURCE_TYPE_VIDEO = CLASS_NAME + "-source-type-video";
    public static final String SOURCE_TYPE_INTERNET = CLASS_NAME + "-source-type-internet";

    private static final ResourceBundle RESOURCE_BUNDLE = ApplicationContext.getInstance().getFolkloreResourceBundle();

    public static Set<Source> createSources() {
        Set<Source> sources = new HashSet<>();
        sources.add(new Source(new SourceType(RESOURCE_BUNDLE.getString(SOURCE_TYPE_TAPE)), ""));
        sources.add(new Source(new SourceType(RESOURCE_BUNDLE.getString(SOURCE_TYPE_CARTRIDGE)), ""));
        sources.add(new Source(new SourceType(RESOURCE_BUNDLE.getString(SOURCE_TYPE_GRAMOPHONE_RECORD)), ""));
        sources.add(new Source(new SourceType(RESOURCE_BUNDLE.getString(SOURCE_TYPE_CD)), ""));
        sources.add(new Source(new SourceType(RESOURCE_BUNDLE.getString(SOURCE_TYPE_VIDEO)), ""));
        sources.add(new Source(new SourceType(RESOURCE_BUNDLE.getString(SOURCE_TYPE_INTERNET)), ""));
        return sources;
    }

    public static Set<Instrument> createInstruments() {
        Set<Instrument> instruments = new HashSet<>();
        instruments.add(new Instrument(RESOURCE_BUNDLE.getString(INSTRUMENT_KAVAL)));
        instruments.add(new Instrument(RESOURCE_BUNDLE.getString(INSTRUMENT_GAIDA)));
        instruments.add(new Instrument(RESOURCE_BUNDLE.getString(INSTRUMENT_GADULKA)));
        instruments.add(new Instrument(RESOURCE_BUNDLE.getString(INSTRUMENT_TAMBURA)));
        instruments.add(new Instrument(RESOURCE_BUNDLE.getString(INSTRUMENT_TAPAN)));
        return instruments;
    }

    public static Set<EthnographicRegion> createEthnographicRegions() {
        Set<EthnographicRegion> ethnographicRegions = new HashSet<>();
        ethnographicRegions.add(new EthnographicRegion(RESOURCE_BUNDLE.getString(REGION_TRAKIA)));
        ethnographicRegions.add(new EthnographicRegion(RESOURCE_BUNDLE.getString(REGION_STRANDZHA)));
        ethnographicRegions.add(new EthnographicRegion(RESOURCE_BUNDLE.getString(REGION_RODOPI)));
        ethnographicRegions.add(new EthnographicRegion(RESOURCE_BUNDLE.getString(REGION_PIRIN)));
        ethnographicRegions.add(new EthnographicRegion(RESOURCE_BUNDLE.getString(REGION_SHOPSKI)));
        ethnographicRegions.add(new EthnographicRegion(RESOURCE_BUNDLE.getString(REGION_MIZIA)));
        ethnographicRegions.add(new EthnographicRegion(RESOURCE_BUNDLE.getString(REGION_DOBRUDZHA)));
        ethnographicRegions.add(new EthnographicRegion(RESOURCE_BUNDLE.getString(REGION_MACEDONIA)));
        ethnographicRegions.add(new EthnographicRegion(RESOURCE_BUNDLE.getString(REGION_AUTHORITY)));
        return ethnographicRegions;
    }

}
