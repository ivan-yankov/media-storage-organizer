package org.yankov.mso.application.ui.tabs.buttons;

import javafx.scene.control.Button;
import javafx.scene.control.TableView;
import org.yankov.mso.datamodel.PieceProperties;

import java.util.List;

public class SearchTabButtons<T extends PieceProperties> extends EditButtons<T> {

    public SearchTabButtons(TableView<T> table) {
        super(table);
    }

    @Override
    protected List<Button> createButtons() {
        return super.createButtons();
    }

}
