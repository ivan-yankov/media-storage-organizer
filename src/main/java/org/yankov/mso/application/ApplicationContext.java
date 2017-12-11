package org.yankov.mso.application;

import org.yankov.mso.datamodel.folklore.FolkloreResources;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;

public class ApplicationContext {

    private static final String ARG_KEY_SETTINGS = "-settings";
    private static final String DEFAULT_SETTINGS = "folklore";
    private static final String ARG_KEY_LANGUAGE = "-language";
    private static final String DEFAULT_LANGUAGE = "bg";

    private static ApplicationContext instance;

    private ApplicationArguments applicationArguments;
    private Locale locale;
    private ApplicationSettings applicationSettings;
    private ResourceBundle folkloreResourceBundle;
    private Map<String, Object> objects;

    private ApplicationContext() {
    }

    public static ApplicationContext getInstance() {
        if (instance == null) {
            instance = new ApplicationContext();
        }
        return instance;
    }

    public void initialize(ApplicationArguments applicationArguments) {
        this.applicationArguments = applicationArguments;
        this.locale = new Locale(applicationArguments.getArgument(ARG_KEY_LANGUAGE, DEFAULT_LANGUAGE));
        this.applicationSettings = createApplicationSettings(
                applicationArguments.getArgument(ARG_KEY_SETTINGS, DEFAULT_SETTINGS));
        this.folkloreResourceBundle = ResourceBundle
                .getBundle(FolkloreResources.class.getName(), getLocale());
        this.objects = new HashMap<>();
    }

    public Locale getLocale() {
        return locale;
    }

    public ApplicationSettings getApplicationSettings() {
        return applicationSettings;
    }

    public ResourceBundle getFolkloreResourceBundle() {
        return folkloreResourceBundle;
    }

    public Object getObject(String key) {
        return objects.get(key);
    }

    public void setObject(String key, Object object) {
        this.objects.put(key, object);
    }

    private ApplicationSettings createApplicationSettings(String type) {
        switch (type) {
            case "folklore":
                return new FolkloreApplicationSettings();
            default:
                return null;
        }
    }

}
