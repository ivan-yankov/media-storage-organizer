package org.yankov.mso.application.command;

import javafx.scene.control.TableView;
import org.yankov.mso.application.ui.edit.OpenFolklorePieceEditorCommand;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class Commands {

    public static final String OPEN_FOLKLORE_PIECE_EDITOR = "open-folklore-piece-editor";

    private List<Command> commands;

    public Commands() {
        this.commands = new ArrayList<>();
    }

    public void initialize() {
        List<String> arguments = new ArrayList<>();
        arguments.add(TableView.class.getName());
        commands.add(new OpenFolklorePieceEditorCommand(OPEN_FOLKLORE_PIECE_EDITOR, arguments));
    }

    public Optional<Command> getCommand(String command) {
        return commands.stream().filter(c -> c.getName().equals(command)).findFirst();
    }

}
