package org.yankov.mso.datamodel.folklore;

import java.util.ListResourceBundle;

public class FolkloreResources extends ListResourceBundle {

    public static final String TRAKIA_REGION = "trakia-region";
    public static final String STRANDZHA_REGION = "strandzha-region";
    public static final String RODOPI_REGION = "rodopi-region";
    public static final String PIRIN_REGION = "pirin-region";
    public static final String SHOPSKI_REGION = "shopski-region";
    public static final String MIZIA_REGION = "mizia-region";
    public static final String DOBRUDZHA_REGION = "dobrudzha-region";
    public static final String VARDARSKA_MAKEDONIA_REGION = "vardarska-makedonia-region";
    public static final String AUTHORITY_REGION = "authority-region";

    @Override
    protected Object[][] getContents() {
        return new Object[][] {
                {FolkloreResources.TRAKIA_REGION, ""},
                {FolkloreResources.STRANDZHA_REGION, ""},
                {FolkloreResources.RODOPI_REGION, ""},
                {FolkloreResources.PIRIN_REGION, ""},
                {FolkloreResources.SHOPSKI_REGION, ""},
                {FolkloreResources.MIZIA_REGION, ""},
                {FolkloreResources.DOBRUDZHA_REGION, ""},
                {FolkloreResources.VARDARSKA_MAKEDONIA_REGION, ""},
                {FolkloreResources.AUTHORITY_REGION, ""}
        };
    }

}
