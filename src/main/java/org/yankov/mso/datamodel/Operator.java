package org.yankov.mso.datamodel;

import java.util.Collection;
import java.util.List;
import java.util.function.BiPredicate;
import java.util.stream.Collectors;

public class Operator {

    private static final String[] STOP_WORDS = {",", ";", "-", "/"};

    private String label;
    private BiPredicate<String, String> predicate;
    private boolean errorResponse;

    public Operator(String label, BiPredicate<String, String> predicate, boolean errorResponse) {
        this.label = label;
        this.predicate = predicate;
        this.errorResponse = errorResponse;
    }

    public String getLabel() {
        return label;
    }

    public <T> List<T> match(Collection<T> items, Variable<T> variable, String value) {
        return items.stream().filter(item -> {
            try {
                String observableProperty = variable.getObservablePropertyProvider().apply(item);
                return predicate.test(escapeStopWords(observableProperty), escapeStopWords(value));
            } catch (Exception e) {
                // happens when property which provider looks for is not presented
                // response depends on operator type and shows when item without property should be included in the result
                return errorResponse;
            }
        }).collect(Collectors.toList());
    }

    private String escapeStopWords(String value) {
        String result = value;
        for (String stopWord : STOP_WORDS) {
            result = result.replace(stopWord, "");
        }
        return result;
    }

}
