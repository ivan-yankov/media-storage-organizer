package org.yankov.mso.application;

import javafx.application.Application;
import javafx.stage.Stage;

public class MediaStorageOrganizer extends Application {

    private static ApplicationSettings applicationSettings;

    public MediaStorageOrganizer() {
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setMaximized(applicationSettings.isMaximized());

        applicationSettings.getWindowWidth().ifPresent(primaryStage::setWidth);
        applicationSettings.getWindowHeight().ifPresent(primaryStage::setHeight);
        applicationSettings.getIcon().ifPresent(primaryStage.getIcons()::add);
        applicationSettings.getTitle().ifPresent(primaryStage::setTitle);
        applicationSettings.getX().ifPresent(primaryStage::setX);
        applicationSettings.getY().ifPresent(primaryStage::setY);

        primaryStage.setScene(applicationSettings.getScene());

        primaryStage.show();
    }

    public static void main(String[] args) {
        String settingsType = argsPresented(args) ? args[0] : "";
        applicationSettings = ApplicationSettingsFactory.createApplicationSettings(settingsType);

        launch(args);
    }

    private static boolean argsPresented(String[] args) {
        return args != null && args.length > 0;
    }

}
