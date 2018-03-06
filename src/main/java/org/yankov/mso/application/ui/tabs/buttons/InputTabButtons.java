package org.yankov.mso.application.ui.tabs.buttons;

import javafx.scene.control.Button;
import javafx.scene.control.TableView;
import org.yankov.mso.datamodel.PieceProperties;

import java.util.ArrayList;
import java.util.List;

public class InputTabButtons<T extends PieceProperties> extends Buttons<T> {

    public InputTabButtons(TableView<T> table) {
        super(table);
    }

    @Override
    protected List<Button> selectButtons() {
        List<Button> buttons = new ArrayList<>();

        buttons.add(allButtons.get(BTN_ADD));
        buttons.add(allButtons.get(BTN_REMOVE));
        buttons.add(allButtons.get(BTN_COPY));
        buttons.add(allButtons.get(BTN_CLEAR));
        buttons.add(allButtons.get(BTN_LOAD_ALBUM_TRACKS));
        buttons.add(allButtons.get(BTN_EDIT_PROPERTIES));
        buttons.add(allButtons.get(BTN_PLAYER_RUN));
        buttons.add(allButtons.get(BTN_UPLOAD));

        return buttons;
    }

}
