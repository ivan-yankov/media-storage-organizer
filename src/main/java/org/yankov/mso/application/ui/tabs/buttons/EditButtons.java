package org.yankov.mso.application.ui.tabs.buttons;

import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.scene.Cursor;
import javafx.scene.control.Button;
import javafx.scene.control.TableView;
import org.yankov.mso.application.ApplicationContext;
import org.yankov.mso.application.Commands;
import org.yankov.mso.datamodel.FolklorePiece;
import org.yankov.mso.datamodel.FolklorePieceProperties;
import org.yankov.mso.datamodel.PieceProperties;
import org.yankov.mso.datamodel.PiecePropertiesUtils;

import javax.sound.sampled.LineEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

public class EditButtons<T extends PieceProperties> extends Buttons<T> {

    private static final String CLASS_NAME = EditButtons.class.getName();

    public static final String BTN_EDIT_PROPERTIES = CLASS_NAME + "-btn-edit-properties";
    public static final String BTN_PLAYER_RUN = CLASS_NAME + "-btn-player-run";
    public static final String BTN_PLAYER_STOP = CLASS_NAME + "-btn-player-stop";
    public static final String BTN_UPLOAD = CLASS_NAME + "-btn-upload";
    public static final String UPLOAD_COMPLETED = CLASS_NAME + "-upload-completed";

    public EditButtons(TableView<T> table) {
        super(table);
    }

    @Override
    protected List<Button> createButtons() {
        Button btnEdit = new Button();
        btnEdit.setText(resourceBundle.getString(BTN_EDIT_PROPERTIES));
        btnEdit.setMaxWidth(Double.MAX_VALUE);
        btnEdit.setOnAction(
                event -> ApplicationContext.getInstance().executeCommand(Commands.OPEN_FOLKLORE_PIECE_EDITOR, table));

        Button btnPlay = new Button();
        btnPlay.setText(resourceBundle.getString(BTN_PLAYER_RUN));
        btnPlay.setMaxWidth(Double.MAX_VALUE);
        btnPlay.setOnAction(this::handlePlayAction);

        Button btnUpload = new Button();
        btnUpload.setText(resourceBundle.getString(BTN_UPLOAD));
        btnUpload.setMaxWidth(Double.MAX_VALUE);
        btnUpload.setOnAction(this::handleUploadAction);

        List<Button> buttons = new ArrayList<>();

        buttons.add(btnEdit);
        buttons.add(btnPlay);
        buttons.add(btnUpload);

        return buttons;
    }

    private void handlePlayAction(ActionEvent event) {
        if (table.getSelectionModel().getSelectedIndex() < 0) {
            return;
        }

        if (flacProcessor.isPlaying()) {
            flacProcessor.stop();
        } else {
            flacProcessor.addListener(e -> Platform.runLater(() -> updateButtonText((Button) event.getSource(), e)));
            flacProcessor.setFile(table.getSelectionModel().getSelectedItem().getFile());
            Thread thread = new Thread(flacProcessor::play);
            thread.start();
        }
    }

    private void updateButtonText(Button button, LineEvent event) {
        if (event.getType() == LineEvent.Type.START) {
            button.setText(resourceBundle.getString(BTN_PLAYER_STOP));
        } else if (event.getType() == LineEvent.Type.STOP) {
            button.setText(resourceBundle.getString(BTN_PLAYER_RUN));
        }
    }

    private void handleUploadAction(ActionEvent event) {
        updateDataModel();

        Task task = new Task() {

            @Override
            protected Object call() {
                ApplicationContext context = ApplicationContext.getInstance();
                context.getPrimaryStage().getScene().setCursor(Cursor.WAIT);
                context.getFolkloreEntityCollections().saveEntityCollections();
                context.getLogger().log(Level.INFO, resourceBundle.getString(UPLOAD_COMPLETED));
                context.getPrimaryStage().getScene().setCursor(Cursor.DEFAULT);
                return null;
            }

        };

        new Thread(task).start();

        table.getItems().clear();
    }

    private void updateDataModel() {
        for (T item : table.getItems()) {
            FolklorePiece piece = PiecePropertiesUtils
                    .createFolklorePieceFromProperties((FolklorePieceProperties) item);
            ApplicationContext.getInstance().getFolkloreEntityCollections().addPiece(piece);
        }
    }

}
