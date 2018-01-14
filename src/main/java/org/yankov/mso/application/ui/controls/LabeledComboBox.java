package org.yankov.mso.application.ui.controls;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Bounds;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Popup;
import javafx.util.StringConverter;
import org.yankov.mso.application.UserInterfaceControls;

import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class LabeledComboBox<T> implements UserInterfaceControls {

    private static final Double DEFAULT_PREF_WIDTH = 250.0;

    private String labelText;
    private ObservableList<T> originalItems;
    private T value;
    private Consumer<T> newValueConsumer;
    private StringConverter<T> converter;
    private boolean editable;
    private VBox container;
    private Label label;
    private ComboBox<T> comboBox;
    private Popup popup;
    private TextField filterTextField;
    private StringBuilder filterText;

    public LabeledComboBox(String labelText, ObservableList<T> items, T value, Consumer<T> newValueConsumer,
                           StringConverter<T> converter, boolean editable) {
        this.labelText = labelText;
        this.originalItems = items.sorted();
        this.value = value;
        this.newValueConsumer = newValueConsumer;
        this.converter = converter;
        this.editable = editable;
        this.container = new VBox();
        this.label = new Label();
        this.comboBox = new ComboBox<>();
        this.popup = new Popup();
        this.filterTextField = new TextField();
        this.filterText = new StringBuilder();
    }

    public ComboBox<T> getComboBox() {
        return comboBox;
    }

    @Override
    public void layout() {
        label.setText(labelText);
        container.getChildren().add(label);

        comboBox.setEditable(editable);
        comboBox.setPrefWidth(DEFAULT_PREF_WIDTH);
        comboBox.setConverter(converter);
        comboBox.setItems(originalItems);
        comboBox.setValue(value);
        comboBox.setOnKeyTyped(this::handleKeyTyped);
        comboBox.setOnKeyReleased(this::handleKeyReleased);
        comboBox.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) {
                resetFilter();
            }
        });

        if (newValueConsumer != null) {
            comboBox.valueProperty().addListener((observable, oldValue, newValue) -> {
                newValueConsumer.accept(newValue);
                resetFilter();
            });
        }

        filterTextField.setDisable(true);
        popup.getContent().add(filterTextField);

        container.getChildren().add(comboBox);
    }

    @Override
    public Pane getContainer() {
        return container;
    }

    private void filterItems() {
        Stream<T> filteredItemsStream = originalItems.stream().filter(item -> converter.toString(item).toLowerCase()
                                                                                       .contains(
                                                                                               filterText.toString()));
        List<T> filteredItemsList = filteredItemsStream.collect(Collectors.toList());
        ObservableList<T> filteredItems = FXCollections.observableList(filteredItemsList);
        comboBox.setItems(filterText.length() == 0 ? originalItems : filteredItems.sorted());
    }

    private void resetFilter() {
        comboBox.setItems(originalItems);
        filterText.setLength(0);
        popup.hide();
    }

    private void showPopup() {
        Bounds bounds = comboBox.localToScreen(comboBox.getBoundsInLocal());
        double x = bounds.getMinX();
        double y = bounds.getMinY() - comboBox.getHeight() - label.getHeight();
        popup.show(comboBox, x, y);
    }

    private void handleKeyTyped(KeyEvent event) {
        filterText.append(event.getCharacter().toLowerCase());
        filterTextField.setText(filterText.toString());
        showPopup();
        filterItems();
    }

    private void handleKeyReleased(KeyEvent event) {
        if (event.getCode().equals(KeyCode.BACK_SPACE)) {
            if (filterText.length() > 0) {
                filterText.deleteCharAt(filterText.length() - 1);
            }
        }

        filterTextField.setText(filterText.toString());
        showPopup();
        filterItems();
    }

}
