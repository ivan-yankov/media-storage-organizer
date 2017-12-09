package org.yankov.mso.application;

public class ApplicationSettingsFactory {

    public static ApplicationSettings createApplicationSettings(String type) {
        switch (type) {
            case "f":
                return new FolkloreApplicationSettings();
            default:
                return new FolkloreApplicationSettings();
        }
    }

}
