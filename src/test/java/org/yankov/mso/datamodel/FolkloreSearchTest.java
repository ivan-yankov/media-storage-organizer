package org.yankov.mso.datamodel;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.yankov.mso.application.ApplicationArguments;
import org.yankov.mso.application.ApplicationContext;
import org.yankov.mso.database.DatabaseTest;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FolkloreSearchTest {

    private List<FolklorePiece> pieces;

    public FolkloreSearchTest() {
        this.pieces = createPieces();
    }

    @BeforeClass
    public static void beforeClass() {
        ApplicationContext.getInstance().initialize(DatabaseTest.TEST_ARGUMENTS);

        ApplicationContext.getInstance().getDatabaseManager()
                          .setOperationFailed(throwable -> Assert.fail(throwable.getMessage()));
    }

    @Test
    public void testOperatorEquals() {
        String value;
        List<FolklorePiece> result;

        value = "недо ле, недке, хубава";
        result = FolkloreSearchFactory.OPERATOR_EQUALS.match(pieces, FolkloreSearchFactory.VAR_TITLE, value);
        assertResult(result, 0);

        value = "вълкана стоянова";
        result = FolkloreSearchFactory.OPERATOR_EQUALS.match(pieces, FolkloreSearchFactory.VAR_PERFORMER, value);
        assertResult(result, 2);

        value = "онмбнр";
        result = FolkloreSearchFactory.OPERATOR_EQUALS
                .match(pieces, FolkloreSearchFactory.VAR_ACCOMPANIMENT_PERFORMER, value);
        assertResult(result, 0);

        value = "красимир кюркчийски";
        result = FolkloreSearchFactory.OPERATOR_EQUALS
                .match(pieces, FolkloreSearchFactory.VAR_ARRANGEMENT_AUTHOR, value);
        assertResult(result, 1);

        value = "коста колев";
        result = FolkloreSearchFactory.OPERATOR_EQUALS.match(pieces, FolkloreSearchFactory.VAR_CONDUCTOR, value);
        assertResult(result, 0);

        value = "гъдулка";
        result = FolkloreSearchFactory.OPERATOR_EQUALS
                .match(pieces, FolkloreSearchFactory.VAR_INSTRUMENT_PERFORMANCE, value);
        assertResult(result, 3);

        value = "кавал";
        result = FolkloreSearchFactory.OPERATOR_EQUALS
                .match(pieces, FolkloreSearchFactory.VAR_INSTRUMENT_ACCOMPANIMENT, value);
        assertResult(result, 2);

        value = "надежда хвойнева";
        result = FolkloreSearchFactory.OPERATOR_EQUALS.match(pieces, FolkloreSearchFactory.VAR_SOLOIST, value);
        assertResult(result, 1);

        value = "филип кутев";
        result = FolkloreSearchFactory.OPERATOR_EQUALS.match(pieces, FolkloreSearchFactory.VAR_AUTHOR, value);
        assertResult(result, 4);

        value = "родопи";
        result = FolkloreSearchFactory.OPERATOR_EQUALS
                .match(pieces, FolkloreSearchFactory.VAR_ETHNOGRAPHIC_REGION, value);
        assertResult(result, 1);

        value = "хорови народни песни";
        result = FolkloreSearchFactory.OPERATOR_EQUALS.match(pieces, FolkloreSearchFactory.VAR_ALBUM_TITLE, value);
        assertResult(result, 1);

        value = "вна001";
        result = FolkloreSearchFactory.OPERATOR_EQUALS
                .match(pieces, FolkloreSearchFactory.VAR_ALBUM_PRODUCTION_SIGNATURE, value);
        assertResult(result, 1);

        value = "f001";
        result = FolkloreSearchFactory.OPERATOR_EQUALS
                .match(pieces, FolkloreSearchFactory.VAR_ALBUM_COLLECTION_SIGNATURE, value);
        assertResult(result, 1);

        value = "лента";
        result = FolkloreSearchFactory.OPERATOR_EQUALS.match(pieces, FolkloreSearchFactory.VAR_SOURCE_TYPE, value);
        assertResult(result, 3);

        value = "вна002";
        result = FolkloreSearchFactory.OPERATOR_EQUALS.match(pieces, FolkloreSearchFactory.VAR_SOURCE_SIGNATURE, value);
        assertResult(result, 3);
    }

    @Test
    public void testOperatorNotEquals() {
        String value;
        List<FolklorePiece> result;

        value = "недо ле, недке, хубава";
        result = FolkloreSearchFactory.OPERATOR_NOT_EQUALS.match(pieces, FolkloreSearchFactory.VAR_TITLE, value);
        assertResult(result, excludePieceIndeces(0));

        value = "вълкана стоянова";
        result = FolkloreSearchFactory.OPERATOR_NOT_EQUALS.match(pieces, FolkloreSearchFactory.VAR_PERFORMER, value);
        assertResult(result, excludePieceIndeces(2));

        value = "онмбнр";
        result = FolkloreSearchFactory.OPERATOR_NOT_EQUALS
                .match(pieces, FolkloreSearchFactory.VAR_ACCOMPANIMENT_PERFORMER, value);
        assertResult(result, excludePieceIndeces(0));

        value = "красимир кюркчийски";
        result = FolkloreSearchFactory.OPERATOR_NOT_EQUALS
                .match(pieces, FolkloreSearchFactory.VAR_ARRANGEMENT_AUTHOR, value);
        assertResult(result, excludePieceIndeces(1));

        value = "коста колев";
        result = FolkloreSearchFactory.OPERATOR_NOT_EQUALS.match(pieces, FolkloreSearchFactory.VAR_CONDUCTOR, value);
        assertResult(result, excludePieceIndeces(0));

        value = "гъдулка";
        result = FolkloreSearchFactory.OPERATOR_NOT_EQUALS
                .match(pieces, FolkloreSearchFactory.VAR_INSTRUMENT_PERFORMANCE, value);
        assertResult(result, excludePieceIndeces(3));

        value = "кавал";
        result = FolkloreSearchFactory.OPERATOR_NOT_EQUALS
                .match(pieces, FolkloreSearchFactory.VAR_INSTRUMENT_ACCOMPANIMENT, value);
        assertResult(result, excludePieceIndeces(2));

        value = "надежда хвойнева";
        result = FolkloreSearchFactory.OPERATOR_NOT_EQUALS.match(pieces, FolkloreSearchFactory.VAR_SOLOIST, value);
        assertResult(result, excludePieceIndeces(1));

        value = "филип кутев";
        result = FolkloreSearchFactory.OPERATOR_NOT_EQUALS.match(pieces, FolkloreSearchFactory.VAR_AUTHOR, value);
        assertResult(result, excludePieceIndeces(4));

        value = "родопи";
        result = FolkloreSearchFactory.OPERATOR_NOT_EQUALS
                .match(pieces, FolkloreSearchFactory.VAR_ETHNOGRAPHIC_REGION, value);
        assertResult(result, excludePieceIndeces(1));

        value = "хорови народни песни";
        result = FolkloreSearchFactory.OPERATOR_NOT_EQUALS.match(pieces, FolkloreSearchFactory.VAR_ALBUM_TITLE, value);
        assertResult(result, excludePieceIndeces(1));

        value = "вна001";
        result = FolkloreSearchFactory.OPERATOR_NOT_EQUALS
                .match(pieces, FolkloreSearchFactory.VAR_ALBUM_PRODUCTION_SIGNATURE, value);
        assertResult(result, excludePieceIndeces(1));

        value = "f001";
        result = FolkloreSearchFactory.OPERATOR_NOT_EQUALS
                .match(pieces, FolkloreSearchFactory.VAR_ALBUM_COLLECTION_SIGNATURE, value);
        assertResult(result, excludePieceIndeces(1));

        value = "лента";
        result = FolkloreSearchFactory.OPERATOR_NOT_EQUALS.match(pieces, FolkloreSearchFactory.VAR_SOURCE_TYPE, value);
        assertResult(result, excludePieceIndeces(3));

        value = "вна002";
        result = FolkloreSearchFactory.OPERATOR_NOT_EQUALS
                .match(pieces, FolkloreSearchFactory.VAR_SOURCE_SIGNATURE, value);
        assertResult(result, excludePieceIndeces(3));
    }

    @Test
    public void testOperatorContains() {
        String value;
        List<FolklorePiece> result;

        value = "недке";
        result = FolkloreSearchFactory.OPERATOR_CONTAINS.match(pieces, FolkloreSearchFactory.VAR_TITLE, value);
        assertResult(result, 0);

        value = "вълкана";
        result = FolkloreSearchFactory.OPERATOR_CONTAINS.match(pieces, FolkloreSearchFactory.VAR_PERFORMER, value);
        assertResult(result, 2);

        value = "бнр";
        result = FolkloreSearchFactory.OPERATOR_CONTAINS
                .match(pieces, FolkloreSearchFactory.VAR_ACCOMPANIMENT_PERFORMER, value);
        assertResult(result, 0);

        value = "кюркчийски";
        result = FolkloreSearchFactory.OPERATOR_CONTAINS
                .match(pieces, FolkloreSearchFactory.VAR_ARRANGEMENT_AUTHOR, value);
        assertResult(result, 1);

        value = "колев";
        result = FolkloreSearchFactory.OPERATOR_CONTAINS.match(pieces, FolkloreSearchFactory.VAR_CONDUCTOR, value);
        assertResult(result, 0);

        value = "дулка";
        result = FolkloreSearchFactory.OPERATOR_CONTAINS
                .match(pieces, FolkloreSearchFactory.VAR_INSTRUMENT_PERFORMANCE, value);
        assertResult(result, 3);

        value = "вал";
        result = FolkloreSearchFactory.OPERATOR_CONTAINS
                .match(pieces, FolkloreSearchFactory.VAR_INSTRUMENT_ACCOMPANIMENT, value);
        assertResult(result, 2);

        value = "надежда";
        result = FolkloreSearchFactory.OPERATOR_CONTAINS.match(pieces, FolkloreSearchFactory.VAR_SOLOIST, value);
        assertResult(result, 1);

        value = "кутев";
        result = FolkloreSearchFactory.OPERATOR_CONTAINS.match(pieces, FolkloreSearchFactory.VAR_AUTHOR, value);
        assertResult(result, 4);

        value = "родоп";
        result = FolkloreSearchFactory.OPERATOR_CONTAINS
                .match(pieces, FolkloreSearchFactory.VAR_ETHNOGRAPHIC_REGION, value);
        assertResult(result, 1);

        value = "народни песни";
        result = FolkloreSearchFactory.OPERATOR_CONTAINS.match(pieces, FolkloreSearchFactory.VAR_ALBUM_TITLE, value);
        assertResult(result, 1);

        value = "01";
        result = FolkloreSearchFactory.OPERATOR_CONTAINS
                .match(pieces, FolkloreSearchFactory.VAR_ALBUM_PRODUCTION_SIGNATURE, value);
        assertResult(result, 1);

        value = "f";
        result = FolkloreSearchFactory.OPERATOR_CONTAINS
                .match(pieces, FolkloreSearchFactory.VAR_ALBUM_COLLECTION_SIGNATURE, value);
        assertResult(result, 1);

        value = "лен";
        result = FolkloreSearchFactory.OPERATOR_CONTAINS.match(pieces, FolkloreSearchFactory.VAR_SOURCE_TYPE, value);
        assertResult(result, 3);

        value = "вн";
        result = FolkloreSearchFactory.OPERATOR_CONTAINS
                .match(pieces, FolkloreSearchFactory.VAR_SOURCE_SIGNATURE, value);
        assertResult(result, 3);

        value = "стани недо ръченица";
        result = FolkloreSearchFactory.OPERATOR_CONTAINS
                .match(pieces, FolkloreSearchFactory.VAR_TITLE, value);
        assertResult(result, 1, 0, 3);

        value = "луди млади";
        result = FolkloreSearchFactory.OPERATOR_CONTAINS
                .match(pieces, FolkloreSearchFactory.VAR_TITLE, value);
        assertResult(result, 5, 6, 7);

        value = "луди-млади";
        result = FolkloreSearchFactory.OPERATOR_CONTAINS
                .match(pieces, FolkloreSearchFactory.VAR_TITLE, value);
        assertResult(result, 5, 6, 7);

        value = "луди - млади";
        result = FolkloreSearchFactory.OPERATOR_CONTAINS
                .match(pieces, FolkloreSearchFactory.VAR_TITLE, value);
        assertResult(result, 5, 6, 7);
    }

    @Test
    public void testOperatorNotContains() {
        String value;
        List<FolklorePiece> result;

        value = "недке";
        result = FolkloreSearchFactory.OPERATOR_NOT_CONTAINS.match(pieces, FolkloreSearchFactory.VAR_TITLE, value);
        assertResult(result, excludePieceIndeces(0));

        value = "вълкана";
        result = FolkloreSearchFactory.OPERATOR_NOT_CONTAINS.match(pieces, FolkloreSearchFactory.VAR_PERFORMER, value);
        assertResult(result, excludePieceIndeces(2));

        value = "бнр";
        result = FolkloreSearchFactory.OPERATOR_NOT_CONTAINS
                .match(pieces, FolkloreSearchFactory.VAR_ACCOMPANIMENT_PERFORMER, value);
        assertResult(result, excludePieceIndeces(0));

        value = "кюркчийски";
        result = FolkloreSearchFactory.OPERATOR_NOT_CONTAINS
                .match(pieces, FolkloreSearchFactory.VAR_ARRANGEMENT_AUTHOR, value);
        assertResult(result, excludePieceIndeces(1));

        value = "колев";
        result = FolkloreSearchFactory.OPERATOR_NOT_CONTAINS.match(pieces, FolkloreSearchFactory.VAR_CONDUCTOR, value);
        assertResult(result, excludePieceIndeces(0));

        value = "дулка";
        result = FolkloreSearchFactory.OPERATOR_NOT_CONTAINS
                .match(pieces, FolkloreSearchFactory.VAR_INSTRUMENT_PERFORMANCE, value);
        assertResult(result, excludePieceIndeces(3));

        value = "вал";
        result = FolkloreSearchFactory.OPERATOR_NOT_CONTAINS
                .match(pieces, FolkloreSearchFactory.VAR_INSTRUMENT_ACCOMPANIMENT, value);
        assertResult(result, excludePieceIndeces(2));

        value = "надежда";
        result = FolkloreSearchFactory.OPERATOR_NOT_CONTAINS.match(pieces, FolkloreSearchFactory.VAR_SOLOIST, value);
        assertResult(result, excludePieceIndeces(1));

        value = "кутев";
        result = FolkloreSearchFactory.OPERATOR_NOT_CONTAINS.match(pieces, FolkloreSearchFactory.VAR_AUTHOR, value);
        assertResult(result, excludePieceIndeces(4));

        value = "родоп";
        result = FolkloreSearchFactory.OPERATOR_NOT_CONTAINS
                .match(pieces, FolkloreSearchFactory.VAR_ETHNOGRAPHIC_REGION, value);
        assertResult(result, excludePieceIndeces(1));

        value = "народни песни";
        result = FolkloreSearchFactory.OPERATOR_NOT_CONTAINS
                .match(pieces, FolkloreSearchFactory.VAR_ALBUM_TITLE, value);
        assertResult(result, excludePieceIndeces(1));

        value = "01";
        result = FolkloreSearchFactory.OPERATOR_NOT_CONTAINS
                .match(pieces, FolkloreSearchFactory.VAR_ALBUM_PRODUCTION_SIGNATURE, value);
        assertResult(result, excludePieceIndeces(1));

        value = "f";
        result = FolkloreSearchFactory.OPERATOR_NOT_CONTAINS
                .match(pieces, FolkloreSearchFactory.VAR_ALBUM_COLLECTION_SIGNATURE, value);
        assertResult(result, excludePieceIndeces(1));

        value = "лен";
        result = FolkloreSearchFactory.OPERATOR_NOT_CONTAINS
                .match(pieces, FolkloreSearchFactory.VAR_SOURCE_TYPE, value);
        assertResult(result, excludePieceIndeces(3));

        value = "вн";
        result = FolkloreSearchFactory.OPERATOR_NOT_CONTAINS
                .match(pieces, FolkloreSearchFactory.VAR_SOURCE_SIGNATURE, value);
        assertResult(result, excludePieceIndeces(3));
    }

    @Test
    public void testOperatorStarts() {
        String value;
        List<FolklorePiece> result;

        value = "нед";
        result = FolkloreSearchFactory.OPERATOR_STARTS.match(pieces, FolkloreSearchFactory.VAR_TITLE, value);
        assertResult(result, 0);

        value = "въл";
        result = FolkloreSearchFactory.OPERATOR_STARTS.match(pieces, FolkloreSearchFactory.VAR_PERFORMER, value);
        assertResult(result, 2);

        value = "онм";
        result = FolkloreSearchFactory.OPERATOR_STARTS
                .match(pieces, FolkloreSearchFactory.VAR_ACCOMPANIMENT_PERFORMER, value);
        assertResult(result, 0);

        value = "краси";
        result = FolkloreSearchFactory.OPERATOR_STARTS
                .match(pieces, FolkloreSearchFactory.VAR_ARRANGEMENT_AUTHOR, value);
        assertResult(result, 1);

        value = "коста";
        result = FolkloreSearchFactory.OPERATOR_STARTS.match(pieces, FolkloreSearchFactory.VAR_CONDUCTOR, value);
        assertResult(result, 0);

        value = "гъ";
        result = FolkloreSearchFactory.OPERATOR_STARTS
                .match(pieces, FolkloreSearchFactory.VAR_INSTRUMENT_PERFORMANCE, value);
        assertResult(result, 3);

        value = "к";
        result = FolkloreSearchFactory.OPERATOR_STARTS
                .match(pieces, FolkloreSearchFactory.VAR_INSTRUMENT_ACCOMPANIMENT, value);
        assertResult(result, 2);

        value = "над";
        result = FolkloreSearchFactory.OPERATOR_STARTS.match(pieces, FolkloreSearchFactory.VAR_SOLOIST, value);
        assertResult(result, 1);

        value = "фил";
        result = FolkloreSearchFactory.OPERATOR_STARTS.match(pieces, FolkloreSearchFactory.VAR_AUTHOR, value);
        assertResult(result, 4);

        value = "род";
        result = FolkloreSearchFactory.OPERATOR_STARTS
                .match(pieces, FolkloreSearchFactory.VAR_ETHNOGRAPHIC_REGION, value);
        assertResult(result, 1);

        value = "хор";
        result = FolkloreSearchFactory.OPERATOR_STARTS.match(pieces, FolkloreSearchFactory.VAR_ALBUM_TITLE, value);
        assertResult(result, 1);

        value = "вна";
        result = FolkloreSearchFactory.OPERATOR_STARTS
                .match(pieces, FolkloreSearchFactory.VAR_ALBUM_PRODUCTION_SIGNATURE, value);
        assertResult(result, 1);

        value = "f";
        result = FolkloreSearchFactory.OPERATOR_STARTS
                .match(pieces, FolkloreSearchFactory.VAR_ALBUM_COLLECTION_SIGNATURE, value);
        assertResult(result, 1);

        value = "л";
        result = FolkloreSearchFactory.OPERATOR_STARTS.match(pieces, FolkloreSearchFactory.VAR_SOURCE_TYPE, value);
        assertResult(result, 3);

        value = "в";
        result = FolkloreSearchFactory.OPERATOR_STARTS.match(pieces, FolkloreSearchFactory.VAR_SOURCE_SIGNATURE, value);
        assertResult(result, 3);
    }

    @Test
    public void testOperatorEnds() {
        String value;
        List<FolklorePiece> result;

        value = "хубава";
        result = FolkloreSearchFactory.OPERATOR_ENDS.match(pieces, FolkloreSearchFactory.VAR_TITLE, value);
        assertResult(result, 0);

        value = "стоянова";
        result = FolkloreSearchFactory.OPERATOR_ENDS.match(pieces, FolkloreSearchFactory.VAR_PERFORMER, value);
        assertResult(result, 2);

        value = "нр";
        result = FolkloreSearchFactory.OPERATOR_ENDS
                .match(pieces, FolkloreSearchFactory.VAR_ACCOMPANIMENT_PERFORMER, value);
        assertResult(result, 0);

        value = "чийски";
        result = FolkloreSearchFactory.OPERATOR_ENDS.match(pieces, FolkloreSearchFactory.VAR_ARRANGEMENT_AUTHOR, value);
        assertResult(result, 1);

        value = "лев";
        result = FolkloreSearchFactory.OPERATOR_ENDS.match(pieces, FolkloreSearchFactory.VAR_CONDUCTOR, value);
        assertResult(result, 0);

        value = "лка";
        result = FolkloreSearchFactory.OPERATOR_ENDS
                .match(pieces, FolkloreSearchFactory.VAR_INSTRUMENT_PERFORMANCE, value);
        assertResult(result, 3);

        value = "вал";
        result = FolkloreSearchFactory.OPERATOR_ENDS
                .match(pieces, FolkloreSearchFactory.VAR_INSTRUMENT_ACCOMPANIMENT, value);
        assertResult(result, 2);

        value = "хвойнева";
        result = FolkloreSearchFactory.OPERATOR_ENDS.match(pieces, FolkloreSearchFactory.VAR_SOLOIST, value);
        assertResult(result, 1);

        value = "кутев";
        result = FolkloreSearchFactory.OPERATOR_ENDS.match(pieces, FolkloreSearchFactory.VAR_AUTHOR, value);
        assertResult(result, 4);

        value = "допи";
        result = FolkloreSearchFactory.OPERATOR_ENDS
                .match(pieces, FolkloreSearchFactory.VAR_ETHNOGRAPHIC_REGION, value);
        assertResult(result, 1);

        value = "песни";
        result = FolkloreSearchFactory.OPERATOR_ENDS.match(pieces, FolkloreSearchFactory.VAR_ALBUM_TITLE, value);
        assertResult(result, 1);

        value = "01";
        result = FolkloreSearchFactory.OPERATOR_ENDS
                .match(pieces, FolkloreSearchFactory.VAR_ALBUM_PRODUCTION_SIGNATURE, value);
        assertResult(result, 1);

        value = "1";
        result = FolkloreSearchFactory.OPERATOR_ENDS
                .match(pieces, FolkloreSearchFactory.VAR_ALBUM_COLLECTION_SIGNATURE, value);
        assertResult(result, 1);

        value = "та";
        result = FolkloreSearchFactory.OPERATOR_ENDS.match(pieces, FolkloreSearchFactory.VAR_SOURCE_TYPE, value);
        assertResult(result, 3);

        value = "02";
        result = FolkloreSearchFactory.OPERATOR_ENDS.match(pieces, FolkloreSearchFactory.VAR_SOURCE_SIGNATURE, value);
        assertResult(result, 3);
    }

    @Test
    public void testEscapeStopWords() {
        String value;
        List<FolklorePiece> result;

        value = "недо ле недке хубава";
        result = FolkloreSearchFactory.OPERATOR_EQUALS.match(pieces, FolkloreSearchFactory.VAR_TITLE, value);
        assertResult(result, 0);

        value = "димитър, кара гемия";
        result = FolkloreSearchFactory.OPERATOR_EQUALS.match(pieces, FolkloreSearchFactory.VAR_TITLE, value);
        assertResult(result, 2);
    }

    private void assertResult(List<FolklorePiece> actualResult, Integer... expectedIndices) {
        Assert.assertEquals("Search result length doesn't match", expectedIndices.length, actualResult.size());
        for (int i = 0; i < expectedIndices.length; i++) {
            Assert.assertEquals(pieces.get(expectedIndices[i]), actualResult.get(i));
        }
    }

    private List<FolklorePiece> createPieces() {
        List<FolklorePiece> pieces = new ArrayList<>();
        FolklorePiece piece;

        piece = new FolklorePiece();
        piece.setTitle("Недо ле, Недке, хубава");
        piece.setPerformer(new Artist("Борис Машалов"));
        piece.setArrangementAuthor(new Artist("Стефан Кънев"));
        piece.setConductor(new Artist("Коста Колев"));
        piece.setEthnographicRegion(new EthnographicRegion("Мизия"));
        piece.setAccompanimentPerformer(createAccompanimentPerformer());
        pieces.add(piece);

        piece = new FolklorePiece();
        piece.setTitle("Стани ми, майчо");
        piece.setPerformer(new Artist("Мистерията на българските гласове"));
        piece.setSoloist(new Artist("Надежда Хвойнева"));
        piece.setEthnographicRegion(new EthnographicRegion("Родопи"));
        piece.setAlbum(createAlbum());
        piece.setArrangementAuthor(new Artist("Красимир Кюркчийски"));
        pieces.add(piece);

        piece = new FolklorePiece();
        piece.setTitle("Димитър кара гемия");
        piece.setPerformer(new Artist("Вълкана Стоянова"));
        piece.setAccompanimentPerformer(createInstrumentAccompanimentPerformer());
        pieces.add(piece);

        piece = new FolklorePiece();
        piece.setTitle("Кюстендилска ръченица");
        piece.setPerformer(createInstrumentPerformer());
        piece.setSource(new Source(new SourceType("Лента"), "ВНА002"));
        pieces.add(piece);

        piece = new FolklorePiece();
        piece.setTitle("Прехвръкна птичка");
        piece.setAuthor(new Artist("Филип Кутев"));
        pieces.add(piece);

        piece = new FolklorePiece();
        piece.setTitle("Луди млади");
        pieces.add(piece);

        piece = new FolklorePiece();
        piece.setTitle("Луди-млади");
        pieces.add(piece);

        piece = new FolklorePiece();
        piece.setTitle("Луди - млади");
        pieces.add(piece);

        return pieces;
    }

    private Integer[] excludePieceIndeces(Integer... excludeIndeces) {
        List<Integer> result = new ArrayList<>();
        List<Integer> excluded = Arrays.asList(excludeIndeces);
        for (int i = 0; i < pieces.size(); i++) {
            if (!excluded.contains(i)) {
                result.add(i);
            }
        }
        return result.toArray(new Integer[0]);
    }

    private Artist createAccompanimentPerformer() {
        Artist artist = new Artist("ОНМБНР");
        artist.addMission(ArtistMission.ORCHESTRA);
        return artist;
    }

    private Album createAlbum() {
        Album album = new Album();
        album.setTitle("Хорови народни песни");
        album.setCollectionSignature("F001");
        album.setProductionSignature("ВНА001");
        return album;
    }

    private Artist createInstrumentAccompanimentPerformer() {
        Artist artist = new Artist("Никола Ганчев");
        artist.addMission(ArtistMission.INSTRUMENT_PLAYER);
        artist.setInstrument(new Instrument("Кавал"));
        return artist;
    }

    private Artist createInstrumentPerformer() {
        Artist artist = new Artist("Атанас Вълчев");
        artist.addMission(ArtistMission.INSTRUMENT_PLAYER);
        artist.setInstrument(new Instrument("Гъдулка"));
        return artist;
    }

}
