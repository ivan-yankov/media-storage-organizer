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
import org.yankov.mso.application.ui.tabs.FolkloreInputArtifactsTab;
import org.yankov.mso.application.ui.tabs.FolkloreInputTab;
import org.yankov.mso.application.ui.tabs.FolkloreSearchTab;

public class FolkloreMainForm implements Form {

    private static final String CLASS_NAME = FolkloreMainForm.class.getName();

    public static final String TAB_INPUT = CLASS_NAME + "-tab-tabs";
    public static final String TAB_OUTPUT = CLASS_NAME + "-tab-output";
    public static final String TAB_INPUT_ARTIFACTS = CLASS_NAME + "-tab-tabs-artifacts";

    private Stage stage;
    private TabPane tabPane;

    public FolkloreMainForm(Stage stage) {
        this.stage = stage;
        this.tabPane = createTabPane();
    }

    @Override
    public void createControls() {
        StackPane root = new StackPane();

        BorderPane borderPane = new BorderPane();
        borderPane.setCenter(tabPane);
        borderPane.setBottom(createConsole());

        root.getChildren().add(borderPane);

        Scene scene = new Scene(root);
        scene.setUserData(this);
        stage.setScene(scene);
    }

    @Override
    public void show() {
        stage.show();
    }

    @Override
    public void close() {
        stage.close();
    }

    @Override
    public void disable(boolean value) {
        tabPane.setDisable(value);
    }

    private Pane createConsole() {
        ApplicationConsole.getInstance().layout();
        return ApplicationConsole.getInstance().getContainer();
    }

    private TabPane createTabPane() {
        UserInterfaceControls inputTab = new FolkloreInputTab();
        inputTab.layout();

        UserInterfaceControls inputArtifactsTab = new FolkloreInputArtifactsTab();
        inputArtifactsTab.layout();

        UserInterfaceControls searchTab = new FolkloreSearchTab();
        searchTab.layout();

        TabPane pane = new TabPane();
        pane.getTabs().add(createTab(TAB_INPUT, ApplicationContext.getInstance().getFolkloreResourceBundle()
                .getString(TAB_INPUT), false,
            inputTab.getContainer()));

        pane.getTabs().add(createTab(TAB_INPUT_ARTIFACTS,
            ApplicationContext.getInstance().getFolkloreResourceBundle()
                .getString(TAB_INPUT_ARTIFACTS), false,
            inputArtifactsTab.getContainer()));

        pane.getTabs().add(createTab(TAB_OUTPUT, ApplicationContext.getInstance().getFolkloreResourceBundle()
                .getString(TAB_OUTPUT), false,
            searchTab.getContainer()));

        return pane;
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
