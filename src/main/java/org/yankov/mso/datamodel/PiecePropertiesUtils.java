package org.yankov.mso.datamodel;

import org.yankov.mso.application.ApplicationContext;

import java.io.File;
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

    public static void copyPieceProperties(PieceProperties source, PieceProperties dest) {
        copyProperties(source, dest, true);
    }

    public static void copyFolklorePieceProperties(FolklorePieceProperties source, FolklorePieceProperties dest) {
        copyProperties(source, dest, true);
        dest.setEthnographicRegion(source.getEthnographicRegion());
    }

    public static void copyParticularPieceProperties(PieceProperties source, PieceProperties dest) {
        copyProperties(source, dest, false);
    }

    public static void copyParticularFolklorePieceProperties(FolklorePieceProperties source,
                                                             FolklorePieceProperties dest) {
        copyProperties(source, dest, false);
        dest.setEthnographicRegion(source.getEthnographicRegion());
    }

    public static <T extends PieceProperties, U extends Piece> void setPropertiesToPiece(T properties, U piece) {
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
        piece.setAlbumTrackOrder(parseAlbumTrackOrder(properties.getAlbumTrackOrder()));
        piece.setDuration(properties.getDuration());
        piece.setRecord(properties.getRecord());

        if (properties instanceof FolklorePieceProperties) {
            FolklorePieceProperties folklorePieceProperties = (FolklorePieceProperties) properties;
            FolklorePiece folklorePiece = (FolklorePiece) piece;
            folklorePiece.setEthnographicRegion(folklorePieceProperties.getEthnographicRegion());
        }
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

    private static void copyProperties(PieceProperties source, PieceProperties dest, boolean completeCopy) {
        dest.setId(source.getId());
        dest.setAlbum(source.getAlbum());
        dest.setPerformer(source.getPerformer());
        dest.setAccompanimentPerformer(source.getAccompanimentPerformer());
        dest.setAuthor(source.getAuthor());
        dest.setArrangementAuthor(source.getArrangementAuthor());
        dest.setConductor(source.getConductor());
        dest.setSoloist(source.getSoloist());
        dest.setNote(source.getNote());
        dest.setSource(source.getSource());
        if (completeCopy) {
            dest.setAlbumTrackOrder(source.getAlbumTrackOrder());
            dest.setTitle(source.getTitle());
            dest.setDuration(source.getDuration());
            dest.setFile(source.getFile());
            dest.setRecord(source.getRecord());
        }
    }

    private static int parseAlbumTrackOrder(String value) {
        return value != null ? Integer.parseInt(value) : 0;
    }

}
