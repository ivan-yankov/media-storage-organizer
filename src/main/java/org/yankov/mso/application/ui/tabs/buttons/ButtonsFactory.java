package org.yankov.mso.application.ui.tabs.buttons;

import javafx.scene.control.Button;
import javafx.scene.control.TableView;
import org.yankov.mso.application.ApplicationContext;
import org.yankov.mso.application.Commands;
import org.yankov.mso.datamodel.FolklorePiece;
import org.yankov.mso.datamodel.FolklorePieceProperties;
import org.yankov.mso.datamodel.PiecePropertiesUtils;

import java.util.ArrayList;
import java.util.List;

public class ButtonsFactory {

    public static Buttons<FolklorePieceProperties, FolklorePiece> createFolkloreInputTabButtons(
            TableView<FolklorePieceProperties> table) {
        Buttons<FolklorePieceProperties, FolklorePiece> buttons = createFolkloreButtons(table);

        buttons.setSelectButtons(allButtons -> {
            List<Button> selectedButtons = new ArrayList<>();
            selectedButtons.add(allButtons.get(Buttons.BTN_ADD));
            selectedButtons.add(allButtons.get(Buttons.BTN_REMOVE));
            selectedButtons.add(allButtons.get(Buttons.BTN_COPY));
            selectedButtons.add(allButtons.get(Buttons.BTN_CLEAR));
            selectedButtons.add(allButtons.get(Buttons.BTN_LOAD_ALBUM_TRACKS));
            selectedButtons.add(allButtons.get(Buttons.BTN_EDIT_PROPERTIES));
            selectedButtons.add(allButtons.get(Buttons.BTN_PLAYER_RUN));
            selectedButtons.add(allButtons.get(Buttons.BTN_SAVE));
            return selectedButtons;
        });

        return buttons;
    }

    public static Buttons<FolklorePieceProperties, FolklorePiece> createFolkloreSearchTabButtons(
            TableView<FolklorePieceProperties> table) {
        Buttons<FolklorePieceProperties, FolklorePiece> buttons = createFolkloreButtons(table);

        buttons.setSelectButtons(allButtons -> {
            List<Button> selectedButtons = new ArrayList<>();
            selectedButtons.add(allButtons.get(Buttons.BTN_CLEAR));
            selectedButtons.add(allButtons.get(Buttons.BTN_EDIT_PROPERTIES));
            selectedButtons.add(allButtons.get(Buttons.BTN_PLAYER_RUN));
            selectedButtons.add(allButtons.get(Buttons.BTN_UPDATE));
            selectedButtons.add(allButtons.get(Buttons.BTN_EXPORT));
            return selectedButtons;
        });

        return buttons;
    }

    private static Buttons<FolklorePieceProperties, FolklorePiece> createFolkloreButtons(
            TableView<FolklorePieceProperties> table) {
        Buttons<FolklorePieceProperties, FolklorePiece> buttons = new Buttons<>();

        buttons.setResourceBundle(ApplicationContext.getInstance().getFolkloreResourceBundle());

        buttons.setEditPieceCommand(
                t -> ApplicationContext.getInstance().executeCommand(Commands.OPEN_FOLKLORE_PIECE_EDITOR, t));

        buttons.setEntityCollections(ApplicationContext.getInstance().getFolkloreEntityCollections());

        buttons.setEntityCreator(PiecePropertiesUtils::createFolklorePieceFromProperties);

        buttons.setTable(table);

        buttons.setPropertiesCreator(FolklorePieceProperties::new);

        buttons.setPropertiesCopier(PiecePropertiesUtils::copyParticularFolklorePieceProperties);

        return buttons;
    }

}
