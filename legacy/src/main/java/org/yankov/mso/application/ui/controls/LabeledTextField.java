package org.yankov.mso.application.ui.controls;

import javafx.beans.property.StringProperty;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import org.yankov.mso.application.UserInterfaceControls;

import java.util.function.Consumer;
import java.util.function.Predicate;

public class LabeledTextField implements UserInterfaceControls {

    private String labelText;
    private Label label;
    private TextField textField;
    private VBox container;
    private String value;
    private Predicate<String> newValueValidator;
    private Consumer<String> newValueConsumer;

    public LabeledTextField(String labelText, String value) {
        this.labelText = labelText;
        this.container = new VBox();
        this.label = new Label();
        this.textField = new TextField();
        this.value = value;
    }

    public void setNewValueValidator(Predicate<String> newValueValidator) {
        this.newValueValidator = newValueValidator;
    }

    public void setNewValueConsumer(Consumer<String> newValueConsumer) {
        this.newValueConsumer = newValueConsumer;
    }

    public TextField getTextField() {
        return textField;
    }

    @Override
    public void layout() {
        label.setText(labelText);
        container.getChildren().add(label);

        textField.setText(value);
        if (newValueValidator != null) {
            textField.textProperty().addListener((observable, oldValue, newValue) -> {
                if (newValueValidator.test(newValue)) {
                    ((StringProperty) observable).setValue(newValue);
                } else {
                    ((StringProperty) observable).setValue(oldValue);
                }
            });
        }
        if (newValueConsumer != null) {
            textField.textProperty().addListener((observable, oldValue, newValue) -> newValueConsumer.accept(newValue));
        }
        container.getChildren().add(textField);
    }

    @Override
    public Pane getContainer() {
        return container;
    }

}
