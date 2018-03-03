package org.yankov.mso.application.ui.tabs;

import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import org.yankov.mso.application.UserInterfaceControls;
import org.yankov.mso.application.ui.tabs.buttons.InputTabButtons;
import org.yankov.mso.datamodel.FolklorePieceProperties;
import org.yankov.mso.datamodel.PiecePropertiesUtils;

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

        InputTabButtons<FolklorePieceProperties> buttons = new InputTabButtons<>(table.getTableView());
        buttons.setItemCreator(FolklorePieceProperties::new);
        buttons.setItemCopier(PiecePropertiesUtils::copyFolklorePieceProperties);
        buttons.layout();
        container.getChildren().add(buttons.getContainer());
    }

    @Override
    public Pane getContainer() {
        return container;
    }

}
