package org.yankov.mso.datamodel.folklore;

import java.util.Locale;
import java.util.ResourceBundle;

public class ResourceBundles {

    private static final Locale BULGARIAN = new Locale("bg");

    public static final ResourceBundle FOLKLORE_RESOURCES_BG =
            ResourceBundle.getBundle(FolkloreResources_bg.class.getName(), BULGARIAN);

}
