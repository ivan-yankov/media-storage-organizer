package org.yankov.mso.application.ui.tabs;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import org.yankov.mso.application.ApplicationContext;
import org.yankov.mso.application.UserInterfaceControls;
import org.yankov.mso.application.ui.controls.LabeledComboBox;
import org.yankov.mso.application.ui.controls.LabeledTextField;
import org.yankov.mso.application.ui.converters.OperatorStringConverter;
import org.yankov.mso.application.ui.converters.VariableStringConverter;
import org.yankov.mso.application.ui.tabs.buttons.Buttons;
import org.yankov.mso.application.ui.tabs.buttons.ButtonsFactory;
import org.yankov.mso.datamodel.*;

import java.util.List;
import java.util.ResourceBundle;

public class FolkloreSearchTab implements UserInterfaceControls {

    private static final String CLASS_NAME = FolkloreSearchTab.class.getName();

    public static final String VARIABLE = CLASS_NAME + "-variable";
    public static final String OPERATOR = CLASS_NAME + "-operator";
    public static final String VALUE = CLASS_NAME + "-value";
    public static final String BTN_SEARCH = CLASS_NAME + "-btn-search";

    private static final Double SPACE = 25.0;
    private static final Insets SEARCH_INSETS = new Insets(20.0, 20.0, 0.0, 20.0);

    private final ResourceBundle resourceBundle = ApplicationContext.getInstance().getFolkloreResourceBundle();

    private FolklorePieceTable table;

    private VBox container;
    private LabeledComboBox<Variable<FolklorePiece>> variables;
    private LabeledComboBox<Operator> operators;
    private LabeledTextField value;

    public FolkloreSearchTab() {
        this.container = new VBox();
    }

    @Override
    public void layout() {
        container.setSpacing(SPACE);
        container.getChildren().add(createSearchControls());

        HBox tableContainer = new HBox();
        VBox.setVgrow(tableContainer, Priority.ALWAYS);
        tableContainer.getChildren().add(createTable());
        tableContainer.getChildren().add(createButtons());
        container.getChildren().add(tableContainer);
    }

    @Override
    public Pane getContainer() {
        return container;
    }

    private Pane createSearchControls() {
        variables = new LabeledComboBox<>(resourceBundle.getString(VARIABLE),
                                          FolkloreSearchFactory.createFolkloreVariables(),
                                          FolkloreSearchFactory.VAR_TITLE, null, new VariableStringConverter<>(), false,
                                          false);
        variables.layout();

        operators = new LabeledComboBox<>(resourceBundle.getString(OPERATOR), FolkloreSearchFactory.createOperators(),
                                          FolkloreSearchFactory.OPERATOR_EQUALS, null, new OperatorStringConverter(),
                                          false, false);
        operators.layout();

        value = new LabeledTextField(resourceBundle.getString(VALUE), "*", null);
        value.layout();

        Button btnSearch = new Button();
        btnSearch.setText(resourceBundle.getString(BTN_SEARCH));
        btnSearch.setOnAction(this::handleBtnSearch);

        HBox searchContainer = new HBox();
        HBox.setHgrow(searchContainer, Priority.ALWAYS);
        HBox.setHgrow(value.getContainer(), Priority.ALWAYS);
        searchContainer.setPadding(SEARCH_INSETS);
        searchContainer.setSpacing(SPACE);
        searchContainer.setAlignment(Pos.BOTTOM_LEFT);
        searchContainer.getChildren().add(variables.getContainer());
        searchContainer.getChildren().add(operators.getContainer());
        searchContainer.getChildren().add(value.getContainer());
        searchContainer.getChildren().add(btnSearch);

        return searchContainer;
    }

    private Pane createTable() {
        table = new FolklorePieceTable();
        table.layout();
        HBox.setHgrow(table.getContainer(), Priority.ALWAYS);
        return table.getContainer();
    }

    private Pane createButtons() {
        Buttons<FolklorePieceProperties, FolklorePiece> buttons = ButtonsFactory
                .createFolkloreSearchTabButtons(table.getTableView());
        buttons.layout();
        return buttons.getContainer();
    }

    private void handleBtnSearch(ActionEvent event) {
        List<FolklorePiece> pieces = ApplicationContext.getInstance().getFolkloreEntityCollections().getPieces();
        Variable<FolklorePiece> variable = variables.getComboBox().getSelectionModel().getSelectedItem();
        Operator operator = operators.getComboBox().getSelectionModel().getSelectedItem();
        String searchValue = value.getTextField().getText();

        List<FolklorePiece> piecesFound;
        if (searchValue.equals("*")) {
            piecesFound = pieces;
        } else {
            piecesFound = operator.match(pieces, variable, searchValue);
        }

        ObservableList<FolklorePieceProperties> properties = FXCollections.observableArrayList();
        piecesFound.forEach(piece -> properties.add(PiecePropertiesUtils.createPropertiesFromFolklorePiece(piece)));

        table.getTableView().getItems().clear();
        table.getTableView().getItems().addAll(properties);
    }

}
