package org.yankov.mso.application.ui;

import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import org.yankov.mso.application.ApplicationContext;
import org.yankov.mso.application.Form;
import org.yankov.mso.application.UserInterfaceControls;
import org.yankov.mso.application.ui.input.FolkloreInputTab;

public class FolkloreMainForm implements Form {

    private static final String CLASS_NAME = FolkloreMainForm.class.getName();

    public static final String TAB_INPUT = CLASS_NAME + "-tab-input";
    public static final String TAB_OUTPUT = CLASS_NAME + "-tab-output";

    private Stage stage;

    public FolkloreMainForm(Stage stage) {
        this.stage = stage;
    }

    @Override
    public void createControls() {
        StackPane root = new StackPane();

        BorderPane borderPane = new BorderPane();
        borderPane.setCenter(createTabPane());
        borderPane.setBottom(createConsole());

        root.getChildren().add(borderPane);

        stage.setScene(new Scene(root));
    }

    @Override
    public void show() {
        stage.show();
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
