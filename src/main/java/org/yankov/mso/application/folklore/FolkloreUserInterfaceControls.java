package org.yankov.mso.application.folklore;

import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.BorderPane;
import org.yankov.mso.application.generic.ApplicationContext;
import org.yankov.mso.datamodel.folklore.FolkloreResources;

public class FolkloreUserInterfaceControls {

    public static final String TAB_ID_INPUT = "tab-input";
    public static final String TAB_ID_OUTPUT = "tab-output";

    private Group root;
    private Scene scene;
    private BorderPane borderPane;
    private TabPane tabPane;

    public FolkloreUserInterfaceControls() {
        this.root = new Group();
        this.scene = new Scene(root);
        this.borderPane = new BorderPane();
        this.tabPane = new TabPane();
    }

    public Scene getScene() {
        return scene;
    }

    public void layout() {
        tabPane.getTabs().add(createTab(TAB_ID_INPUT, ApplicationContext.getInstance().getFolkloreResourceBundle()
                                                                        .getString(FolkloreResources.UI_TAB_TEXT_INPUT),
                                        false, null));
        tabPane.getTabs().add(createTab(TAB_ID_OUTPUT, ApplicationContext.getInstance().getFolkloreResourceBundle()
                                                                         .getString(
                                                                                 FolkloreResources.UI_TAB_TEXT_OUTPUT),
                                        false, null));
        borderPane.setCenter(tabPane);
        root.getChildren().add(borderPane);
    }

    private Tab createTab(String id, String text, boolean closable, Node content) {
        Tab tab = new Tab();

        tab.setId(id);
        tab.setText(text);
        tab.setClosable(closable);
        tab.setContent(content);

        return tab;
    }

}
