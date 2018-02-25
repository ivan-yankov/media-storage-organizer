package org.yankov.mso.datamodel;

import org.yankov.mso.application.ApplicationContext;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;

public class FolklorePieceUploader implements DatabaseUploader<FolklorePieceProperties> {

    private static final String CLASS_NAME = FolklorePieceUploader.class.getName();

    public static final String FILE_LOADED = CLASS_NAME + "-file-loaded";
    public static final String PIECES_UPLOAD_COMPLETED = CLASS_NAME + "-pieces-upload-completed";

    private final ResourceBundle resourceBundle = ApplicationContext.getInstance().getFolkloreResourceBundle();

    public FolklorePieceUploader() {
    }

    @Override
    public void uploadToDatabase(List<FolklorePieceProperties> items) {
        if (items.isEmpty()) {
            return;
        }
        uploadToDataModel(items);
        ApplicationContext.getInstance().getFolkloreEntityCollections().saveEntityCollections();
        ApplicationContext.getInstance().getLogger().log(Level.INFO, resourceBundle.getString(PIECES_UPLOAD_COMPLETED));
    }

    private void uploadToDataModel(List<FolklorePieceProperties> items) {
        for (FolklorePieceProperties item : items) {
            FolklorePiece piece = PiecePropertiesUtils.createFolklorePieceFromProperties(item);
            ApplicationContext.getInstance().getFolkloreEntityCollections().addPiece(piece);
            ApplicationContext.getInstance().getLogger()
                              .log(Level.INFO, resourceBundle.getString(FILE_LOADED) + ": " + item.getFile().getName());
        }
    }

}
