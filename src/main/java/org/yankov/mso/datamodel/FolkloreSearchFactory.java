package org.yankov.mso.datamodel;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.yankov.mso.application.ApplicationContext;

import java.util.ResourceBundle;

public class FolkloreSearchFactory {

    private static final String CLASS_NAME = FolkloreSearchFactory.class.getName();

    public static final String VAR_LABEL_TITLE = CLASS_NAME + "-title";
    public static final String VAR_LABEL_PERFORMER = CLASS_NAME + "-performer";
    public static final String VAR_LABEL_ACCOMPANIMENT_PERFORMER = CLASS_NAME + "-accompaniment-performer";
    public static final String VAR_LABEL_ARRANGEMENT_AUTHOR = CLASS_NAME + "-arrangement-author";
    public static final String VAR_LABEL_CONDUCTOR = CLASS_NAME + "-conductor";
    public static final String VAR_LABEL_INSTRUMENT_PERFORMANCE = CLASS_NAME + "-instrument-performer";
    public static final String VAR_LABEL_INSTRUMENT_ACCOMPANIMENT = CLASS_NAME + "-instrument-accompaniment";
    public static final String VAR_LABEL_SOLOIST = CLASS_NAME + "-soloist";
    public static final String VAR_LABEL_AUTHOR = CLASS_NAME + "-author";
    public static final String VAR_LABEL_ETHNOGRAPHIC_REGION = CLASS_NAME + "-ethnographic-region";
    public static final String VAR_LABEL_PIECE_NOTE = CLASS_NAME + "-piece-note";
    public static final String VAR_LABEL_SOURCE_TYPE = CLASS_NAME + "-source-type";
    public static final String VAR_LABEL_SOURCE_SIGNATURE = CLASS_NAME + "-source-signature";

    public static final String OPERATOR_LABEL_EQUALS = CLASS_NAME + "-equals";
    public static final String OPERATOR_LABEL_NOT_EQUALS = CLASS_NAME + "-not-equals";
    public static final String OPERATOR_LABEL_CONTAINS = CLASS_NAME + "-contains";
    public static final String OPERATOR_LABEL_NOT_CONTAINS = CLASS_NAME + "-not-contains";
    public static final String OPERATOR_LABEL_STARTS = CLASS_NAME + "-starts";
    public static final String OPERATOR_LABEL_ENDS = CLASS_NAME + "-ends";

    private static final ResourceBundle resourceBundle = ApplicationContext.getInstance().getFolkloreResourceBundle();

    public static final Variable<FolklorePiece> VAR_TITLE = new Variable<>(resourceBundle.getString(VAR_LABEL_TITLE),
        FolklorePiece::getTitle);

    public static final Variable<FolklorePiece> VAR_PERFORMER = new Variable<>(
        resourceBundle.getString(VAR_LABEL_PERFORMER), folklorePiece -> folklorePiece.getPerformer().getName());

    public static final Variable<FolklorePiece> VAR_ACCOMPANIMENT_PERFORMER = new Variable<>(
        resourceBundle.getString(VAR_LABEL_ACCOMPANIMENT_PERFORMER),
        folklorePiece -> folklorePiece.getAccompanimentPerformer().getName());

    public static final Variable<FolklorePiece> VAR_ARRANGEMENT_AUTHOR = new Variable<>(
        resourceBundle.getString(VAR_LABEL_ARRANGEMENT_AUTHOR),
        folklorePiece -> folklorePiece.getArrangementAuthor().getName());

    public static final Variable<FolklorePiece> VAR_CONDUCTOR = new Variable<>(
        resourceBundle.getString(VAR_LABEL_CONDUCTOR), folklorePiece -> folklorePiece.getConductor().getName());

    public static final Variable<FolklorePiece> VAR_INSTRUMENT_PERFORMANCE = new Variable<>(
        resourceBundle.getString(VAR_LABEL_INSTRUMENT_PERFORMANCE),
        folklorePiece -> folklorePiece.getPerformer().getInstrument().getName());

    public static final Variable<FolklorePiece> VAR_INSTRUMENT_ACCOMPANIMENT = new Variable<>(
        resourceBundle.getString(VAR_LABEL_INSTRUMENT_ACCOMPANIMENT),
        folklorePiece -> folklorePiece.getAccompanimentPerformer().getInstrument().getName());

