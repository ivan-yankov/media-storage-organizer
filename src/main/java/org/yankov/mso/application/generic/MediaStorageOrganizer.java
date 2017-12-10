package org.yankov.mso.application.generic;

import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableView;
import javafx.stage.Stage;

public class MediaStorageOrganizer extends Application {

    public MediaStorageOrganizer() {
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        ApplicationSettings applicationSettings = ApplicationContext.getInstance().getApplicationSettings();

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
        ApplicationArguments applicationArguments = new ApplicationArguments(args);
        ApplicationContext.getInstance().initialize(applicationArguments);

        launch(args);
    }

}
