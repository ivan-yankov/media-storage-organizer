package org.yankov.mso.application;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Stage;
import org.yankov.mso.application.ui.FolkloreMainForm;

public class MediaStorageOrganizer extends Application {

    public MediaStorageOrganizer() {
    }

    @Override
    public void start(Stage primaryStage) {
        ApplicationContext.getInstance().setPrimaryStage(primaryStage);

        ApplicationSettings applicationSettings = ApplicationContext.getInstance().getApplicationSettings();
        primaryStage.setMaximized(applicationSettings.isMaximized());
        applicationSettings.getWindowWidth().ifPresent(primaryStage::setWidth);
        applicationSettings.getWindowHeight().ifPresent(primaryStage::setHeight);
        applicationSettings.getIcon().ifPresent(primaryStage.getIcons()::add);
        applicationSettings.getTitle().ifPresent(primaryStage::setTitle);
        applicationSettings.getX().ifPresent(primaryStage::setX);
        applicationSettings.getY().ifPresent(primaryStage::setY);

        ApplicationContext.getInstance().getDatabaseSessionManager().openSession();

        Form form = new FolkloreMainForm(primaryStage);
        form.createControls();
        form.show();
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
