package org.yankov.mso.application.ui.edit;

import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import org.yankov.mso.application.UserInterfaceControls;

public class LabeledTextField implements UserInterfaceControls {

    private String labelText;
    private Label label;
    private TextField textField;
    private VBox container;

    public LabeledTextField(String labelText) {
        this.labelText = labelText;
        this.container = new VBox();
        this.label = new Label();
        this.textField = new TextField();
    }

    @Override
    public void layout() {
        label.setText(labelText);
        container.getChildren().add(label);

        container.getChildren().add(textField);
    }

    @Override
    public Pane getContainer() {
        return container;
    }

}
