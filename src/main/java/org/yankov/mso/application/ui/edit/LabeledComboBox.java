package org.yankov.mso.application.ui.edit;

import javafx.collections.ObservableList;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.util.StringConverter;
import org.yankov.mso.application.UserInterfaceControls;

import java.util.function.Consumer;

public class LabeledComboBox<T> implements UserInterfaceControls {

    private static final Double DEFAULT_PREF_WIDTH = 250.0;

    private String labelText;
    private VBox container;
    private ObservableList<T> items;
    private T value;
    private Consumer<T> newValueConsumer;
    private StringConverter<T> converter;

    public LabeledComboBox(String labelText, ObservableList<T> items, T value, Consumer<T> newValueConsumer,
                           StringConverter<T> converter) {
        this.labelText = labelText;
        this.container = new VBox();
        this.items = items;
        this.value = value;
        this.newValueConsumer = newValueConsumer;
        this.converter = converter;
    }

    @Override
    public void layout() {
        Label label = new Label();
        label.setText(labelText);
        container.getChildren().add(label);

        ComboBox<T> comboBox = new ComboBox<>();
        comboBox.setEditable(false);
        comboBox.setPrefWidth(DEFAULT_PREF_WIDTH);
        comboBox.setConverter(converter);
        comboBox.setItems(items.sorted());
        comboBox.setValue(value);
        comboBox.valueProperty().addListener((observable, oldValue, newValue) -> newValueConsumer.accept(newValue));
        container.getChildren().add(comboBox);
    }

    @Override
    public Pane getContainer() {
        return container;
    }

}
