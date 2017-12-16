package org.yankov.mso.application.ui.edit;

import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import org.yankov.mso.application.UserInterfaceControls;

public class LabeledComboBox<T> implements UserInterfaceControls {

    private static final Double DEFAULT_PREF_WIDTH = 250.0;

    private String labelText;
    private VBox container;

    public LabeledComboBox(String labelText) {
        this.labelText = labelText;
        this.container = new VBox();
    }

    @Override
    public void layout() {
        Label label = new Label();
        label.setText(labelText);
        container.getChildren().add(label);

        ComboBox<T> comboBox = new ComboBox<>();
        comboBox.setEditable(false);
        comboBox.setPrefWidth(DEFAULT_PREF_WIDTH);
        container.getChildren().add(comboBox);
    }

    @Override
    public Pane getContainer() {
        return container;
    }

}
