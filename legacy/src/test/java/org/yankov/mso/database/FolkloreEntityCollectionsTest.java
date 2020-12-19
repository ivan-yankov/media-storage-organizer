package org.yankov.mso.database;

import org.junit.Assert;
import org.junit.Test;
import org.yankov.mso.datamodel.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.Duration;
import java.util.ArrayDeque;
import java.util.Queue;
import java.util.Set;

public class FolkloreEntityCollectionsTest extends DatabaseTest {

    private static final String ACCORDEON = "Акордеон";
    private static final String KOSTA_KOLEV = "Коста Колев";
    private static final String KOSTA_KOLEV_NOTE = "Знакова фигура";
    private static final String FILIP_KUTEV = "Филип Кутев";
    private static final String ENS_FILIP_KUTEV = "Ансамбъл 'Филип Кутев'";
    private static final String VERKA_SIDEROVA = "Верка Сидерова";
    private static final String ALBUM_COLLECTION_SIGNATURE = "F1";
    private static final String ALBUM_TITLE = "Българска народна музика";
    private static final String ALBUM_NOTE = "Тестов диск";
    private static final String ETHNOGRAPHIC_REGION = "Добруджанска";
    private static final String NO_AUTHOR = "Народна";
    private static final String PIECE_NOTE = "Забележка";
    private static final String TRIO = "Трио";
    private static final String SOURCE_TYPE = "Грамофонна плоча";
    private static final String PIECE_TITLE = "Лале ли си, зюмбюл ли си, гюл ли си";
    private static final String SOURCE_SIGNATURE = "ВНА-002";
    private static final String RECORD_DATA_FORMAT = "FLAC";
    private static final Integer TRACK_ORDER = 5;
    private static final Duration PIECE_DURATION = Duration.ofMinutes(3);

    private static final String RECORD_FILE = "/record.flac";

    public FolkloreEntityCollectionsTest() {
    }

    @Test
    public void testInitialize() {
        FolkloreEntityCollections collections = new FolkloreEntityCollections();
        collections.clear();
        collections.initialize();
        assertInitializedEntities(collections);
    }

    @Test
    public void testSaveLoadDatabase() {
        FolkloreEntityCollections collections = new FolkloreEntityCollections();
        collections.clear();
        collections.initialize();

        modifyEntityCollections(collections, true, 1);

        ExpectedCollections expectedCollections = new ExpectedCollections();
        expectedCollections.createExpectedCollections(collections);

        DatabaseOperations.saveToDatabase(collections);
        collections = (FolkloreEntityCollections) DatabaseOperations.loadFromDatabase(FolkloreEntityCollections::new);

        assertStoredEntities(expectedCollections, collections);
    }

    @Test
    public void testAddGetSourceType() {
        FolkloreEntityCollections collections = new FolkloreEntityCollections();
        collections.clear();
        collections.initialize();

        Assert.assertTrue(collections.addSource(new Source(new SourceType("Източник"), "")));
        Assert.assertFalse(collections.addSource(new Source(new SourceType(" източник "), "")));
        Assert.assertFalse(collections.addSource(new Source(new SourceType("иЗТОЧНИК"), "")));
        Assert.assertTrue(collections.addSource(new Source(new SourceType("Друг източник"), "Балкантон")));

        Assert.assertTrue(collections.getSource("източник ").isPresent());
        Assert.assertFalse(collections.getSource("източник_").isPresent());
        Assert.assertTrue(collections.getSource("Друг източник/Балкантон").isPresent());
    }

    @Test
    public void testAddGetInstrument() {
        FolkloreEntityCollections collections = new FolkloreEntityCollections();
        collections.clear();
        collections.initialize();

        Assert.assertTrue(collections.addInstrument(new Instrument("Флигорна")));
        Assert.assertFalse(collections.addInstrument(new Instrument(" флигорна ")));
        Assert.assertFalse(collections.addInstrument(new Instrument(" фЛИГОРНА")));
        Assert.assertTrue(collections.addInstrument(new Instrument("Тромпет")));

        Assert.assertTrue(collections.getInstrument("флигорна ").isPresent());
        Assert.assertFalse(collections.getInstrument("фагот").isPresent());
    }

