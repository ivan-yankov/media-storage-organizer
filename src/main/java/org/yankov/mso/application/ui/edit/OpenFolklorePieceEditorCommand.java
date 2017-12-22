package org.yankov.mso.application.ui.edit;

import javafx.scene.control.TableView;
import org.yankov.mso.application.Form;
import org.yankov.mso.application.command.Command;
import org.yankov.mso.application.ui.datamodel.FolklorePieceProperties;

import java.util.List;

public class OpenFolklorePieceEditorCommand extends Command {

    public OpenFolklorePieceEditorCommand(String name, List<String> argumentTypeNames) {
        super(name, argumentTypeNames);
    }

    @Override
    public void execute(Object... commandArgs) {
        TableView<FolklorePieceProperties> table = (TableView<FolklorePieceProperties>) commandArgs[0];

        int selectedIndex = table.getSelectionModel().getSelectedIndex();
        if (selectedIndex < 0) {
            return;
        }

        Form editor = new FolklorePieceEditor(table, selectedIndex);
        editor.createControls();
        editor.show();
    }

}
