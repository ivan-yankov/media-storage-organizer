package org.yankov.mso.application;

import java.util.Optional;
import java.util.function.Consumer;
import java.util.logging.Level;
import org.yankov.mso.application.ui.FolkloreMainForm;
import org.yankov.mso.application.utils.FxUtils;
import org.yankov.mso.database.DatabaseOperations;
import org.yankov.mso.database.FolkloreEntityCollections;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class MediaStorageOrganizer extends Application {

    public static final String TEST_MODE_NAME = "test-mode";
    public static final String LANGUAGE_NAME = "lang";
    public static final String DB_URL_NAME = "db-url";
    public static final String DB_DRIVER_NAME = "db-driver";

    public static final CommandLineArguments arguments;

    static {
        arguments = new CommandLineArguments();
        arguments.add(new CommandLineArgument(TEST_MODE_NAME, null, "false", false, true, "true", "false"));
        arguments.add(new CommandLineArgument(LANGUAGE_NAME, null, "bg", false, false, "bg"));
        arguments.add(new CommandLineArgument(DB_URL_NAME, null, null, true, false));
        arguments.add(new CommandLineArgument(DB_DRIVER_NAME, null, "embedded", false, false, "embedded", "client"));
    }

    public MediaStorageOrganizer() {
    }

    @Override
    public void start(Stage primaryStage) {
        try {
            ApplicationContext.getInstance().setPrimaryStage(primaryStage);

            ApplicationSettings applicationSettings = ApplicationContext.getInstance().getApplicationSettings();
            primaryStage.setMaximized(applicationSettings.isMaximized());
            applicationSettings.getWindowWidth().ifPresent(primaryStage::setWidth);
            applicationSettings.getWindowHeight().ifPresent(primaryStage::setHeight);
            applicationSettings.getIcon().ifPresent(primaryStage.getIcons()::add);
            applicationSettings.getTitle().ifPresent(primaryStage::setTitle);
            applicationSettings.getX().ifPresent(primaryStage::setX);
            applicationSettings.getY().ifPresent(primaryStage::setY);

            primaryStage.setOnCloseRequest(this::onCloseApplication);

            Thread startServerThread = new Thread(
                () -> ApplicationContext.getInstance().getDatabaseManager().startServer());
            startServerThread.start();

            if (!ApplicationContext.getInstance().getDatabaseManager().openSession()) {
                throw new Exception("Database error.");
            }

            FolkloreEntityCollections collections = (FolkloreEntityCollections) DatabaseOperations
                .loadFromDatabase(FolkloreEntityCollections::new);
            collections.initialize();
            ApplicationContext.getInstance().setFolkloreEntityCollections(collections);

            Form form = new FolkloreMainForm(primaryStage);
            form.createControls();
            form.show();
        } catch (Exception e) {
            ApplicationContext.getInstance().getLogger().log(Level.SEVERE, e.getMessage(), e);
            exit(1);
        }
    }

    @Override
    public void stop() {
        ApplicationContext.getInstance().getDatabaseManager().closeSession();
        ApplicationContext.getInstance().getDatabaseManager().stopServer();
        exit(0);
    }

    private static void exit(int exitCode) {
        Platform.exit();
        System.exit(exitCode);
    }

    private void onCloseApplication(WindowEvent event) {
        if (!FxUtils.confirmCloseApplication()) {
            event.consume();
        }
    }

    public static void main(String[] args) {
        Optional<String> validationError = arguments.parseValues(args);
        if (validationError.isPresent()) {
            System.out.println(validationError);
            exit(1);
        }
        ApplicationContext.getInstance().initialize(arguments);

        Consumer<Throwable> dbLogger = throwable -> ApplicationContext.getInstance().getLogger().log(Level.SEVERE,
            throwable.getMessage(), throwable);

        ApplicationContext.getInstance().getDatabaseManager().setOperationFailed(dbLogger);

        launch(args);
    }

}
