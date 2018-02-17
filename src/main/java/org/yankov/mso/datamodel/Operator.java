package org.yankov.mso.datamodel;

import java.util.List;
import java.util.function.BiPredicate;
import java.util.stream.Collectors;

public class Operator {

    private String label;
    private BiPredicate<String, String> predicate;

    public Operator(String label, BiPredicate<String, String> predicate) {
        this.label = label;
        this.predicate = predicate;
    }

    public String getLabel() {
        return label;
    }

    public <T> List<T> match(Variable<T> variable, String value) {
        return variable.getObservableItems().stream().filter(item -> {
            String observableProperty = variable.getObservablePropertyProvider().apply(item);
            return predicate.test(observableProperty, value);
        }).collect(Collectors.toList());
    }

}
