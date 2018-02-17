package org.yankov.mso.application;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Stage;
import org.yankov.mso.application.ui.FolkloreMainForm;
import org.yankov.mso.database.FolkloreEntityCollections;

import java.util.logging.Level;

public class MediaStorageOrganizer extends Application {

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

            boolean sessionOpened = ApplicationContext.getInstance().getDatabaseSessionManager().openSession();
            if (!sessionOpened) {
                ApplicationContext.getInstance().getLogger().log(Level.SEVERE, "Unable to create database session.");
                Platform.exit();
                System.exit(1);
            }

            FolkloreEntityCollections folkloreEntityCollections = new FolkloreEntityCollections();
            folkloreEntityCollections.initializeEntityCollections();
            ApplicationContext.getInstance().setFolkloreEntityCollections(folkloreEntityCollections);

            Form form = new FolkloreMainForm(primaryStage);
            form.createControls();
            form.show();
        } catch (Exception e) {
            ApplicationContext.getInstance().getLogger().log(Level.SEVERE, "Cannot start application", e);
            System.exit(1);
        }
    }

    @Override
    public void stop() {
        ApplicationContext.getInstance().getDatabaseSessionManager().closeSession();
        Platform.exit();
        System.exit(0);
    }

    public static void main(String[] args) {
        ApplicationContext.getInstance().initialize(new ApplicationArguments(args));
        launch(args);
    }

}
