package org.yankov.mso.application.ui.input;

import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import org.yankov.mso.application.ui.UserInterfaceControls;

public class FolkloreInputTab implements UserInterfaceControls {

    private VBox container;

    public FolkloreInputTab() {
        this.container = new VBox();
    }

    @Override
    public void layout() {
        HBox tableWithButtons = new HBox();

        FolkloreInputTable table = new FolkloreInputTable();
        table.layout();
        HBox.setHgrow(table.getContainer(), Priority.ALWAYS);
        tableWithButtons.getChildren().add(table.getContainer());

        FolkloreInputButtons buttons = new FolkloreInputButtons(table.getTableView());
        buttons.layout();
        tableWithButtons.getChildren().add(buttons.getContainer());

        container.getChildren().add(tableWithButtons);
    }

    @Override
    public Pane getContainer() {
        return container;
    }

}
