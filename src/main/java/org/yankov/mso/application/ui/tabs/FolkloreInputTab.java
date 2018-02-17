package org.yankov.mso.application.ui.tabs;

import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import org.yankov.mso.application.UserInterfaceControls;

public class FolkloreInputTab implements UserInterfaceControls {

    private HBox container;

    public FolkloreInputTab() {
        this.container = new HBox();
    }

    @Override
    public void layout() {
        FolklorePieceTable table = new FolklorePieceTable();
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
