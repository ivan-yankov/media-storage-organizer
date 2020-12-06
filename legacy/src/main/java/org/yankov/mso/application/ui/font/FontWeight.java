package org.yankov.mso.application.ui.font;

public enum FontWeight {

    NORMAL("normal"),
    BOLD("bold"),
    BOLDER("bolder"),
    LIGHTER("lighter");

    private String cssRepresentation;

    FontWeight(String cssRepresentation) {
        this.cssRepresentation = cssRepresentation;
    }

    public String getCssRepresentation() {
        return cssRepresentation;
    }

    public javafx.scene.text.FontWeight toFxFontWeight() {
        switch (this) {
            case NORMAL:
                return javafx.scene.text.FontWeight.NORMAL;
            case BOLD:
                return javafx.scene.text.FontWeight.BOLD;
            case BOLDER:
                return javafx.scene.text.FontWeight.EXTRA_BOLD;
            case LIGHTER:
                return javafx.scene.text.FontWeight.LIGHT;
            default:
                return javafx.scene.text.FontWeight.NORMAL;
        }
    }

}
