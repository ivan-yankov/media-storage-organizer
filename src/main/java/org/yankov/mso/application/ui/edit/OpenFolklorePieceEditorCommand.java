package org.yankov.mso.application.ui.edit;

import javafx.scene.control.TableView;
import org.yankov.mso.application.Form;
import org.yankov.mso.application.command.Command;
import org.yankov.mso.application.command.InvalidCommandArgumentsException;
import org.yankov.mso.application.ui.datamodel.FolklorePieceProperties;

public class OpenFolklorePieceEditorCommand implements Command {

    @Override
    public void execute(Object... commandArgs) throws InvalidCommandArgumentsException {
        if (commandArgs == null || commandArgs.length != 1) {
            throw new InvalidCommandArgumentsException(1, "TableView");
        }

        TableView table = (TableView) commandArgs[0];

        int selectedIndex = table.getSelectionModel().getSelectedIndex();
        if (selectedIndex < 0) {
            return;
        }

        Form editor = new FolklorePieceEditor((FolklorePieceProperties) table.getItems().get(selectedIndex));
        editor.createControls();
        editor.show();
    }

}
