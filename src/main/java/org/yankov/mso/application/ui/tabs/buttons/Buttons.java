package org.yankov.mso.application.ui.tabs.buttons;

import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.TableView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import org.yankov.mso.application.ApplicationContext;
import org.yankov.mso.application.UserInterfaceControls;
import org.yankov.mso.application.utils.FlacProcessor;
import org.yankov.mso.datamodel.DatabaseUploader;
import org.yankov.mso.datamodel.PieceProperties;

import java.util.List;
import java.util.ResourceBundle;
import java.util.function.Supplier;
import java.util.function.UnaryOperator;

public abstract class Buttons<T extends PieceProperties> implements UserInterfaceControls {

    protected abstract List<Button> createButtons();

    protected final ResourceBundle resourceBundle = ApplicationContext.getInstance().getFolkloreResourceBundle();
    protected final TableView<T> table;
    protected final FlacProcessor flacProcessor;

    protected Supplier<T> itemCreator;
    protected UnaryOperator<T> itemCopier;
    protected DatabaseUploader<T> databaseUploader;

    private static final Double SPACE = 25.0;
    private static final Insets INSETS = new Insets(25.0);
    private static final Double MIN_WIDTH = 250.0;

    private VBox container;

    public Buttons(TableView<T> table) {
        this.table = table;
        this.container = new VBox();
        this.flacProcessor = new FlacProcessor();
    }

    public void setItemCreator(Supplier<T> itemCreator) {
        this.itemCreator = itemCreator;
    }

    public void setItemCopier(UnaryOperator<T> itemCopier) {
        this.itemCopier = itemCopier;
    }

    public void setDatabaseUploader(DatabaseUploader<T> databaseUploader) {
        this.databaseUploader = databaseUploader;
    }

    @Override
    public void layout() {
        container.setPadding(INSETS);
        container.setSpacing(SPACE);
        container.setMinWidth(MIN_WIDTH);
        container.getChildren().addAll(createButtons());
    }

    @Override
    public Pane getContainer() {
        return container;
    }

}
