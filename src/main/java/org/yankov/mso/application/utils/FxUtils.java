package org.yankov.mso.application.utils;

import javafx.geometry.Pos;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.stage.FileChooser;
import org.yankov.mso.application.ApplicationContext;
import org.yankov.mso.application.ui.font.CustomFont;
import org.yankov.mso.application.ui.font.FontFamily;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class FxUtils {

    public static double calculateTextWidth(String text, CustomFont font) {
        if (text == null) {
            return 0.0;
        }
        // empirical formula
        double coefficient = font.getFamily() == FontFamily.MONOSPACE ? 0.75 : 0.8;
        return (double) text.length() * font.getSize() * coefficient;
    }

    public static <T> TableCell<T, String> createTextCellAligned(TableColumn<T, String> column, Pos pos) {
        return new TableCell<>() {

            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                setText(item);
                setAlignment(pos);
            }

        };
    }

    public static Optional<List<File>> selectFiles(String title, boolean singleSelection,
                                                   FileChooser.ExtensionFilter... filters) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle(title);
        fileChooser.getExtensionFilters().addAll(filters);
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

}
