package org.yankov.mso.application;

import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Screen;

import java.util.Optional;

public class FolkloreApplicationSettings implements ApplicationSettings {

    public FolkloreApplicationSettings() {
    }

    @Override
    public boolean isMaximized() {
        return false;
    }

    @Override
    public Optional<Image> getIcon() {
        return Optional.of(new Image("file:./src/main/resources/application-icon.png"));
    }

    @Override
    public Optional<String> getTitle() {
        return Optional.of("Folklore Media Storage Organizer");
    }

    @Override
    public Optional<Double> getWindowWidth() {
        return Optional.of(1000.0);
    }

    @Override
    public Optional<Double> getWindowHeight() {
        return Optional.of(500.0);
    }

    @Override
    public Optional<Double> getX() {
        return Optional.of(Screen.getPrimary().getVisualBounds().getMinX());
    }

    @Override
    public Optional<Double> getY() {
        return Optional.of(Screen.getPrimary().getVisualBounds().getMinY());
    }

    @Override
    public Scene getScene() {
        return null;
    }

}
