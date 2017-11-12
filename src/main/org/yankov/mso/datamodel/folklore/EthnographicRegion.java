package org.yankov.mso.datamodel.folklore;

public enum EthnographicRegion {

    TRAKIA(ResourceBundles.FOLKLORE_RESOURCES_BG.getString("trakia-region")),
    STRANDZHA(ResourceBundles.FOLKLORE_RESOURCES_BG.getString("strandzha-region")),
    RODOPI(ResourceBundles.FOLKLORE_RESOURCES_BG.getString("rodopi-region")),
    PIRIN(ResourceBundles.FOLKLORE_RESOURCES_BG.getString("pirin-region")),
    SHOPSKI(ResourceBundles.FOLKLORE_RESOURCES_BG.getString("shopski-region")),
    MIZIA(ResourceBundles.FOLKLORE_RESOURCES_BG.getString("mizia-region")),
    DOBRUDZHA(ResourceBundles.FOLKLORE_RESOURCES_BG.getString("dobrudzha-region")),
    VARDARSKA_MAKEDONIA(ResourceBundles.FOLKLORE_RESOURCES_BG.getString("vardarska-makedonia-region")),
    AUTHORITY(ResourceBundles.FOLKLORE_RESOURCES_BG.getString("authority-region"));

    private String name;

    EthnographicRegion(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

}
