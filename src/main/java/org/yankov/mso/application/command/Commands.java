package org.yankov.mso.application.command;

import org.yankov.mso.application.ui.edit.OpenFolklorePieceEditorCommand;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class Commands {

    public static final String OPEN_FOLKLORE_PIECE_EDITOR = "open-folklore-piece-editor";

    private Map<String, Command> commands;

    public Commands() {
        this.commands = new HashMap<>();
    }

    public void initialize() {
        commands.put(OPEN_FOLKLORE_PIECE_EDITOR, new OpenFolklorePieceEditorCommand());
    }

    public Optional<Command> getCommand(String command) {
        Command c = commands.get(command);
        return c != null ? Optional.of(c) : Optional.empty();
    }

}