    public static final Variable<FolklorePiece> VAR_SOLOIST = new Variable<>(
        resourceBundle.getString(VAR_LABEL_SOLOIST), folklorePiece -> folklorePiece.getSoloist().getName());

    public static final Variable<FolklorePiece> VAR_AUTHOR = new Variable<>(resourceBundle.getString(VAR_LABEL_AUTHOR),
        folklorePiece -> folklorePiece.getAuthor().getName());

    public static final Variable<FolklorePiece> VAR_ETHNOGRAPHIC_REGION = new Variable<>(
        resourceBundle.getString(VAR_LABEL_ETHNOGRAPHIC_REGION),
        folklorePiece -> folklorePiece.getEthnographicRegion().getName());

    public static final Variable<FolklorePiece> VAR_PIECE_NOTE = new Variable<>(
        resourceBundle.getString(VAR_LABEL_PIECE_NOTE), Piece::getNote);

    public static final Variable<FolklorePiece> VAR_SOURCE_TYPE = new Variable<>(
        resourceBundle.getString(VAR_LABEL_SOURCE_TYPE),
        folklorePiece -> folklorePiece.getSource().getType().getName());

    public static final Variable<FolklorePiece> VAR_SOURCE_SIGNATURE = new Variable<>(
        resourceBundle.getString(VAR_LABEL_SOURCE_SIGNATURE),
        folklorePiece -> folklorePiece.getSource().getSignature());

    public static final Operator OPERATOR_EQUALS = new Operator(resourceBundle.getString(OPERATOR_LABEL_EQUALS),
        (s1, s2) -> s1.trim().equalsIgnoreCase(s2.trim()), false, false);

    public static final Operator OPERATOR_NOT_EQUALS = new Operator(resourceBundle.getString(OPERATOR_LABEL_NOT_EQUALS),
        (s1, s2) -> !s1.trim().equalsIgnoreCase(s2.trim()), true, false);

    public static final Operator OPERATOR_CONTAINS = new Operator(resourceBundle.getString(OPERATOR_LABEL_CONTAINS),
        (s1, s2) -> s1.trim().toLowerCase().contains(s2.trim().toLowerCase()), false, true);

    public static final Operator OPERATOR_NOT_CONTAINS = new Operator(
        resourceBundle.getString(OPERATOR_LABEL_NOT_CONTAINS),
        (s1, s2) -> !s1.trim().toLowerCase().contains(s2.trim().toLowerCase()), true, false);

    public static final Operator OPERATOR_STARTS = new Operator(resourceBundle.getString(OPERATOR_LABEL_STARTS),
        (s1, s2) -> s1.trim().toLowerCase().startsWith(s2.trim().toLowerCase()), false, false);

    public static final Operator OPERATOR_ENDS = new Operator(resourceBundle.getString(OPERATOR_LABEL_ENDS),
        (s1, s2) -> s1.trim().toLowerCase().endsWith(s2.trim().toLowerCase()), false, false);

    public static ObservableList<Variable<FolklorePiece>> createFolkloreVariables() {
        ObservableList<Variable<FolklorePiece>> variables = FXCollections.observableArrayList();

        variables.add(VAR_TITLE);
        variables.add(VAR_PERFORMER);
        variables.add(VAR_ACCOMPANIMENT_PERFORMER);
        variables.add(VAR_ARRANGEMENT_AUTHOR);
        variables.add(VAR_CONDUCTOR);
        variables.add(VAR_INSTRUMENT_PERFORMANCE);
        variables.add(VAR_INSTRUMENT_ACCOMPANIMENT);
        variables.add(VAR_SOLOIST);
        variables.add(VAR_AUTHOR);
        variables.add(VAR_ETHNOGRAPHIC_REGION);
        variables.add(VAR_PIECE_NOTE);
        variables.add(VAR_SOURCE_TYPE);
        variables.add(VAR_SOURCE_SIGNATURE);

        return variables;
    }

    public static ObservableList<Operator> createOperators() {
        ObservableList<Operator> operators = FXCollections.observableArrayList();

        operators.add(OPERATOR_EQUALS);
        operators.add(OPERATOR_NOT_EQUALS);
        operators.add(OPERATOR_CONTAINS);
        operators.add(OPERATOR_NOT_CONTAINS);
        operators.add(OPERATOR_STARTS);
        operators.add(OPERATOR_ENDS);

        return operators;
    }

}
