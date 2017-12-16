package org.yankov.mso.application.ui.input;

import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import org.yankov.mso.application.UserInterfaceControls;

public class FolkloreInputTab implements UserInterfaceControls {

    private HBox container;

    public FolkloreInputTab() {
        this.container = new HBox();
    }

    @Override
    public void layout() {
        FolkloreInputTable table = new FolkloreInputTable();
        table.layout();
        HBox.setHgrow(table.getContainer(), Priority.ALWAYS);
        container.getChildren().add(table.getContainer());

        FolkloreInputButtons buttons = new FolkloreInputButtons(table.getTableView());
        buttons.layout();
        container.getChildren().add(buttons.getContainer());
    }

    @Override
    public Pane getContainer() {
        return container;
    }

}
