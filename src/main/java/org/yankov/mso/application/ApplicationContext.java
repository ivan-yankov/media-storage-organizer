package org.yankov.mso.application;

import java.util.Locale;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.yankov.mso.application.ui.ApplicationConsole;
import org.yankov.mso.application.ui.ApplicationConsoleLogHandler;
import org.yankov.mso.database.DatabaseManager;
import org.yankov.mso.database.FolkloreEntityCollections;
import javafx.stage.Stage;

public class ApplicationContext {

    private static ApplicationContext instance;

    private ApplicationArguments applicationArguments;
    private Locale locale;
    private ApplicationSettings applicationSettings;
    private ResourceBundle folkloreResourceBundle;
    private Logger logger;
    private Stage primaryStage;
    private Commands commands;
    private DatabaseManager databaseManager;
    private FolkloreEntityCollections folkloreEntityCollections;

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

        String lang = applicationArguments.getArgument(ApplicationArguments.LANGUAGE_KEY);
        this.locale = new Locale(lang);

        this.applicationSettings = new FolkloreApplicationSettings();

        this.folkloreResourceBundle = ResourceBundle.getBundle(FolkloreResources.class.getName(), getLocale());

        this.logger = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
        this.logger.addHandler(new ApplicationConsoleLogHandler(createConsoleService()));

        this.commands = new Commands();
        this.commands.initialize();

        this.databaseManager = new DatabaseManager();
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

    public Stage getPrimaryStage() {
        return primaryStage;
    }

    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    public DatabaseManager getDatabaseManager() {
        return databaseManager;
    }

    public FolkloreEntityCollections getFolkloreEntityCollections() {
        return folkloreEntityCollections;
    }

    public void setFolkloreEntityCollections(FolkloreEntityCollections folkloreEntityCollections) {
        this.folkloreEntityCollections = folkloreEntityCollections;
    }

    public void executeCommand(String command, Object... commandArgs) {
        if (commands.getCommand(command).isPresent()) {
            Command cmd = commands.getCommand(command).get();
            if (cmd.checkArguments(commandArgs)) {
                cmd.execute(commandArgs);
            } else {
                logger.log(Level.SEVERE, cmd.getErrorMessage());
            }
        } else {
            logger.log(Level.SEVERE, "No such command " + command);
        }
    }

    private ConsoleService createConsoleService() {
        return !isTestMode() ? ApplicationConsole.getInstance() : new ConsoleServiceAdapter();
    }

    private boolean isTestMode() {
        return Boolean.parseBoolean(applicationArguments.getArgument(ApplicationArguments.TEST_MODE_KEY));
    }

}
