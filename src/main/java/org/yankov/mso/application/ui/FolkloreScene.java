package org.yankov.mso.application.ui;

import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import org.yankov.mso.application.ApplicationContext;
import org.yankov.mso.application.ui.input.FolkloreInputTab;

public class FolkloreScene {

    private static final String CLASS_NAME = FolkloreScene.class.getName();

    public static final String TAB_INPUT = CLASS_NAME + "-tab-input";
    public static final String TAB_OUTPUT = CLASS_NAME + "-tab-output";

    private StackPane root;
    private Scene scene;

    public FolkloreScene() {
        this.root = new StackPane();
        this.scene = new Scene(root);
    }

    public void layout() {
        BorderPane borderPane = new BorderPane();
        borderPane.setCenter(createTabPane());
        borderPane.setBottom(createConsole());

        root.getChildren().add(borderPane);
    }

    public Scene getScene() {
        return scene;
    }

    private Pane createConsole() {
        ApplicationConsole.getInstance().layout();
        return ApplicationConsole.getInstance().getContainer();
    }

    private Node createTabPane() {
        UserInterfaceControls inputTab = new FolkloreInputTab();
        inputTab.layout();

        TabPane tabPane = new TabPane();
        tabPane.getTabs().add(createTab(TAB_INPUT, ApplicationContext.getInstance().getFolkloreResourceBundle()
                                                                     .getString(TAB_INPUT), false,
                                        inputTab.getContainer()));
        tabPane.getTabs().add(createTab(TAB_OUTPUT, ApplicationContext.getInstance().getFolkloreResourceBundle()
                                                                      .getString(TAB_OUTPUT), false, null));

        return tabPane;
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
