package org.yankov.mso.database.folklore;

import org.junit.Assert;
import org.junit.Test;
import org.yankov.mso.database.FolkloreEntityCollectionFactory;
import org.yankov.mso.database.FolkloreEntityCollections;
import org.yankov.mso.database.generic.DatabaseTest;
import org.yankov.mso.datamodel.*;

import java.io.FileInputStream;
import java.io.IOException;
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
    private static final String ALBUM_PRODUCTION_SIGNATURE = "ВНА-001";
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
    private static final Duration ALBUM_DURATION = Duration.ofMinutes(70);

    private static final String RECORD_FILE = "/record.flac";

    public FolkloreEntityCollectionsTest() {
    }

    @Test
    public void testInitializeEntityCollections() {
        FolkloreEntityCollections actualEntityCollections = new FolkloreEntityCollections();
        actualEntityCollections.initializeEntityCollections();
        assertInitializedEntities(actualEntityCollections);

        modifyEntityCollections(actualEntityCollections, true, 1);
        actualEntityCollections.saveEntityCollections();
        actualEntityCollections.initializeEntityCollections();

        FolkloreEntityCollections expectedEntityCollections = new FolkloreEntityCollections();
        expectedEntityCollections.addSourceTypes(FolkloreEntityCollectionFactory.createSourceTypes());
        expectedEntityCollections
                .addSources(FolkloreEntityCollectionFactory.createSources(expectedEntityCollections.getSourceTypes()));
        expectedEntityCollections.addInstruments(FolkloreEntityCollectionFactory.createInstruments());
        expectedEntityCollections.addEthnographicRegions(FolkloreEntityCollectionFactory.createEthnographicRegions());
        modifyEntityCollections(expectedEntityCollections, true, 1);

        assertStoredEntities(expectedEntityCollections, actualEntityCollections);
    }

    @Test
    public void testAddGetSourceType() {
        FolkloreEntityCollections collections = new FolkloreEntityCollections();

        Assert.assertTrue(collections.addSource(new Source(new SourceType("Грамофонна плоча"), "")));
        Assert.assertFalse(collections.addSource(new Source(new SourceType(" грамофонна плоча "), "")));
        Assert.assertFalse(collections.addSource(new Source(new SourceType("гРАМОФОННА ПЛОЧА"), "")));
        Assert.assertTrue(collections.addSource(new Source(new SourceType("Грамофоннаплоча"), "")));
        Assert.assertTrue(collections.addSource(new Source(new SourceType("Лента"), "Балкантон")));

        Assert.assertTrue(collections.getSource("грамофонна плоча ").isPresent());
        Assert.assertFalse(collections.getSource("грамофонна_плоча").isPresent());
        Assert.assertTrue(collections.getSource("Лента/Балкантон").isPresent());
    }

    @Test
    public void testAddGetInstrument() {
        FolkloreEntityCollections collections = new FolkloreEntityCollections();

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

        Assert.assertTrue(collections.addArtist(new Artist("Борис Машалов")));
        Assert.assertFalse(collections.addArtist(new Artist(" борис машалов ")));
        Assert.assertFalse(collections.addArtist(new Artist(" бОРИС МАШАЛОВ")));
        Assert.assertTrue(collections.addArtist(new Artist("Мита Стойчева")));

        Assert.assertTrue(collections.getArtist("борис машалов ").isPresent());
        Assert.assertFalse(collections.getArtist("Борис Карлов").isPresent());
    }

    @Test
    public void testAddGetDisc() {
        FolkloreEntityCollections collections = new FolkloreEntityCollections();

        Assert.assertTrue(collections.addAlbum(new Album("F1")));
        Assert.assertFalse(collections.addAlbum(new Album(" f1 ")));
        Assert.assertTrue(collections.addAlbum(new Album("F2")));

        Assert.assertTrue(collections.getAlbum("f1 ").isPresent());
        Assert.assertFalse(collections.getAlbum("F3").isPresent());
    }

    @Test
    public void testAddGetEthnographicRegion() {
        FolkloreEntityCollections collections = new FolkloreEntityCollections();

        Assert.assertTrue(collections.addEthnographicRegion(new EthnographicRegion("Тракийска")));
        Assert.assertFalse(collections.addEthnographicRegion(new EthnographicRegion(" тракийска ")));
        Assert.assertFalse(collections.addEthnographicRegion(new EthnographicRegion(" тРАКИЙСКА ")));
        Assert.assertTrue(collections.addEthnographicRegion(new EthnographicRegion("Родопска")));

        Assert.assertTrue(collections.getEthnographicRegion("тракийска ").isPresent());
        Assert.assertFalse(collections.getEthnographicRegion("Северняшка").isPresent());
    }

    private void assertStoredEntities(FolkloreEntityCollections expectedEntityCollections,
                                      FolkloreEntityCollections actualEntityCollections) {
        assertSetsEqual(expectedEntityCollections.getSources(), actualEntityCollections.getSources());
        assertSetsEqual(expectedEntityCollections.getInstruments(), actualEntityCollections.getInstruments());
        assertSetsEqual(expectedEntityCollections.getArtists(), actualEntityCollections.getArtists());
        assertSetsEqual(expectedEntityCollections.getAlbums(), actualEntityCollections.getAlbums());
        assertSetsEqual(expectedEntityCollections.getEthnographicRegions(),
                        actualEntityCollections.getEthnographicRegions());

        Assert.assertTrue(actualEntityCollections.getAlbum(ALBUM_COLLECTION_SIGNATURE).isPresent());
        Album actualAlbum = actualEntityCollections.getAlbum(ALBUM_COLLECTION_SIGNATURE).get();
        Assert.assertEquals(ALBUM_NOTE, actualAlbum.getNote());
        Assert.assertEquals(ALBUM_PRODUCTION_SIGNATURE, actualAlbum.getProductionSignature());
        Assert.assertEquals(ALBUM_TITLE, actualAlbum.getTitle());
        Assert.assertEquals(ALBUM_DURATION, actualAlbum.getDuration());

        Assert.assertTrue(actualEntityCollections.getArtist(KOSTA_KOLEV).isPresent());
        Assert.assertEquals(ACCORDEON, actualEntityCollections.getArtist(KOSTA_KOLEV).get().getInstrument().getName());
        Assert.assertEquals(KOSTA_KOLEV_NOTE, actualEntityCollections.getArtist(KOSTA_KOLEV).get().getNote());
        Assert.assertTrue(actualEntityCollections.getArtist(FILIP_KUTEV).get().getMissions().isEmpty());
        assertSetsEqual(expectedEntityCollections.getArtist(KOSTA_KOLEV).get().getMissions(),
                        actualEntityCollections.getArtist(KOSTA_KOLEV).get().getMissions());

        Assert.assertEquals(expectedEntityCollections.getPieces().size(), actualEntityCollections.getPieces().size());
        assertPiece(expectedEntityCollections.getPiece(0).get(), actualEntityCollections.getPiece(0).get());
    }

    private void assertPiece(FolklorePiece expected, FolklorePiece actual) {
        Assert.assertEquals(expected.getEthnographicRegion(), actual.getEthnographicRegion());
        Assert.assertEquals(expected.getAccompanimentPerformer(), actual.getAccompanimentPerformer());
        Assert.assertEquals(expected.getArrangementAuthor(), actual.getArrangementAuthor());
        Assert.assertEquals(expected.getAuthor(), actual.getAuthor());
        Assert.assertEquals(expected.getAlbumTrackOrder(), actual.getAlbumTrackOrder());
        Assert.assertEquals(expected.getConductor(), actual.getConductor());
        Assert.assertEquals(expected.getAlbum(), actual.getAlbum());
        Assert.assertEquals(expected.getDuration(), actual.getDuration());
        Assert.assertEquals(expected.getNote(), actual.getNote());
        Assert.assertEquals(expected.getPerformer(), actual.getPerformer());
        Assert.assertEquals(expected.getSoloist(), actual.getSoloist());
        Assert.assertEquals(expected.getTitle(), actual.getTitle());

        Assert.assertEquals(SOURCE_TYPE, actual.getSource().getType().getName());
        Assert.assertEquals(SOURCE_SIGNATURE, actual.getSource().getSignature());

        byte[] expectedRecordBytes = readRecordBytes();
        Assert.assertTrue(expectedRecordBytes.length > 0);
        Assert.assertArrayEquals(expectedRecordBytes, actual.getRecord().getBytes());
        Assert.assertEquals(RECORD_DATA_FORMAT, actual.getRecord().getDataFormat());
    }

    private void assertInitializedEntities(FolkloreEntityCollections entityCollections) {
        Assert.assertTrue(entityCollections.getArtists().isEmpty());
        Assert.assertTrue(entityCollections.getAlbums().isEmpty());
        Assert.assertTrue(entityCollections.getPieces().isEmpty());

        assertSetsEqual(FolkloreEntityCollectionFactory.createInstruments(), entityCollections.getInstruments());
        assertSetsEqual(FolkloreEntityCollectionFactory.createSources(entityCollections.getSourceTypes()),
                        entityCollections.getSources());
        assertSetsEqual(FolkloreEntityCollectionFactory.createEthnographicRegions(),
                        entityCollections.getEthnographicRegions());
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

        entityCollections.addAlbum(createAlbum());

        entityCollections.addSource(new Source(new SourceType(SOURCE_TYPE), SOURCE_SIGNATURE));

        for (int i = 0; i < numberPieceTableRecords; i++) {
            entityCollections.addPiece(createPiece(entityCollections, addMediaRecord));
        }
    }

    private static Album createAlbum() {
        Album album = new Album(ALBUM_COLLECTION_SIGNATURE);
        album.setDuration(ALBUM_DURATION);
        album.setTitle(ALBUM_TITLE);
        album.setProductionSignature(ALBUM_PRODUCTION_SIGNATURE);
        album.setNote(ALBUM_NOTE);
        return album;
    }

    private static Record createRecord() {
        Record record = new Record();
        record.setBytes(readRecordBytes());
        record.setDataFormat(RECORD_DATA_FORMAT);
        return record;
    }

    private static FolklorePiece createPiece(FolkloreEntityCollections entityCollections, boolean addMediaRecord) {
        FolklorePiece piece = new FolklorePiece();
        piece.setEthnographicRegion(entityCollections.getEthnographicRegion(ETHNOGRAPHIC_REGION).get());
        piece.setAccompanimentPerformer(entityCollections.getArtist(ENS_FILIP_KUTEV).get());
        piece.setArrangementAuthor(entityCollections.getArtist(FILIP_KUTEV).get());
        piece.setAuthor(entityCollections.getArtist(NO_AUTHOR).get());
        piece.setAlbumTrackOrder(TRACK_ORDER);
        piece.setConductor(entityCollections.getArtist(KOSTA_KOLEV).get());
        piece.setAlbum(entityCollections.getAlbum(ALBUM_COLLECTION_SIGNATURE).get());
        piece.setDuration(PIECE_DURATION);
        piece.setNote(PIECE_NOTE);
        piece.setPerformer(entityCollections.getArtist(TRIO).get());
        piece.setSoloist(entityCollections.getArtist(VERKA_SIDEROVA).get());
        piece.setTitle(PIECE_TITLE);
        piece.setSource(entityCollections.getSource(SOURCE_TYPE + "/" + SOURCE_SIGNATURE).get());
        if (addMediaRecord) {
            piece.setRecord(createRecord());
        }
        return piece;
    }

    private static byte[] readRecordBytes() {
        String path = FolkloreEntityCollectionsTest.class.getResource(RECORD_FILE).getPath();
        try (FileInputStream in = new FileInputStream(path)) {
            return in.readAllBytes();
        } catch (IOException e) {
            e.printStackTrace();
            return new byte[0];
        }
    }

}
