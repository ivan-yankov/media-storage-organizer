package org.yankov.mso.datamodel.folklore;

import java.util.ListResourceBundle;
import java.util.Locale;
import java.util.ResourceBundle;

public class FolkloreResources extends ListResourceBundle {

    private static final Locale BULGARIAN = new Locale("bg");

    public static final ResourceBundle FOLKLORE_RESOURCES_BG =
            ResourceBundle.getBundle(FolkloreResources_bg.class.getName(), BULGARIAN);

    public static final String REGION_TRAKIA = "region-trakia";
    public static final String REGION_STRANDZHA = "region-strandzha";
    public static final String REGION_RODOPI = "region-rodopi";
    public static final String REGION_PIRIN = "region-pirin";
    public static final String REGION_SHOPSKI = "region-shopski";
    public static final String REGION_MIZIA = "region-mizia";
    public static final String REGION_DOBRUDZHA = "region-dobrudzha";
    public static final String REGION_MACEDONIA = "region-vardarska-makedonia";
    public static final String REGION_AUTHORITY = "region-authority";

    public static final String INSTRUMENT_KAVAL = "instrument-kaval";
    public static final String INSTRUMENT_GAIDA = "instrument-gaida";
    public static final String INSTRUMENT_GADULKA = "instrument-gadulka";
    public static final String INSTRUMENT_TAMBURA = "instrument-tambura";
    public static final String INSTRUMENT_TAPAN = "instrument-tapan";

    public static final String SOURCE_TYPE_TAPE = "source-type-tape";
    public static final String SOURCE_TYPE_CARTRIDGE = "source-type-cartridge";
    public static final String SOURCE_TYPE_GRAMOPHONE_PLATE = "source-type-gramophone-plate";
    public static final String SOURCE_TYPE_CD = "source-type-cd";
    public static final String SOURCE_TYPE_VIDEO = "source-type-video";
    public static final String SOURCE_TYPE_INTERNET = "source-type-internet";

    @Override
    protected Object[][] getContents() {
        return new Object[][] {
                { FolkloreResources.REGION_TRAKIA, "" },
                { FolkloreResources.REGION_STRANDZHA, "" },
                { FolkloreResources.REGION_RODOPI, "" },
                { FolkloreResources.REGION_PIRIN, "" },
                { FolkloreResources.REGION_SHOPSKI, "" },
                { FolkloreResources.REGION_MIZIA, "" },
                { FolkloreResources.REGION_DOBRUDZHA, "" },
                { FolkloreResources.REGION_MACEDONIA, "" },
                { FolkloreResources.REGION_AUTHORITY, "" },

                { FolkloreResources.INSTRUMENT_KAVAL, "" },
                { FolkloreResources.INSTRUMENT_GAIDA, "" },
                { FolkloreResources.INSTRUMENT_GADULKA, "" },
                { FolkloreResources.INSTRUMENT_TAMBURA, "" },
                { FolkloreResources.INSTRUMENT_TAPAN, "" },

                { FolkloreResources.SOURCE_TYPE_TAPE, "" },
                { FolkloreResources.SOURCE_TYPE_CARTRIDGE, "" },
                { FolkloreResources.SOURCE_TYPE_GRAMOPHONE_PLATE, "" },
                { FolkloreResources.SOURCE_TYPE_CD, "" },
                { FolkloreResources.SOURCE_TYPE_VIDEO, "" },
                { FolkloreResources.SOURCE_TYPE_INTERNET, "" },
        };
    }

}
