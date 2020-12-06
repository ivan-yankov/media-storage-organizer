package org.yankov.mso.application.ui.tabs;

import org.yankov.mso.application.ui.controls.LabeledComboBox;
import org.yankov.mso.application.ui.controls.LabeledTextField;
import org.yankov.mso.datamodel.Operator;
import org.yankov.mso.datamodel.Variable;

public class Filter<T> {

    private final LabeledComboBox<Variable<T>> variables;
    private final LabeledComboBox<Operator> operators;
    private final LabeledTextField value;

    public Filter(LabeledComboBox<Variable<T>> variables, LabeledComboBox<Operator> operators, LabeledTextField value) {
        this.variables = variables;
        this.operators = operators;
        this.value = value;
    }

    public LabeledComboBox<Variable<T>> getVariables() {
        return variables;
    }

    public LabeledComboBox<Operator> getOperators() {
        return operators;
    }

    public LabeledTextField getValue() {
        return value;
    }

}