    @Test
    public void testAddGetArtist() {
        FolkloreEntityCollections collections = new FolkloreEntityCollections();
        collections.clear();
        collections.initialize();

        Assert.assertTrue(collections.addArtist(new Artist("Борис Машалов")));
        Assert.assertFalse(collections.addArtist(new Artist(" борис машалов ")));
        Assert.assertFalse(collections.addArtist(new Artist(" бОРИС МАШАЛОВ")));
        Assert.assertTrue(collections.addArtist(new Artist("Мита Стойчева")));

        Assert.assertTrue(collections.getArtist("борис машалов ").isPresent());
        Assert.assertFalse(collections.getArtist("Борис Карлов").isPresent());
    }

    @Test
    public void testAddGetEthnographicRegion() {
        FolkloreEntityCollections collections = new FolkloreEntityCollections();
        collections.clear();
        collections.initialize();

        Assert.assertTrue(collections.addEthnographicRegion(new EthnographicRegion("Регион")));
        Assert.assertFalse(collections.addEthnographicRegion(new EthnographicRegion(" регион ")));
        Assert.assertFalse(collections.addEthnographicRegion(new EthnographicRegion(" рЕГИОН ")));

        Assert.assertTrue(collections.getEthnographicRegion("регион ").isPresent());
        Assert.assertFalse(collections.getEthnographicRegion("друг регион").isPresent());
    }

    private void assertStoredEntities(ExpectedCollections expectedCollections, FolkloreEntityCollections collections) {
        assertSetsEqual(expectedCollections.getSources(), collections.getSources());
        assertSetsEqual(expectedCollections.getInstruments(), collections.getInstruments());
        assertSetsEqual(expectedCollections.getArtists(), collections.getArtists());
        assertSetsEqual(expectedCollections.getEthnographicRegions(), collections.getEthnographicRegions());

        Assert.assertTrue(collections.getArtist(KOSTA_KOLEV).isPresent());
        Assert.assertEquals(ACCORDEON, collections.getArtist(KOSTA_KOLEV).get().getInstrument().getName());
        Assert.assertEquals(KOSTA_KOLEV_NOTE, collections.getArtist(KOSTA_KOLEV).get().getNote());
        Assert.assertTrue(collections.getArtist(FILIP_KUTEV).get().getMissions().isEmpty());
        assertSetsEqual(expectedCollections.findArtist(KOSTA_KOLEV).get().getMissions(),
            collections.getArtist(KOSTA_KOLEV).get().getMissions());

        Assert.assertEquals(expectedCollections.getPieces().size(), collections.getPieces().size());
        assertPiece(expectedCollections.getPieces().get(0), collections.getPiece(0).get());
    }

    private void assertPiece(FolklorePiece expected, FolklorePiece actual) {
        Assert.assertEquals(expected.getEthnographicRegion(), actual.getEthnographicRegion());
        Assert.assertEquals(expected.getAccompanimentPerformer(), actual.getAccompanimentPerformer());
        Assert.assertEquals(expected.getArrangementAuthor(), actual.getArrangementAuthor());
        Assert.assertEquals(expected.getAuthor(), actual.getAuthor());
        Assert.assertEquals(expected.getConductor(), actual.getConductor());
        Assert.assertEquals(expected.getDuration(), actual.getDuration());
        Assert.assertEquals(expected.getNote(), actual.getNote());
        Assert.assertEquals(expected.getPerformer(), actual.getPerformer());
        Assert.assertEquals(expected.getSoloist(), actual.getSoloist());
        Assert.assertEquals(expected.getTitle(), actual.getTitle());

        Assert.assertEquals(SOURCE_TYPE, actual.getSource().getType().getName());
        Assert.assertEquals(SOURCE_SIGNATURE, actual.getSource().getSignature());

        byte[] expectedRecordBytes = readRecordBytes();
        Assert.assertTrue(expectedRecordBytes.length > 0);
        Assert.assertArrayEquals(expectedRecordBytes, actual.getRecord());
        Assert.assertEquals(RECORD_DATA_FORMAT, actual.getRecordFormat());
    }

