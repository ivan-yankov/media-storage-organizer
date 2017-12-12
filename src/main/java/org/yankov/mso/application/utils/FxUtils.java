package org.yankov.mso.application.utils;

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

}
