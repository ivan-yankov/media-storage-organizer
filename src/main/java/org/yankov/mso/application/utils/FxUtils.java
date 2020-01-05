package org.yankov.mso.application.utils;

import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import org.yankov.mso.application.ApplicationContext;
import org.yankov.mso.application.ui.font.CustomFont;
import org.yankov.mso.application.ui.font.FontFamily;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

public class FxUtils {

    private static final String CLASS_NAME = FxUtils.class.getName();

    public static final String SELECT_AUDIO_FILES = CLASS_NAME + "-select-audio-files";
    public static final String SELECT_EXPORT_DIRECTORY = CLASS_NAME + "-select-export-directory";
    public static final String FLAC_FILTER_NAME = CLASS_NAME + "-flac-filter-name";
    public static final String FLAC_FILTER_EXT = CLASS_NAME + "-flac-filter-ext";
    public static final String CONFIRMATION = CLASS_NAME + "-confirmation";
    public static final String OVERWRITE_RECORDS_IN_DATABASE = CLASS_NAME + "-delete-record-from-database";
    public static final String CLOSE_APPLICATION = CLASS_NAME + "-close-application";
    public static final String ARE_YOU_SURE = CLASS_NAME + "-are-you-sure";
    public static final String YES = CLASS_NAME + "-yes";
    public static final String NO = CLASS_NAME + "-no";

    private static final ResourceBundle resourceBundle = ApplicationContext.getInstance().getFolkloreResourceBundle();

    public static double calculateTextWidth(String text, CustomFont font) {
        if (text == null) {
            return 0.0;
        }
        // empirical formula
        double coefficient = font.getFamily() == FontFamily.MONOSPACE ? 0.75 : 0.8;
        return (double) text.length() * font.getSize() * coefficient;
    }

    public static <T> TableCell<T, String> createTextCellAligned(TableColumn<T, String> column, Pos pos) {
        return new TableCell<T, String>() {

            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                setText(item);
                setAlignment(pos);
            }

        };
    }

    public static Optional<List<File>> selectFlacFiles(boolean singleSelection) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle(resourceBundle.getString(SELECT_AUDIO_FILES));
        fileChooser.getExtensionFilters().add(createFlacFileFilter());
        if (singleSelection) {
            File file = fileChooser
                .showOpenDialog(ApplicationContext.getInstance().getPrimaryStage().getScene().getWindow());
            if (file != null) {
                List<File> list = new ArrayList<>();
                list.add(file);
                return Optional.of(list);
            } else {
                return Optional.empty();
            }
        } else {
            List<File> files = fileChooser
                .showOpenMultipleDialog(ApplicationContext.getInstance().getPrimaryStage().getScene().getWindow());
            return files != null ? Optional.of(files) : Optional.empty();
        }
    }

    public static Optional<File> selectDirectory() {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setTitle(resourceBundle.getString(SELECT_EXPORT_DIRECTORY));
        File file = directoryChooser
            .showDialog(ApplicationContext.getInstance().getPrimaryStage().getScene().getWindow());
        return file != null ? Optional.of(file) : Optional.empty();
    }

    public static boolean confirmOverwrite() {
        return confirmDialog(resourceBundle.getString(OVERWRITE_RECORDS_IN_DATABASE));
    }

    public static boolean confirmCloseApplication() {
        return confirmDialog(resourceBundle.getString(CLOSE_APPLICATION));
    }

    private static boolean confirmDialog(String headerText) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(resourceBundle.getString(CONFIRMATION));
        alert.setHeaderText(headerText);
        alert.setContentText(resourceBundle.getString(ARE_YOU_SURE));

        ButtonType yes = new ButtonType(resourceBundle.getString(YES));
        ButtonType no = new ButtonType(resourceBundle.getString(NO));

        alert.getButtonTypes().setAll(yes, no);
        Optional<ButtonType> answer = alert.showAndWait();

        return answer.isPresent() && answer.get() == yes;
    }

    private static FileChooser.ExtensionFilter createFlacFileFilter() {
        return new FileChooser.ExtensionFilter(resourceBundle.getString(FLAC_FILTER_NAME),
            resourceBundle.getString(FLAC_FILTER_EXT));
    }

}