    private void assertInitializedEntities(FolkloreEntityCollections collections) {
        Assert.assertTrue(collections.getArtists().isEmpty());
        Assert.assertTrue(collections.getPieces().isEmpty());

        assertSetsEqual(FolkloreEntityCollectionFactory.createInstruments(), collections.getInstruments());
        assertSetsEqual(FolkloreEntityCollectionFactory.createSources(collections.getSourceTypes()),
            collections.getSources());
        assertSetsEqual(FolkloreEntityCollectionFactory.createEthnographicRegions(),
            collections.getEthnographicRegions());
    }

    private <T> void assertSetsEqual(Set<T> expected, Set<T> actual) {
        Assert.assertTrue(actual.size() == expected.size());
        Queue<T> actualQueue = new ArrayDeque<>(actual);
        while (actualQueue.peek() != null) {
            Assert.assertTrue(expected.contains(actualQueue.poll()));
        }
    }

    public static void modifyEntityCollections(FolkloreEntityCollections entityCollections, boolean addMediaRecord,
                                               int numberPieceTableRecords) {
        entityCollections.addInstrument(new Instrument(ACCORDEON));

        entityCollections.addArtist(new Artist(KOSTA_KOLEV));
        entityCollections.getArtist(KOSTA_KOLEV).ifPresent(artist -> {
            artist.setInstrument(entityCollections.getInstrument(ACCORDEON).get());
            artist.setNote(KOSTA_KOLEV_NOTE);
            artist.addMission(ArtistMission.INSTRUMENT_PLAYER);
            artist.addMission(ArtistMission.COMPOSER);
            artist.addMission(ArtistMission.CONDUCTOR);
        });
        entityCollections.addArtist(new Artist(FILIP_KUTEV));
        entityCollections.addArtist(new Artist(ENS_FILIP_KUTEV));
        entityCollections.addArtist(new Artist(VERKA_SIDEROVA));
        entityCollections.addArtist(new Artist(NO_AUTHOR));
        entityCollections.addArtist(new Artist(TRIO));

        entityCollections.addSource(new Source(new SourceType(SOURCE_TYPE), SOURCE_SIGNATURE));

        for (int i = 0; i < numberPieceTableRecords; i++) {
            entityCollections.addPiece(createPiece(entityCollections, addMediaRecord));
        }
    }

    private static FolklorePiece createPiece(FolkloreEntityCollections entityCollections, boolean addMediaRecord) {
        FolklorePiece piece = new FolklorePiece();
        piece.setEthnographicRegion(entityCollections.getEthnographicRegion(ETHNOGRAPHIC_REGION).get());
        piece.setAccompanimentPerformer(entityCollections.getArtist(ENS_FILIP_KUTEV).get());
        piece.setArrangementAuthor(entityCollections.getArtist(FILIP_KUTEV).get());
        piece.setAuthor(entityCollections.getArtist(NO_AUTHOR).get());
        piece.setConductor(entityCollections.getArtist(KOSTA_KOLEV).get());
        piece.setDuration(PIECE_DURATION);
        piece.setNote(PIECE_NOTE);
        piece.setPerformer(entityCollections.getArtist(TRIO).get());
        piece.setSoloist(entityCollections.getArtist(VERKA_SIDEROVA).get());
        piece.setTitle(PIECE_TITLE);
        piece.setSource(entityCollections.getSource(SOURCE_TYPE + "/" + SOURCE_SIGNATURE).get());
        if (addMediaRecord) {
            piece.setRecord(readRecordBytes());
            piece.setRecordFormat(RECORD_DATA_FORMAT);
        }
        return piece;
    }

    private static byte[] readRecordBytes() {
        String path = FolkloreEntityCollectionsTest.class.getResource(RECORD_FILE).getPath();
        try {
            return Files.readAllBytes(Paths.get(path));
        } catch (IOException e) {
            e.printStackTrace();
            return new byte[0];
        }
    }

}
