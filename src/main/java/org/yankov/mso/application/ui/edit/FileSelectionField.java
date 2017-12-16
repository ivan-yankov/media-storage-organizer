package org.yankov.mso.application.ui.edit;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import org.yankov.mso.application.UserInterfaceControls;

public class FileSelectionField implements UserInterfaceControls {

    private String labelText;
    private Label label;
    private TextField textField;
    private VBox container;

    public FileSelectionField(String labelText) {
        this.labelText = labelText;
        this.container = new VBox();
        this.label = new Label();
        this.textField = new TextField();
    }

    @Override
    public void layout() {
        label.setText(labelText);
        container.getChildren().add(label);

        HBox selectionContainer = new HBox();
        container.getChildren().add(selectionContainer);

        selectionContainer.getChildren().add(textField);
        HBox.setHgrow(textField, Priority.ALWAYS);

        Button button = new Button();
        button.setText("...");
        selectionContainer.getChildren().add(button);
    }

    @Override
    public Pane getContainer() {
        return container;
    }

}
