package org.yankov.mso.application.ui.controls;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.yankov.mso.application.ApplicationContext;
import org.yankov.mso.datamodel.*;

import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

public class SaveProgressMonitor implements ProgressMonitor {

    private static final String CLASS_NAME = SaveProgressMonitor.class.getName();

    public static final String OPERATION = CLASS_NAME + "-operation";
    public static final String SOURCES = CLASS_NAME + "-sources";
    public static final String INSTRUMENTS = CLASS_NAME + "-instruments";
    public static final String ARTISTS = CLASS_NAME + "-artists";
    public static final String ALBUMS = CLASS_NAME + "-albums";
    public static final String ETHNOGRAPHIC_REGIONS = CLASS_NAME + "-ethnographic-regions";
    public static final String PIECES = CLASS_NAME + "-pieces";

    private static final ResourceBundle RESOURCE_BUNDLE = ApplicationContext.getInstance().getFolkloreResourceBundle();

    private static final Insets INSETS = new Insets(20.0);
    private static final double SPACING = 20.0;
    private static final Map<String, String> OPERATIONS;

    private Stage stage;
    private Label label;
    private ProgressBar progressBar;

    private int work;

    static {
        OPERATIONS = new HashMap<>();
        OPERATIONS.put(Source.class.getName(), RESOURCE_BUNDLE.getString(SOURCES));
        OPERATIONS.put(Instrument.class.getName(), RESOURCE_BUNDLE.getString(INSTRUMENTS));
        OPERATIONS.put(Artist.class.getName(), RESOURCE_BUNDLE.getString(ARTISTS));
        OPERATIONS.put(Album.class.getName(), RESOURCE_BUNDLE.getString(ALBUMS));
        OPERATIONS.put(EthnographicRegion.class.getName(), RESOURCE_BUNDLE.getString(ETHNOGRAPHIC_REGIONS));
        OPERATIONS.put(FolklorePiece.class.getName(), RESOURCE_BUNDLE.getString(PIECES));

    }

    public SaveProgressMonitor() {
        this.stage = new Stage();
        this.label = new Label();
        this.progressBar = new ProgressBar();
    }

    @Override
    public void createControls() {
        VBox container = new VBox();

        container.setPadding(INSETS);
        container.setSpacing(SPACING);

        container.getChildren().add(label);
        container.getChildren().add(progressBar);

        stage.setScene(new Scene(container));
    }

    @Override
    public void show() {
        stage.showAndWait();
    }

    @Override
    public void close() {
        stage.close();
    }

    @Override
    public void setWork(int work) {
        this.work = work;
    }

    @Override
    public void setStep(int step) {
        double progress = (double) step / (double) work;
        progressBar.setProgress(progress);
    }

    @Override
    public void setOperation(String operation) {
        StringBuilder text = new StringBuilder();
        text.append(RESOURCE_BUNDLE.getString(OPERATION));
        text.append(": ");
        text.append(OPERATIONS.get(operation));
        label.setText(text.toString());
    }

}
