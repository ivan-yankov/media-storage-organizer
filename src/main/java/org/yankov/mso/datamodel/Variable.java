package org.yankov.mso.datamodel;

import java.util.Collection;
import java.util.function.Function;

public class Variable<T> {

    private final String label;
    private final Collection<T> observableItems;
    private final Function<T, String> observablePropertyProvider;

    public Variable(String label, Collection<T> items, Function<T, String> observablePropertyProvider) {
        this.label = label;
        this.observableItems = items;
        this.observablePropertyProvider = observablePropertyProvider;
    }

    public String getLabel() {
        return label;
    }

    public Collection<T> getObservableItems() {
        return observableItems;
    }

    public Function<T, String> getObservablePropertyProvider() {
        return observablePropertyProvider;
    }

}
