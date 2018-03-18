package org.yankov.mso.datamodel;

import org.yankov.mso.application.ApplicationContext;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.function.Supplier;
import java.util.logging.Level;

public class PiecePropertiesUtils {

    private static final String CLASS_NAME = PiecePropertiesUtils.class.getName();

    public static final String UNDEFINED_ALBUM = CLASS_NAME + "-undefined-album";

    private static final ResourceBundle resourceBundle = ApplicationContext.getInstance().getFolkloreResourceBundle();

    public static Piece createPieceFromProperties(PieceProperties properties) {
        FolklorePiece piece = new FolklorePiece();
        setPropertiesToPiece(properties, piece);
        return piece;
    }

    public static FolklorePiece createFolklorePieceFromProperties(FolklorePieceProperties properties) {
        FolklorePiece piece = new FolklorePiece();
        setPropertiesToPiece(properties, piece);
        piece.setEthnographicRegion(properties.getEthnographicRegion());
        return piece;
    }

    public static PieceProperties createPropertiesFromPiece(Piece piece) {
        PieceProperties properties = new PieceProperties();
        setPieceToProperties(piece, properties);
        return properties;
    }

    public static FolklorePieceProperties createPropertiesFromFolklorePiece(FolklorePiece piece) {
        FolklorePieceProperties properties = new FolklorePieceProperties();
        setPieceToProperties(piece, properties);
        properties.setEthnographicRegion(piece.getEthnographicRegion());
        return properties;
    }

    public static <T extends PieceProperties> T createPropertiesFromFile(Supplier<T> creator, File file) {
        T item = creator.get();
        String albumSignature = file.getParentFile().getName();
        Optional<Album> album = ApplicationContext.getInstance().getFolkloreEntityCollections()
                                                  .getAlbum(albumSignature);
        String message = resourceBundle.getString(UNDEFINED_ALBUM) + " " + albumSignature;

        if (album.isPresent()) {
            item.setAlbum(album.get());
        } else {
            ApplicationContext.getInstance().getLogger().log(Level.SEVERE, message);
        }

        item.setFile(file);

        return item;
    }

    public static PieceProperties copyPieceProperties(PieceProperties source) {
        PieceProperties dest = new PieceProperties();
        copyProperties(source, dest);
        return dest;
    }

    public static FolklorePieceProperties copyFolklorePieceProperties(FolklorePieceProperties source) {
        FolklorePieceProperties dest = new FolklorePieceProperties();
        copyProperties(source, dest);
        dest.setEthnographicRegion(source.getEthnographicRegion());
        return dest;
    }

    public static void setPropertiesToPiece(PieceProperties properties, Piece piece) {
        piece.setNote(properties.getNote());
        piece.setSource(properties.getSource());
        piece.setSoloist(properties.getSoloist());
        piece.setAuthor(properties.getAuthor());
        piece.setConductor(properties.getConductor());
        piece.setArrangementAuthor(properties.getArrangementAuthor());
        piece.setAccompanimentPerformer(properties.getAccompanimentPerformer());
        piece.setPerformer(properties.getPerformer());
        piece.setAlbum(properties.getAlbum());
        piece.setTitle(properties.getTitle());
        piece.setAlbumTrackOrder(Integer.parseInt(properties.getAlbumTrackOrder()));
        piece.setDuration(properties.getDuration());
        piece.setRecord(properties.getRecord());
    }

    private static void setPieceToProperties(Piece piece, PieceProperties properties) {
        properties.setId(piece.getId());
        properties.setNote(piece.getNote());
        properties.setSource(piece.getSource());
        properties.setSoloist(piece.getSoloist());
        properties.setAuthor(piece.getAuthor());
        properties.setConductor(piece.getConductor());
        properties.setArrangementAuthor(piece.getArrangementAuthor());
        properties.setAccompanimentPerformer(piece.getAccompanimentPerformer());
        properties.setPerformer(piece.getPerformer());
        properties.setAlbum(piece.getAlbum());
        properties.setTitle(piece.getTitle());
        properties.setAlbumTrackOrder(Integer.toString(piece.getAlbumTrackOrder()));
        properties.setDuration(piece.getDuration());
        properties.setRecord(piece.getRecord());
    }

    private static void copyProperties(PieceProperties source, PieceProperties dest) {
        dest.setId(source.getId());
        dest.setAlbum(source.getAlbum());
        dest.setAlbumTrackOrder(source.getAlbumTrackOrder());
        dest.setTitle(source.getTitle());
        dest.setPerformer(source.getPerformer());
        dest.setAccompanimentPerformer(source.getAccompanimentPerformer());
        dest.setAuthor(source.getAuthor());
        dest.setArrangementAuthor(source.getArrangementAuthor());
        dest.setConductor(source.getConductor());
        dest.setSoloist(source.getSoloist());
        dest.setDuration(source.getDuration());
        dest.setNote(source.getNote());
        dest.setSource(source.getSource());
        dest.setFile(source.getFile());
        dest.setRecord(source.getRecord());
    }

}
