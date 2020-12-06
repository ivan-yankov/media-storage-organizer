package org.yankov.mso.application.ui.font;

import javafx.scene.text.FontPosture;

public enum FontStyle {

    NORMAL("normal"),
    ITALIC("italic"),
    OBLIQUE("oblique");

    private String cssRepresentation;

    FontStyle(String cssRepresentation) {
        this.cssRepresentation = cssRepresentation;
    }

    public String getCssRepresentation() {
        return cssRepresentation;
    }

    public FontPosture toFontPosture() {
        switch (this) {
            case NORMAL:
                return FontPosture.REGULAR;
            case ITALIC:
                return FontPosture.ITALIC;
            case OBLIQUE:
                return FontPosture.ITALIC;
            default:
                return FontPosture.REGULAR;
        }
    }

}
