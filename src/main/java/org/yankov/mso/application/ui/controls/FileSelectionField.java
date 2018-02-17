package org.yankov.mso.application.ui.controls;

import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import org.yankov.mso.application.ApplicationContext;
import org.yankov.mso.application.UserInterfaceControls;
import org.yankov.mso.application.ui.tabs.FolkloreInputButtons;
import org.yankov.mso.application.utils.FxUtils;

import java.io.File;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.function.Consumer;

public class FileSelectionField implements UserInterfaceControls {

    private final ResourceBundle resourceBundle = ApplicationContext.getInstance().getFolkloreResourceBundle();

    private String labelText;
    private Label label;
    private TextField textField;
    private VBox container;
    private File value;
    private Consumer<File> newValueConsumer;

    public FileSelectionField(String labelText, File value, Consumer<File> newValueConsumer) {
        this.labelText = labelText;
        this.container = new VBox();
        this.label = new Label();
        this.textField = new TextField();
        this.value = value;
        this.newValueConsumer = newValueConsumer;
    }

    @Override
    public void layout() {
        label.setText(labelText);
        container.getChildren().add(label);

        HBox selectionContainer = new HBox();
        container.getChildren().add(selectionContainer);

        textField.setEditable(false);
        String fileName = value != null ? value.getName() : "";
        textField.setText(fileName);
        HBox.setHgrow(textField, Priority.ALWAYS);
        selectionContainer.getChildren().add(textField);

        Button button = new Button();
        button.setText("...");
        button.setOnAction(this::handleButton);
        selectionContainer.getChildren().add(button);
    }

    @Override
    public Pane getContainer() {
        return container;
    }

    private void handleButton(ActionEvent event) {
        FileChooser.ExtensionFilter filter = new FileChooser.ExtensionFilter(
                resourceBundle.getString(FolkloreInputButtons.FLAC_FILTER_NAME),
                resourceBundle.getString(FolkloreInputButtons.FLAC_FILTER_EXT));
        Optional<List<File>> selection = FxUtils
                .selectFiles(resourceBundle.getString(FolkloreInputButtons.SELECT_AUDIO_FILES), true, filter);

        selection.ifPresent(files -> {
            newValueConsumer.accept(files.get(0));
            textField.setText(files.get(0).getName());
        });
    }

}
