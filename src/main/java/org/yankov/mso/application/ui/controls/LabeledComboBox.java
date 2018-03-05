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

import java.util.Comparator;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class LabeledComboBox<T> implements UserInterfaceControls {

    private static final Double DEFAULT_PREF_WIDTH = 250.0;

    private final ObservableList<T> originalItems;
    private Consumer<T> newValueConsumer;
    private StringConverter<T> converter;
    private VBox container;
    private Label label;
    private ComboBox<T> comboBox;
    private Popup popup;
    private TextField filterTextField;
    private StringBuilder filterText;

    private Comparator<T> itemComparator = (i1, i2) -> {
        String s1 = converter != null ? converter.toString(i1) : i1.toString();
        String s2 = converter != null ? converter.toString(i2) : i2.toString();
        return s1.compareToIgnoreCase(s2);
    };

    public LabeledComboBox(String labelText, ObservableList<T> items, T value, Consumer<T> newValueConsumer,
                           StringConverter<T> converter, boolean editable, boolean sortItems) {
        this.converter = converter;

        this.originalItems = FXCollections.observableArrayList();
        this.originalItems.addAll(sortItems ? items.sorted(itemComparator) : items);

        this.comboBox = new ComboBox<>();
        this.comboBox.setEditable(editable);
        this.comboBox.setConverter(converter);
        this.comboBox.setItems(originalItems);
        this.comboBox.setValue(value);

        this.newValueConsumer = newValueConsumer;
        this.container = new VBox();
        this.label = new Label(labelText);

        this.popup = new Popup();
        this.filterTextField = new TextField();
        this.filterText = new StringBuilder();
    }

    public ComboBox<T> getComboBox() {
        return comboBox;
    }

    public Comparator<T> getItemComparator() {
        return itemComparator;
    }

    @Override
    public void layout() {
        container.getChildren().add(label);

        comboBox.setPrefWidth(DEFAULT_PREF_WIDTH);
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
        List<T> filteredItemsList = originalItems.stream().filter(item -> converter.toString(item).toLowerCase()
                                                                                   .contains(filterText.toString()
                                                                                                       .toLowerCase()))
                                                 .collect(Collectors.toList());
        ObservableList<T> filteredItems = FXCollections.observableList(filteredItemsList);
        comboBox.setItems(
                filterText.length() == 0 ? originalItems.sorted(itemComparator) : filteredItems.sorted(itemComparator));
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
