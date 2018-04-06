package org.yankov.mso.application.ui.tabs;

import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import org.yankov.mso.application.UserInterfaceControls;
import org.yankov.mso.application.ui.tabs.buttons.Buttons;
import org.yankov.mso.application.ui.tabs.buttons.ButtonsFactory;
import org.yankov.mso.datamodel.FolklorePiece;
import org.yankov.mso.datamodel.FolklorePieceProperties;

public class FolkloreInputTab implements UserInterfaceControls {

    private VBox container;

    public FolkloreInputTab() {
        this.container = new VBox();
    }

    @Override
    public void layout() {
        FolklorePieceTable table = new FolklorePieceTable(true);

        Buttons<FolklorePieceProperties, FolklorePiece> buttons = ButtonsFactory
                .createFolkloreInputTabButtons(table.getTableView(), () -> false, () -> false);
        buttons.layout();
        container.getChildren().add(buttons.getContainer());

        table.layout();
        VBox.setVgrow(table.getContainer(), Priority.ALWAYS);
        container.getChildren().add(table.getContainer());
    }

    @Override
    public Pane getContainer() {
        return container;
    }

}
