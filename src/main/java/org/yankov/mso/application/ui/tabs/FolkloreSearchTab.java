package org.yankov.mso.application.ui.tabs;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import org.yankov.mso.application.ApplicationContext;
import org.yankov.mso.application.UserInterfaceControls;
import org.yankov.mso.application.ui.controls.LabeledComboBox;
import org.yankov.mso.application.ui.controls.LabeledTextField;
import org.yankov.mso.application.ui.converters.OperatorStringConverter;
import org.yankov.mso.application.ui.converters.VariableStringConverter;
import org.yankov.mso.datamodel.FolkloreSearchFactory;
import org.yankov.mso.datamodel.Operator;
import org.yankov.mso.datamodel.Variable;

import java.util.ResourceBundle;

public class FolkloreSearchTab implements UserInterfaceControls {

    private static final String CLASS_NAME = FolkloreSearchTab.class.getName();

    public static final String VARIABLE = CLASS_NAME + "-variable";
    public static final String OPERATOR = CLASS_NAME + "-operator";
    public static final String VALUE = CLASS_NAME + "-value";
    public static final String BTN_SEARCH = CLASS_NAME + "-btn-search";

    private static final Double SPACE = 25.0;
    private static final Insets INSETS = new Insets(25.0);
    private static final Double SEARCH_VALUE_WIDTH = 650.0;

    private final ResourceBundle resourceBundle = ApplicationContext.getInstance().getFolkloreResourceBundle();

    private VBox container;

    public FolkloreSearchTab() {
        this.container = new VBox();
    }

    @Override
    public void layout() {
        container.setPadding(INSETS);
        container.setSpacing(SPACE);
        container.getChildren().add(createSearchControls());

        HBox tableContainer = new HBox();
        tableContainer.setSpacing(SPACE);
        tableContainer.getChildren().add(createTable());
        tableContainer.getChildren().add(createButtons());
        container.getChildren().add(tableContainer);
    }

    @Override
    public Pane getContainer() {
        return container;
    }

    private Pane createSearchControls() {
        ObservableList<Variable> variablesList = FolkloreSearchFactory.createVariables();
        Variable defaultVariable = FolkloreSearchFactory.VAR_TITLE;
        LabeledComboBox<Variable> variables = new LabeledComboBox<>(resourceBundle.getString(VARIABLE), variablesList,
                                                                    defaultVariable, null,
                                                                    new VariableStringConverter(), false);
        variables.setSortItems(false);
        variables.layout();

        ObservableList<Operator> operatorsList = FolkloreSearchFactory.createOperators();
        Operator defaultOperator = FolkloreSearchFactory.OPERATOR_EQUALS;
        LabeledComboBox<Operator> operators = new LabeledComboBox<>(resourceBundle.getString(OPERATOR), operatorsList,
                                                                    defaultOperator, null,
                                                                    new OperatorStringConverter(), false);
        operators.setSortItems(false);
        operators.layout();

        LabeledTextField value = new LabeledTextField(resourceBundle.getString(VALUE), "*", null);
        value.getTextField().setPrefWidth(SEARCH_VALUE_WIDTH);
        value.layout();

        Button btnSearch = new Button();
        btnSearch.setText(resourceBundle.getString(BTN_SEARCH));
        btnSearch.setOnAction(this::handleBtnSearch);

        HBox searchContainer = new HBox();
        searchContainer.setSpacing(SPACE);
        searchContainer.setAlignment(Pos.BOTTOM_LEFT);
        searchContainer.getChildren().add(variables.getContainer());
        searchContainer.getChildren().add(operators.getContainer());
        searchContainer.getChildren().add(value.getContainer());
        searchContainer.getChildren().add(btnSearch);

        return searchContainer;
    }

    private Pane createTable() {
        return new VBox();
    }

    private Pane createButtons() {
        return new VBox();
    }

    private void handleBtnSearch(ActionEvent event) {
    }

}
