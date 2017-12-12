package org.yankov.mso.application.ui.font;

public enum FontFamily {

    SERIF("serif"),
    SANS_SERIF("sans-serif"),
    CURSIVE("cursive"),
    FANTASY("fantasy"),
    MONOSPACE("monospace");

    private String cssRepresentation;

    FontFamily(String cssRepresentation) {
        this.cssRepresentation = cssRepresentation;
    }

    public String getCssRepresentation() {
        return cssRepresentation;
    }

    public String getName() {
        return cssRepresentation;
    }

}
