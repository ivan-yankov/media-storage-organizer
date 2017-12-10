package org.yankov.mso.application.folklore;

import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Screen;
import org.yankov.mso.application.generic.ApplicationContext;
import org.yankov.mso.application.generic.ApplicationSettings;

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
        return Optional.of(new Image("file:./src/main/resources/application-icon.png"));
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

    @Override
    public Scene getScene() {
        FolkloreScene folkloreScene = new FolkloreScene();
        folkloreScene.layout();
        return folkloreScene.getContent();
    }

}
