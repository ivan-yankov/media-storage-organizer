package org.yankov.mso.application;

import javafx.application.Application;
import javafx.stage.Stage;
import org.yankov.mso.application.ui.FolkloreMainForm;

public class MediaStorageOrganizer extends Application {

    public MediaStorageOrganizer() {
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        ApplicationSettings applicationSettings = ApplicationContext.getInstance().getApplicationSettings();

        ApplicationContext.getInstance().setPrimaryStage(primaryStage);

        primaryStage.setMaximized(applicationSettings.isMaximized());

        applicationSettings.getWindowWidth().ifPresent(primaryStage::setWidth);
        applicationSettings.getWindowHeight().ifPresent(primaryStage::setHeight);
        applicationSettings.getIcon().ifPresent(primaryStage.getIcons()::add);
        applicationSettings.getTitle().ifPresent(primaryStage::setTitle);
        applicationSettings.getX().ifPresent(primaryStage::setX);
        applicationSettings.getY().ifPresent(primaryStage::setY);

        Form form = new FolkloreMainForm(primaryStage);
        form.createControls();
        form.show();
    }

    public static void main(String[] args) {
        ApplicationArguments applicationArguments = new ApplicationArguments(args);
        ApplicationContext.getInstance().initialize(applicationArguments);

        launch(args);
    }

}
