package org.yankov.mso.application.ui.edit;

import javafx.collections.ObservableList;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import org.yankov.mso.application.UserInterfaceControls;

import java.util.function.Function;

public class LabeledComboBox<T> implements UserInterfaceControls {

    private static final Double DEFAULT_PREF_WIDTH = 250.0;

    private String labelText;
    private VBox container;
    private ObservableList<T> items;
    private T value;
    private Function<T, String> itemToString;

    public LabeledComboBox(String labelText, ObservableList<T> items, T value, Function<T, String> itemToString) {
        this.labelText = labelText;
        this.container = new VBox();
        this.items = items;
        this.value = value;
        this.itemToString = itemToString;
    }

    @Override
    public void layout() {
        Label label = new Label();
        label.setText(labelText);
        container.getChildren().add(label);

        ComboBox<T> comboBox = new ComboBox<>();
        comboBox.setEditable(false);
        comboBox.setPrefWidth(DEFAULT_PREF_WIDTH);
        comboBox.setItems(items.sorted());
        comboBox.setValue(value);
        comboBox.setCellFactory(listView -> new ListCell<>() {

            @Override
            protected void updateItem(T item, boolean empty) {
                if (empty) {
                    setText(null);
                } else {
                    setText(itemToString.apply(item));
                }
            }

        });
        container.getChildren().add(comboBox);
    }

    @Override
    public Pane getContainer() {
        return container;
    }

}
