package org.yankov.mso.application;

import javafx.scene.image.Image;
import javafx.stage.Screen;

import java.net.URL;
import java.util.Optional;

public class FolkloreApplicationSettings implements ApplicationSettings {

    private static final String CLASS_NAME = FolkloreApplicationSettings.class.getName();

    public static final String STAGE_TITLE = CLASS_NAME + "-stage-title";

    public FolkloreApplicationSettings() {
    }

    @Override
    public boolean isMaximized() {
        return true;
    }

    @Override
    public Optional<Image> getIcon() {
        URL url = getClass().getResource("/icons/application.png");
        Image image = new Image(url.toString());
        return Optional.of(image);
    }

    @Override
    public Optional<String> getTitle() {
        return Optional.of(ApplicationContext.getInstance().getFolkloreResourceBundle().getString(STAGE_TITLE));
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

}
