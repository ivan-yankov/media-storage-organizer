package org.yankov.mso.application.folklore;

import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.StackPane;
import org.yankov.mso.application.generic.ApplicationContext;
import org.yankov.mso.application.generic.UserInterfaceControls;

public class FolkloreScene implements UserInterfaceControls<Scene> {

    private static final String CLASS_NAME = FolkloreScene.class.getName();

    public static final String TAB_INPUT = CLASS_NAME + "-tab-input";
    public static final String TAB_OUTPUT = CLASS_NAME + "-tab-output";

    private StackPane root;
    private Scene scene;

    public FolkloreScene() {
        this.root = new StackPane();
        this.scene = new Scene(root);
    }

    @Override
    public Scene getContent() {
        return scene;
    }

    @Override
    public void layout() {
        FolkloreInputTabControls inputTabControls = new FolkloreInputTabControls();
        inputTabControls.layout();

        TabPane tabPane = new TabPane();
        tabPane.getTabs().add(createTab(TAB_INPUT, ApplicationContext.getInstance().getFolkloreResourceBundle()
                                                                     .getString(TAB_INPUT), false,
                                        inputTabControls.getContent()));
        tabPane.getTabs().add(createTab(TAB_OUTPUT, ApplicationContext.getInstance().getFolkloreResourceBundle()
                                                                      .getString(TAB_OUTPUT), false, null));

        root.getChildren().add(tabPane);
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
