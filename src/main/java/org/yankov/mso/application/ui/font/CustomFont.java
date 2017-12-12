package org.yankov.mso.application.ui.font;

import javafx.scene.text.Font;

import java.text.MessageFormat;

public class CustomFont {

    private static final String CSS_PATTERN =
            "-fx-font-family:{0};-fx-font-weight:{1};-fx-font-style:{2};-fx-font-size:{3}px;";

    private final FontFamily family;
    private final FontWeight weight;
    private final FontStyle style;
    private final int size;

    public CustomFont(FontFamily family, FontWeight weight, FontStyle style, int size) {
        this.family = family;
        this.weight = weight;
        this.style = style;
        this.size = size;
    }

    public FontFamily getFamily() {
        return family;
    }

    public FontWeight getWeight() {
        return weight;
    }

    public FontStyle getStyle() {
        return style;
    }

    public int getSize() {
        return size;
    }

    public String toCssRepresentation() {
        return MessageFormat.format(CSS_PATTERN, family.getCssRepresentation(), weight.getCssRepresentation(),
                                    style.getCssRepresentation(), Integer.toString(size));
    }

    public Font toFxFont() {
        return Font.font(family.getName(), weight.toFxFontWeight(), style.toFontPosture(), size);
    }

}
