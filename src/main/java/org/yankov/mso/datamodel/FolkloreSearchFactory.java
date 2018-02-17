package org.yankov.mso.datamodel;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.yankov.mso.application.ApplicationContext;

import java.util.List;
import java.util.ResourceBundle;

public class FolkloreSearchFactory {

    private static final String CLASS_NAME = FolkloreSearchFactory.class.getName();

    public static final String VAR_TITLE = CLASS_NAME + "-title";
    public static final String VAR_PERFORMER = CLASS_NAME + "-performer";
    public static final String VAR_ACCOMPANIMENT_PERFORMER = CLASS_NAME + "-accompaniment-performer";
    public static final String VAR_ARRANGEMENT_AUTHOR = CLASS_NAME + "-arrangement-author";
    public static final String VAR_CONDUCTOR = CLASS_NAME + "-conductor";
    public static final String VAR_INSTRUMENT_PERFORMANCE = CLASS_NAME + "-instrument-performer";
    public static final String VAR_INSTRUMENT_ACCOMPANIMENT = CLASS_NAME + "-instrument-accompaniment";
    public static final String VAR_SOLOIST = CLASS_NAME + "-soloist";
    public static final String VAR_AUTHOR = CLASS_NAME + "-author";
    public static final String VAR_ETHNOGRAPHIC_REGION = CLASS_NAME + "-ethnographic-region";
    public static final String VAR_ALBUM_TITLE = CLASS_NAME + "-album-title";
    public static final String VAR_ALBUM_PRODUCTION_SIGNATURE = CLASS_NAME + "-album-production-signature";
    public static final String VAR_ALBUM_COLLECTION_SIGNATURE = CLASS_NAME + "-album-collection-signature";
    public static final String VAR_SOURCE_TYPE = CLASS_NAME + "-source-type";
    public static final String VAR_SOURCE_SIGNATURE = CLASS_NAME + "-source-signature";

    public static final String OPERATOR_EQUALS = CLASS_NAME + "-equals";
    public static final String OPERATOR_NOT_EQUALS = CLASS_NAME + "-not-equals";
    public static final String OPERATOR_CONTAINS = CLASS_NAME + "-contains";
    public static final String OPERATOR_NOT_CONTAINS = CLASS_NAME + "-not-contains";
    public static final String OPERATOR_STARTS = CLASS_NAME + "-starts";
    public static final String OPERATOR_ENDS = CLASS_NAME + "-ends";

    private static final ResourceBundle resourceBundle = ApplicationContext.getInstance().getFolkloreResourceBundle();

    public static ObservableList<Variable> createVariables() {
        List<FolklorePiece> pieces = ApplicationContext.getInstance().getFolkloreEntityCollections().getPieces();

        ObservableList<Variable> variables = FXCollections.observableArrayList();

        variables.add(new Variable<>(resourceBundle.getString(VAR_TITLE), pieces, FolklorePiece::getTitle));
        variables.add(new Variable<>(resourceBundle.getString(VAR_PERFORMER), pieces,
                                     folklorePiece -> folklorePiece.getPerformer().getName()));
        variables.add(new Variable<>(resourceBundle.getString(VAR_ACCOMPANIMENT_PERFORMER), pieces,
                                     folklorePiece -> folklorePiece.getAccompanimentPerformer().getName()));
        variables.add(new Variable<>(resourceBundle.getString(VAR_ARRANGEMENT_AUTHOR), pieces,
                                     folklorePiece -> folklorePiece.getArrangementAuthor().getName()));
        variables.add(new Variable<>(resourceBundle.getString(VAR_CONDUCTOR), pieces,
                                     folklorePiece -> folklorePiece.getConductor().getName()));
        variables.add(new Variable<>(resourceBundle.getString(VAR_INSTRUMENT_PERFORMANCE), pieces,
                                     folklorePiece -> folklorePiece.getPerformer().getInstrument().getName()));
        variables.add(new Variable<>(resourceBundle.getString(VAR_INSTRUMENT_ACCOMPANIMENT), pieces,
                                     folklorePiece -> folklorePiece.getAccompanimentPerformer().getInstrument()
                                                                   .getName()));
        variables.add(new Variable<>(resourceBundle.getString(VAR_SOLOIST), pieces,
                                     folklorePiece -> folklorePiece.getSoloist().getName()));
        variables.add(new Variable<>(resourceBundle.getString(VAR_AUTHOR), pieces,
                                     folklorePiece -> folklorePiece.getAuthor().getName()));
        variables.add(new Variable<>(resourceBundle.getString(VAR_ETHNOGRAPHIC_REGION), pieces,
                                     folklorePiece -> folklorePiece.getEthnographicRegion().getName()));
        variables.add(new Variable<>(resourceBundle.getString(VAR_ALBUM_TITLE), pieces,
                                     folklorePiece -> folklorePiece.getAlbum().getTitle()));
        variables.add(new Variable<>(resourceBundle.getString(VAR_ALBUM_PRODUCTION_SIGNATURE), pieces,
                                     folklorePiece -> folklorePiece.getAlbum().getProductionSignature()));
        variables.add(new Variable<>(resourceBundle.getString(VAR_ALBUM_COLLECTION_SIGNATURE), pieces,
                                     folklorePiece -> folklorePiece.getAlbum().getCollectionSignature()));
        variables.add(new Variable<>(resourceBundle.getString(VAR_SOURCE_TYPE), pieces,
                                     folklorePiece -> folklorePiece.getSource().getType().getName()));
        variables.add(new Variable<>(resourceBundle.getString(VAR_SOURCE_SIGNATURE), pieces,
                                     folklorePiece -> folklorePiece.getSource().getSignature()));

        return variables;
    }

    public static ObservableList<Operator> createOperators() {
        ObservableList<Operator> operators = FXCollections.observableArrayList();

        operators.add(new Operator(resourceBundle.getString(OPERATOR_EQUALS),
                                   (s1, s2) -> s1.trim().equalsIgnoreCase(s2.trim())));
        operators.add(new Operator(resourceBundle.getString(OPERATOR_NOT_EQUALS),
                                   (s1, s2) -> !s1.trim().equalsIgnoreCase(s2.trim())));
        operators.add(new Operator(resourceBundle.getString(OPERATOR_CONTAINS),
                                   (s1, s2) -> s1.trim().toLowerCase().contains(s2.trim().toLowerCase())));
        operators.add(new Operator(resourceBundle.getString(OPERATOR_NOT_CONTAINS),
                                   (s1, s2) -> !s1.trim().toLowerCase().contains(s2.trim().toLowerCase())));
        operators.add(new Operator(resourceBundle.getString(OPERATOR_STARTS),
                                   (s1, s2) -> s1.trim().toLowerCase().startsWith(s2.trim().toLowerCase())));
        operators.add(new Operator(resourceBundle.getString(OPERATOR_ENDS),
                                   (s1, s2) -> s1.trim().toLowerCase().endsWith(s2.trim().toLowerCase())));

        return operators;
    }

}
