package org.yankov.mso.datamodel;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.BiPredicate;
import java.util.stream.Collectors;

public class Operator {

    private static final String[] STOP_WORDS = {",", ";", "-", "/"};

    private String label;
    private BiPredicate<String, String> predicate;
    private boolean errorResponse;

    // true if operator should search for separate words if hole phrase was not found
    private boolean searchForWords;

    public Operator(String label, BiPredicate<String, String> predicate, boolean errorResponse,
                    boolean searchForWords) {
        this.label = label;
        this.predicate = predicate;
        this.errorResponse = errorResponse;
        this.searchForWords = searchForWords;
    }

    public String getLabel() {
        return label;
    }

    public <T> List<T> match(Collection<T> items, Variable<T> variable, String value) {
        List<T> result = search(items, variable, value);
        if (result.isEmpty() && searchForWords) {
            return searchWords(items, variable, value.split(" "));
        } else {
            return result;
        }
    }

    private <T> List<T> search(Collection<T> items, Variable<T> variable, String value) {
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

    private <T> List<T> searchWords(Collection<T> items, Variable<T> variable, String[] words) {
        List<T> result = new ArrayList<>();
        for (String word : words) {
            result.addAll(search(items, variable, word));
        }
        return result;
    }

    private String escapeStopWords(String value) {
        String result = value;
        for (String stopWord : STOP_WORDS) {
            if (stopWord.equals("-")) {
                result = result.replace("-", " ").replace("   ", " ");
                continue;
            }
            result = result.replace(stopWord, "");
        }
        return result;
    }

}
