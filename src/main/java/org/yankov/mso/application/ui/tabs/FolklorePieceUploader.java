package org.yankov.mso.application.ui.tabs;

import org.yankov.mso.application.ApplicationContext;
import org.yankov.mso.datamodel.FolklorePieceProperties;
import org.yankov.mso.datamodel.FolklorePiece;
import org.yankov.mso.datamodel.Record;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;

public class FolklorePieceUploader {

    private static final String CLASS_NAME = FolklorePieceUploader.class.getName();
    private static final String FLAC = "FLAC";

    public static final String UNABLE_READ_FILE = CLASS_NAME + "-unable-read-file";
    public static final String FILE_LOADED = CLASS_NAME + "-file-loaded";
    public static final String PIECES_UPLOAD_COMPLETED = CLASS_NAME + "-pieces-upload-completed";

    private final List<FolklorePieceProperties> items;

    private final ResourceBundle resourceBundle = ApplicationContext.getInstance().getFolkloreResourceBundle();

    public FolklorePieceUploader(List<FolklorePieceProperties> items) {
        this.items = items;
    }

    public void uploadToDatabase() {
        if (items.isEmpty()) {
            return;
        }
        uploadToDataModel();
        ApplicationContext.getInstance().getFolkloreEntityCollections().saveEntityCollections();
        ApplicationContext.getInstance().getLogger().log(Level.INFO, resourceBundle.getString(PIECES_UPLOAD_COMPLETED));
    }

    private void uploadToDataModel() {
        for (FolklorePieceProperties item : items) {
            FolklorePiece piece = createPieceFromProperties(item);
            ApplicationContext.getInstance().getFolkloreEntityCollections().addPiece(piece);
            ApplicationContext.getInstance().getLogger()
                              .log(Level.INFO, resourceBundle.getString(FILE_LOADED) + ": " + item.getFile().getName());
        }
    }

    private FolklorePiece createPieceFromProperties(FolklorePieceProperties properties) {
        FolklorePiece piece = new FolklorePiece();

        piece.setNote(properties.getNote());
        piece.setSource(properties.getSource());
        piece.setEthnographicRegion(properties.getEthnographicRegion());
        piece.setSoloist(properties.getSoloist());
        piece.setAuthor(properties.getAuthor());
        piece.setConductor(properties.getConductor());
        piece.setArrangementAuthor(properties.getArrangementAuthor());
        piece.setAccompanimentPerformer(properties.getAccompanimentPerformer());
        piece.setPerformer(properties.getPerformer());
        piece.setAlbum(properties.getAlbum());
        piece.setTitle(properties.getTitle());
        piece.setAlbumTrackOrder(properties.getAlbumTrackOrder());
        piece.setDuration(properties.getDuration());
        piece.setRecord(loadRecordFromFile(properties.getFile()));

        return piece;
    }

    private Record loadRecordFromFile(File file) {
        Record record = new Record();

        record.setDataFormat(FLAC);

        try {
            FileInputStream in = new FileInputStream(file);
            byte[] bytes = in.readAllBytes();
            record.setBytes(bytes);
            in.close();
        } catch (IOException e) {
            ApplicationContext.getInstance().getLogger().log(Level.SEVERE,
                                                             resourceBundle.getString(UNABLE_READ_FILE) + ": " + file
                                                                     .getAbsolutePath());
        }

        return record;
    }

}
