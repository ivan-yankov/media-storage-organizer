package org.yankov.mso.application;

import javafx.scene.Scene;
import javafx.scene.image.Image;

import java.util.Optional;

public interface ApplicationSettings {

    boolean isMaximized();

    Optional<Image> getIcon();

    Optional<String> getTitle();

    Optional<Double> getWindowWidth();

    Optional<Double> getWindowHeight();

    Optional<Double> getX();

    Optional<Double> getY();

}
