package org.yankov.mso.database.folklore;

import org.junit.Assert;
import org.junit.Test;
import org.yankov.mso.database.generic.DatabaseTest;
import org.yankov.mso.datamodel.folklore.FolklorePiece;
import org.yankov.mso.datamodel.generic.*;

import java.io.FileInputStream;
import java.io.IOException;
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

    private static final String RECORD_FILE = "./src/test/resources/record.flac";

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
        expectedEntityCollections.getSourceTypes().addAll(FolkloreEntityCollectionFactory.createSourceTypes());
        expectedEntityCollections.getInstruments().addAll(FolkloreEntityCollectionFactory.createInstruments());
        expectedEntityCollections.getEthnographicRegions().addAll(
                FolkloreEntityCollectionFactory.createEthnographicRegions());
        modifyEntityCollections(expectedEntityCollections, true, 1);

        assertStoredEntities(expectedEntityCollections, actualEntityCollections);
    }

    @Test
    public void testAddGetSourceType() {
        FolkloreEntityCollections collections = new FolkloreEntityCollections();

        Assert.assertTrue(collections.addSourceType("Грамофонна плоча"));
        Assert.assertFalse(collections.addSourceType(" грамофонна плоча "));
        Assert.assertFalse(collections.addSourceType("гРАМОФОННА ПЛОЧА"));
        Assert.assertTrue(collections.addSourceType("Грамофоннаплоча"));

        Assert.assertTrue(collections.getSourceType("грамофонна плоча ").isPresent());
        Assert.assertFalse(collections.getSourceType("грамофонна_плоча").isPresent());
    }

    @Test
    public void testAddGetInstrument() {
        FolkloreEntityCollections collections = new FolkloreEntityCollections();

        Assert.assertTrue(collections.addInstrument("Флигорна"));
        Assert.assertFalse(collections.addInstrument(" флигорна "));
        Assert.assertFalse(collections.addInstrument(" фЛИГОРНА"));
        Assert.assertTrue(collections.addInstrument("Тромпет"));

        Assert.assertTrue(collections.getInstrument("флигорна ").isPresent());
        Assert.assertFalse(collections.getInstrument("фагот").isPresent());
    }

    @Test
    public void testAddGetArtist() {
        FolkloreEntityCollections collections = new FolkloreEntityCollections();

        Assert.assertTrue(collections.addArtist("Борис Машалов"));
        Assert.assertFalse(collections.addArtist(" борис машалов "));
        Assert.assertFalse(collections.addArtist(" бОРИС МАШАЛОВ"));
        Assert.assertTrue(collections.addArtist("Мита Стойчева"));

        Assert.assertTrue(collections.getArtist("борис машалов ").isPresent());
        Assert.assertFalse(collections.getArtist("Борис Карлов").isPresent());
    }

    @Test
    public void testAddGetDisc() {
        FolkloreEntityCollections collections = new FolkloreEntityCollections();

        Assert.assertTrue(collections.addAlbum("F1"));
        Assert.assertFalse(collections.addAlbum(" f1 "));
        Assert.assertTrue(collections.addAlbum("F2"));

        Assert.assertTrue(collections.getAlbum("f1 ").isPresent());
        Assert.assertFalse(collections.getAlbum("F3").isPresent());
    }

    @Test
    public void testAddGetEthnographicRegion() {
        FolkloreEntityCollections collections = new FolkloreEntityCollections();

        Assert.assertTrue(collections.addEthnographicRegion("Тракийска"));
        Assert.assertFalse(collections.addEthnographicRegion(" тракийска "));
        Assert.assertFalse(collections.addEthnographicRegion(" тРАКИЙСКА "));
        Assert.assertTrue(collections.addEthnographicRegion("Родопска"));

        Assert.assertTrue(collections.getEthnographicRegion("тракийска ").isPresent());
        Assert.assertFalse(collections.getEthnographicRegion("Северняшка").isPresent());
    }

    private void assertStoredEntities(FolkloreEntityCollections expectedEntityCollections,
                                      FolkloreEntityCollections actualEntityCollections) {
        assertSetsEqual(expectedEntityCollections.getSourceTypes(), actualEntityCollections.getSourceTypes());
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
        assertPiece(expectedEntityCollections.getPieces().get(0), actualEntityCollections.getPieces().get(0));
    }

    private void assertPiece(FolklorePiece expected, FolklorePiece actual) {
        Assert.assertEquals(expected.getEthnographicRegion(), actual.getEthnographicRegion());
        Assert.assertEquals(expected.getAccompanimentPerformer(), actual.getAccompanimentPerformer());
        Assert.assertEquals(expected.getArrangementAuthor(), actual.getArrangementAuthor());
        Assert.assertEquals(expected.getAuthor(), actual.getAuthor());
        Assert.assertEquals(expected.getCdTrackOrder(), actual.getCdTrackOrder());
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
        assertSetsEqual(FolkloreEntityCollectionFactory.createSourceTypes(), entityCollections.getSourceTypes());
        assertSetsEqual(FolkloreEntityCollectionFactory.createEthnographicRegions(),
                entityCollections.getEthnographicRegions());
    }

    private <T> void assertSetsEqual(Set<T> expected, Set<T> actual) {
        Assert.assertTrue(actual.size() == expected.size());
        Queue<T> actualQueue = new ArrayDeque<>();
        actualQueue.addAll(actual);
        while (actualQueue.peek() != null) {
            Assert.assertTrue(expected.contains(actualQueue.poll()));
        }
    }

    public static void modifyEntityCollections(FolkloreEntityCollections entityCollections,
                                               boolean addMediaRecord, int numberPieceTableRecords) {
        entityCollections.addInstrument(ACCORDEON);

        entityCollections.addArtist(KOSTA_KOLEV);
        entityCollections.getArtist(KOSTA_KOLEV).ifPresent(artist -> {
            artist.setInstrument(entityCollections.getInstrument(ACCORDEON).get());
            artist.setNote(KOSTA_KOLEV_NOTE);
            artist.addMission(ArtistMission.INSTRUMENT_PLAYER);
            artist.addMission(ArtistMission.COMPOSER);
            artist.addMission(ArtistMission.CONDUCTOR);
        });
        entityCollections.addArtist(FILIP_KUTEV);
        entityCollections.addArtist(ENS_FILIP_KUTEV);
        entityCollections.addArtist(VERKA_SIDEROVA);
        entityCollections.addArtist(NO_AUTHOR);
        entityCollections.addArtist(TRIO);

        entityCollections.getAlbums().add(createAlbum());

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

    private static Source createSource(SourceType sourceType) {
        Source source = new Source(sourceType);
        source.setSignature(SOURCE_SIGNATURE);
        return source;
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
        piece.setCdTrackOrder(TRACK_ORDER);
        piece.setConductor(entityCollections.getArtist(KOSTA_KOLEV).get());
        piece.setAlbum(entityCollections.getAlbum(ALBUM_COLLECTION_SIGNATURE).get());
        piece.setDuration(PIECE_DURATION);
        piece.setNote(PIECE_NOTE);
        piece.setPerformer(entityCollections.getArtist(TRIO).get());
        piece.setSoloist(entityCollections.getArtist(VERKA_SIDEROVA).get());
        piece.setTitle(PIECE_TITLE);
        piece.setSource(createSource(entityCollections.getSourceType(SOURCE_TYPE).get()));
        if (addMediaRecord) {
            piece.setRecord(createRecord());
        }
        return piece;
    }

    private static byte[] readRecordBytes() {
        try (FileInputStream in = new FileInputStream(Paths.get(RECORD_FILE).toRealPath().toFile())) {
            return in.readAllBytes();
        } catch (IOException e) {
            e.printStackTrace();
            return new byte[0];
        }
    }

}
