package org.yankov.mso.application.ui.tabs;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import org.yankov.mso.application.ApplicationContext;
import org.yankov.mso.application.UserInterfaceControls;
import org.yankov.mso.application.ui.controls.FolkloreComboBoxFactory;
import org.yankov.mso.application.ui.controls.LabeledComboBox;
import org.yankov.mso.application.ui.controls.LabeledTextField;
import org.yankov.mso.application.ui.tabs.buttons.Buttons;
import org.yankov.mso.application.ui.tabs.buttons.ButtonsFactory;
import org.yankov.mso.datamodel.*;

import java.text.MessageFormat;
import java.time.Duration;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;

public class FolkloreSearchTab implements UserInterfaceControls {

    private static final String CLASS_NAME = FolkloreSearchTab.class.getName();

    public static final String VARIABLE = CLASS_NAME + "-variable";
    public static final String OPERATOR = CLASS_NAME + "-operator";
    public static final String VALUE = CLASS_NAME + "-value";
    public static final String BTN_SEARCH = CLASS_NAME + "-btn-search";
    public static final String TOTAL_ITEMS_FOUND = CLASS_NAME + "-number-items-found";

    private static final Double SPACE = 25.0;
    private static final Insets SEARCH_INSETS = new Insets(0.0, 20.0, 0.0, 20.0);

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
        createTable();

        container.setSpacing(SPACE);
        container.getChildren().add(createButtons());
        container.getChildren().add(createSearchControls());

        VBox tableContainer = new VBox();
        VBox.setVgrow(tableContainer, Priority.ALWAYS);
        tableContainer.getChildren().add(table.getContainer());
        container.getChildren().add(tableContainer);
    }

    @Override
    public Pane getContainer() {
        return container;
    }

    private Pane createSearchControls() {
        variables = FolkloreComboBoxFactory.createSearchVariable();
        variables.layout();

        operators = FolkloreComboBoxFactory.createSearchOperators();
        operators.layout();

        value = new LabeledTextField(resourceBundle.getString(VALUE), "*");
        value.getTextField().setOnKeyReleased(this::valueKeyTyped);
        value.layout();

        Button btnSearch = new Button();
        btnSearch.setText(resourceBundle.getString(BTN_SEARCH));
        btnSearch.setOnAction(this::handleBtnSearch);

        HBox searchContainer = new HBox();
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

    private void valueKeyTyped(KeyEvent event) {
        if (event.getCode().equals(KeyCode.ENTER)) {
            search();
        }
    }

    private void createTable() {
        table = new FolklorePieceTable(false);
        VBox.setVgrow(table.getContainer(), Priority.ALWAYS);
        table.layout();
    }

    private Pane createButtons() {
        Buttons<FolklorePieceProperties, FolklorePiece> buttons = ButtonsFactory
                .createFolkloreSearchTabButtons(table.getTableView());
        buttons.layout();
        return buttons.getContainer();
    }

    private void handleBtnSearch(ActionEvent event) {
        search();
    }

    private void search() {
        List<FolklorePiece> pieces = ApplicationContext.getInstance().getFolkloreEntityCollections().getPieces();
        Variable<FolklorePiece> variable = variables.getValue();
        Operator operator = operators.getValue();
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

        Duration totalDuration = calculateTotalDuration(properties);
        DurationConverter converter = new DurationConverter();
        String msg = MessageFormat.format(resourceBundle.getString(TOTAL_ITEMS_FOUND), properties.size(),
                                          converter.convertToDatabaseColumn(totalDuration));
        ApplicationContext.getInstance().getLogger().log(Level.INFO, msg);
    }

    private Duration calculateTotalDuration(List<? extends PieceProperties> pieces) {
        Duration total = Duration.ZERO;
        for (PieceProperties piece : pieces) {
            total = total.plus(piece.getDuration());
        }
        return total;
    }

}
