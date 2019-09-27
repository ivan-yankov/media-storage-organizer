package org.yankov.mso.application.ui.tabs;

import java.text.MessageFormat;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;

import org.yankov.mso.application.ApplicationContext;
import org.yankov.mso.application.UserInterfaceControls;
import org.yankov.mso.application.ui.controls.FolkloreComboBoxFactory;
import org.yankov.mso.application.ui.controls.LabeledComboBox;
import org.yankov.mso.application.ui.controls.LabeledTextField;
import org.yankov.mso.application.ui.tabs.buttons.Buttons;
import org.yankov.mso.application.ui.tabs.buttons.ButtonsFactory;
import org.yankov.mso.datamodel.DurationConverter;
import org.yankov.mso.datamodel.FolklorePiece;
import org.yankov.mso.datamodel.FolklorePieceProperties;
import org.yankov.mso.datamodel.Operator;
import org.yankov.mso.datamodel.PieceProperties;
import org.yankov.mso.datamodel.PiecePropertiesUtils;
import org.yankov.mso.datamodel.Variable;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TitledPane;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

public class FolkloreSearchTab implements UserInterfaceControls {

    private static final String CLASS_NAME = FolkloreSearchTab.class.getName();

    public static final String VARIABLE = CLASS_NAME + "-variable";
    public static final String OPERATOR = CLASS_NAME + "-operator";
    public static final String VALUE = CLASS_NAME + "-value";
    public static final String BTN_SEARCH = CLASS_NAME + "-btn-search";
    public static final String TOTAL_ITEMS_FOUND = CLASS_NAME + "-number-items-found";
    public static final String PLAY_NEXT = CLASS_NAME + "-play-next";
    public static final String PLAY_RANDOM = CLASS_NAME + "-play-random";
    public static final String FILTER = CLASS_NAME + "-filter";

    private static final Double SEARCH_SPACE = 25.0;
    private static final Insets SEARCH_INSETS = new Insets(10.0, 20.0, 10.0, 20.0);

    private static final Double PLAY_OPTIONS_SPACE = 10.0;
    private static final Insets PLAY_OPTIONS_INSETS = new Insets(5.0);
    private static final int NUMBER_OF_FILTERS = 3;

    private final ResourceBundle resourceBundle = ApplicationContext.getInstance().getFolkloreResourceBundle();

    private FolklorePieceTable table;

    private VBox container;
    private List<Filter<FolklorePiece>> filters;
    private CheckBox playNext;
    private CheckBox playRandom;

    public FolkloreSearchTab() {
        this.container = new VBox();
        this.filters = new ArrayList<>();
    }

    @Override
    public void layout() {
        createTable();

        HBox buttonsContainer = new HBox();
        buttonsContainer.getChildren().add(createButtons());
        buttonsContainer.getChildren().add(createPlayOptions());

        container.getChildren().add(buttonsContainer);
        for (int i = 0; i < NUMBER_OF_FILTERS; i++) {
            String filter = resourceBundle.getString(FILTER);
            container.getChildren().add(createSearchControls(i == 0, filter + " " + Integer.toString(i + 1)));
        }

        VBox tableContainer = new VBox();
        VBox.setVgrow(tableContainer, Priority.ALWAYS);
        tableContainer.getChildren().add(table.getContainer());
        container.getChildren().add(tableContainer);
    }

    @Override
    public Pane getContainer() {
        return container;
    }

    private Pane createPlayOptions() {
        playNext = new CheckBox(resourceBundle.getString(PLAY_NEXT));
        playRandom = new CheckBox(resourceBundle.getString(PLAY_RANDOM));

        VBox playOptionsContainer = new VBox();
        playOptionsContainer.setSpacing(PLAY_OPTIONS_SPACE);
        playOptionsContainer.setPadding(PLAY_OPTIONS_INSETS);
        playOptionsContainer.getChildren().add(playNext);
        playOptionsContainer.getChildren().add(playRandom);

        return playOptionsContainer;
    }

    private TitledPane createSearchControls(boolean isFirst, String title) {
        LabeledComboBox<Variable<FolklorePiece>> variables = FolkloreComboBoxFactory.createSearchVariable();
        variables.layout();

        LabeledComboBox<Operator> operators = FolkloreComboBoxFactory.createSearchOperators();
        operators.layout();

        LabeledTextField value = new LabeledTextField(resourceBundle.getString(VALUE), "*");
        value.getTextField().setOnKeyReleased(this::valueKeyTyped);
        value.layout();

        Filter<FolklorePiece> filter = new Filter<>(variables, operators, value);
        filters.add(filter);

        HBox searchContainer = new HBox();
        HBox.setHgrow(value.getContainer(), Priority.ALWAYS);
        searchContainer.setPadding(SEARCH_INSETS);
        searchContainer.setSpacing(SEARCH_SPACE);
        searchContainer.setAlignment(Pos.BOTTOM_LEFT);
        searchContainer.getChildren().add(variables.getContainer());
        searchContainer.getChildren().add(operators.getContainer());
        searchContainer.getChildren().add(value.getContainer());

        if (isFirst) {
            Button btnSearch = new Button();
            btnSearch.setText(resourceBundle.getString(BTN_SEARCH));
            btnSearch.setOnAction(this::handleBtnSearch);
            searchContainer.getChildren().add(btnSearch);
        }

        TitledPane titledPane = new TitledPane(title, searchContainer);
        titledPane.setExpanded(isFirst);

        return titledPane;
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
                .createFolkloreSearchTabButtons(table.getTableView(), () -> playNext.isSelected(),
                                                () -> playRandom.isSelected());
        buttons.layout();
        return buttons.getContainer();
    }

    private void handleBtnSearch(ActionEvent event) {
        search();
    }

    private void search() {
        List<FolklorePiece> pieces = ApplicationContext.getInstance().getFolkloreEntityCollections().getPieces();

        for (Filter<FolklorePiece> filter : filters) {
            pieces = applyFilter(pieces, filter);
        }

        ObservableList<FolklorePieceProperties> properties = FXCollections.observableArrayList();
        pieces.forEach(piece -> properties.add(PiecePropertiesUtils.createPropertiesFromFolklorePiece(piece)));

        table.getTableView().getItems().clear();
        table.getTableView().getItems().addAll(properties);

        Duration totalDuration = calculateTotalDuration(properties);
        String msg = MessageFormat
                .format(resourceBundle.getString(TOTAL_ITEMS_FOUND), Integer.toString(properties.size()),
                        convertDuration(totalDuration));
        ApplicationContext.getInstance().getLogger().log(Level.INFO, msg);
    }

    private List<FolklorePiece> applyFilter(List<FolklorePiece> pieces, Filter<FolklorePiece> filter) {
        Variable<FolklorePiece> variable = filter.getVariables().getValue();
        Operator operator = filter.getOperators().getValue();
        String searchValue = filter.getValue().getTextField().getText();

        List<FolklorePiece> piecesFound;
        if (searchValue.equals("*")) {
            piecesFound = pieces;
        } else {
            piecesFound = operator.match(pieces, variable, searchValue);
        }

        return piecesFound;
    }

    private Duration calculateTotalDuration(List<? extends PieceProperties> pieces) {
        Duration total = Duration.ZERO;
        for (PieceProperties piece : pieces) {
            total = total.plus(piece.getDuration());
        }
        return total;
    }

    private String convertDuration(Duration duration) {
        String format = "%d:%02d:%02d";
        return String.format(format, duration.toHours(), DurationConverter.toMinutesPart(duration),
                             DurationConverter.toSecondsPart(duration));
    }

}
