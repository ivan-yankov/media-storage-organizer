package org.yankov.mso.application.ui.controls;

import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import org.yankov.mso.application.UserInterfaceControls;

import java.util.function.Consumer;

public class LabeledTextField implements UserInterfaceControls {

    private String labelText;
    private Label label;
    private TextField textField;
    private VBox container;
    private String value;
    private Consumer<String> newValueConsumer;

    public LabeledTextField(String labelText, String value, Consumer<String> newValueConsumer) {
        this.labelText = labelText;
        this.container = new VBox();
        this.label = new Label();
        this.textField = new TextField();
        this.value = value;
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
