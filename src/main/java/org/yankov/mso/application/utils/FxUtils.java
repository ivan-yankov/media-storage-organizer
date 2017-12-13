package org.yankov.mso.application.utils;

import javafx.geometry.Pos;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import org.yankov.mso.application.ui.font.CustomFont;
import org.yankov.mso.application.ui.font.FontFamily;

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
                setText(item);
                setAlignment(pos);
            }

        };
    }

}
