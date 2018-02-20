package org.yankov.mso.datamodel;

import java.util.function.Function;

public class Variable<T> {

    private final String label;
    private final Function<T, String> observablePropertyProvider;

    public Variable(String label, Function<T, String> observablePropertyProvider) {
        this.label = label;
        this.observablePropertyProvider = observablePropertyProvider;
    }

    public String getLabel() {
        return label;
    }

    public Function<T, String> getObservablePropertyProvider() {
        return observablePropertyProvider;
    }

}
