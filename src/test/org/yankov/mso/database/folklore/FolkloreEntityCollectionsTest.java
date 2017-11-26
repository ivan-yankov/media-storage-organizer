package org.yankov.mso.database.folklore;

import org.junit.Assert;
import org.junit.Test;
import org.yankov.mso.database.generic.DatabaseTest;
import org.yankov.mso.database.org.yankov.mso.database.folklore.FolkloreEntityCollectionFactory;
import org.yankov.mso.database.org.yankov.mso.database.folklore.FolkloreEntityCollections;
import org.yankov.mso.datamodel.folklore.FolklorePiece;
import org.yankov.mso.datamodel.generic.Disc;
import org.yankov.mso.datamodel.generic.Record;
import org.yankov.mso.datamodel.generic.Source;

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
    private static final String FILIP_KUTEV = "Филип Кутев";
    private static final String ENS_FILIP_KUTEV = "Ансамбъл 'Филип Кутев'";
    private static final String VERKA_SIDEROVA = "Верка Сидерова";
    private static final String DISC_COLLECTION_SIGNATURE = "F1";
    private static final String DISC_TITLE = "Българска народна музика";
    private static final String DISC_PRODUCTION_SIGNATURE = "ВНА-001";
    private static final String DISC_NOTE = "Тестов диск";
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

    private static final String RECORD_FILE = "./src/test/resources/record.flac";

    private final FolkloreEntityCollections entityCollections;

    private final FolklorePiece piece;

    public FolkloreEntityCollectionsTest() {
        this.entityCollections = new FolkloreEntityCollections();
        this.piece = new FolklorePiece();
    }

    @Test
    public void testInitializeEntityCollections() {
        entityCollections.initializeEntityCollections();
        assertInitializedEntities();

        addEntities(entityCollections);
        entityCollections.saveEntityCollections();
        entityCollections.initializeEntityCollections();

        assertStoredEntities();
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

        Assert.assertTrue(collections.addDisc("F1"));
        Assert.assertFalse(collections.addDisc(" f1 "));
        Assert.assertTrue(collections.addDisc("F2"));

        Assert.assertTrue(collections.getDisc("f1 ").isPresent());
        Assert.assertFalse(collections.getDisc("F3").isPresent());
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

    private void assertStoredEntities() {
        FolkloreEntityCollections expectedEntityCollections = new FolkloreEntityCollections();
        expectedEntityCollections.getSourceTypes().addAll(FolkloreEntityCollectionFactory.createSourceTypes());
        expectedEntityCollections.getInstruments().addAll(FolkloreEntityCollectionFactory.createInstruments());
        expectedEntityCollections.getEthnographicRegions().addAll(
                FolkloreEntityCollectionFactory.createEthnographicRegions());
        addEntities(expectedEntityCollections);

        assertSetsEquals(expectedEntityCollections.getSourceTypes(), entityCollections.getSourceTypes());
        assertSetsEquals(expectedEntityCollections.getInstruments(), entityCollections.getInstruments());
        assertSetsEquals(expectedEntityCollections.getArtists(), entityCollections.getArtists());
        assertSetsEquals(expectedEntityCollections.getDiscs(), entityCollections.getDiscs());
        assertSetsEquals(expectedEntityCollections.getEthnographicRegions(),
                entityCollections.getEthnographicRegions());
        Assert.assertArrayEquals(expectedEntityCollections.getPieces().toArray(new FolklorePiece[0]),
                entityCollections.getPieces().toArray(new FolklorePiece[0]));

        Assert.assertTrue(entityCollections.getArtist(KOSTA_KOLEV).isPresent());
        Assert.assertEquals(ACCORDEON, entityCollections.getArtist(KOSTA_KOLEV).get().getInstrument().getName());

        assertPiece(piece, entityCollections.getPieces().get(0));
    }

    private void assertPiece(FolklorePiece expected, FolklorePiece actual) {
        Assert.assertEquals(expected.getEthnographicRegion(), actual.getEthnographicRegion());
        Assert.assertEquals(expected.getAccompanimentPerformer(), actual.getAccompanimentPerformer());
        Assert.assertEquals(expected.getArrangementAuthor(), actual.getArrangementAuthor());
        Assert.assertEquals(expected.getAuthor(), actual.getAuthor());
        Assert.assertEquals(expected.getCdTrackOrder(), actual.getCdTrackOrder());
        Assert.assertEquals(expected.getConductor(), actual.getConductor());
        Assert.assertEquals(expected.getDisc(), actual.getDisc());
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

    private void assertInitializedEntities() {
        Assert.assertTrue(entityCollections.getArtists().isEmpty());
        Assert.assertTrue(entityCollections.getDiscs().isEmpty());
        Assert.assertTrue(entityCollections.getPieces().isEmpty());

        assertSetsEquals(FolkloreEntityCollectionFactory.createInstruments(), entityCollections.getInstruments());
        assertSetsEquals(FolkloreEntityCollectionFactory.createSourceTypes(), entityCollections.getSourceTypes());
        assertSetsEquals(FolkloreEntityCollectionFactory.createEthnographicRegions(),
                entityCollections.getEthnographicRegions());
    }

    private <T> void assertSetsEquals(Set<T> expected, Set<T> actual) {
        Assert.assertTrue(actual.size() == expected.size());
        Queue<T> actualQueue = new ArrayDeque<>();
        actualQueue.addAll(actual);
        while (actualQueue.peek() != null) {
            Assert.assertTrue(expected.contains(actualQueue.poll()));
        }
    }

    private void addEntities(FolkloreEntityCollections entityCollections) {
        entityCollections.addInstrument(ACCORDEON);

        entityCollections.addArtist(KOSTA_KOLEV);
        entityCollections.getArtist(KOSTA_KOLEV).ifPresent(
                artist -> artist.setInstrument(entityCollections.getInstrument(ACCORDEON).get()));
        entityCollections.addArtist(FILIP_KUTEV);
        entityCollections.addArtist(ENS_FILIP_KUTEV);
        entityCollections.addArtist(VERKA_SIDEROVA);
        entityCollections.addArtist(NO_AUTHOR);
        entityCollections.addArtist(TRIO);

        Disc disc = new Disc(DISC_COLLECTION_SIGNATURE);
        disc.setDuration(Duration.ofMinutes(70));
        disc.setTitle(DISC_TITLE);
        disc.setProductionSignature(DISC_PRODUCTION_SIGNATURE);
        disc.setNote(DISC_NOTE);
        entityCollections.getDiscs().add(disc);

        Source source = new Source(entityCollections.getSourceType(SOURCE_TYPE).get());
        source.setSignature(SOURCE_SIGNATURE);

        Record record = new Record();
        record.setBytes(readRecordBytes());
        record.setDataFormat(RECORD_DATA_FORMAT);

        piece.setEthnographicRegion(entityCollections.getEthnographicRegion(ETHNOGRAPHIC_REGION).get());
        piece.setAccompanimentPerformer(entityCollections.getArtist(ENS_FILIP_KUTEV).get());
        piece.setArrangementAuthor(entityCollections.getArtist(FILIP_KUTEV).get());
        piece.setAuthor(entityCollections.getArtist(NO_AUTHOR).get());
        piece.setCdTrackOrder(TRACK_ORDER);
        piece.setConductor(entityCollections.getArtist(KOSTA_KOLEV).get());
        piece.setDisc(entityCollections.getDisc(DISC_COLLECTION_SIGNATURE).get());
        piece.setDuration(PIECE_DURATION);
        piece.setNote(PIECE_NOTE);
        piece.setPerformer(entityCollections.getArtist(TRIO).get());
        piece.setSoloist(entityCollections.getArtist(VERKA_SIDEROVA).get());
        piece.setTitle(PIECE_TITLE);
        piece.setSource(source);
        piece.setRecord(record);
        entityCollections.getPieces().add(piece);
    }

    private byte[] readRecordBytes() {
        try (FileInputStream in = new FileInputStream(Paths.get(RECORD_FILE).toRealPath().toFile())) {
            return in.readAllBytes();
        } catch (IOException e) {
            e.printStackTrace();
            return new byte[0];
        }
    }

}
