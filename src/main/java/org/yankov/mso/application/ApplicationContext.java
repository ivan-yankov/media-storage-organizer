package org.yankov.mso.application;

import javafx.stage.Stage;
import org.yankov.mso.application.ui.ApplicationConsole;
import org.yankov.mso.application.ui.ApplicationConsoleLogHandler;

import java.util.Locale;
import java.util.ResourceBundle;
import java.util.logging.Logger;

public class ApplicationContext {

    private static final String ARG_KEY_SETTINGS = "-settings";
    private static final String DEFAULT_SETTINGS = "folklore";
    private static final String ARG_KEY_LANGUAGE = "-language";
    private static final String DEFAULT_LANGUAGE = "bg";
    private static final String ARG_KEY_MODE = "-mode";
    private static final String DEFAULT_MODE = "run";

    private static ApplicationContext instance;

    private ApplicationArguments applicationArguments;
    private Locale locale;
    private ApplicationSettings applicationSettings;
    private ResourceBundle folkloreResourceBundle;
    private Logger logger;
    private Stage prmaryStage;

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
        this.folkloreResourceBundle = ResourceBundle.getBundle(FolkloreResources.class.getName(), getLocale());
        this.logger = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
        this.logger.addHandler(new ApplicationConsoleLogHandler(
                createConsoleService(applicationArguments.getArgument(ARG_KEY_MODE, DEFAULT_MODE))));
    }

    public ApplicationArguments getApplicationArguments() {
        return applicationArguments;
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

    public Logger getLogger() {
        return logger;
    }

    public Stage getPrmaryStage() {
        return prmaryStage;
    }

    public void setPrmaryStage(Stage prmaryStage) {
        this.prmaryStage = prmaryStage;
    }

    private ApplicationSettings createApplicationSettings(String type) {
        switch (type) {
            case "folklore":
                return new FolkloreApplicationSettings();
            default:
                return null;
        }
    }

    private ConsoleService createConsoleService(String mode) {
        switch (mode) {
            case "run":
                return ApplicationConsole.getInstance();
            case "test":
                return new ConsoleServiceAdapter();
            default:
                return new ConsoleServiceAdapter();
        }
    }

}
