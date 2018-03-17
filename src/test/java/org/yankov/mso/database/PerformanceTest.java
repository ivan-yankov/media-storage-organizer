package org.yankov.mso.database;

import org.junit.Assert;
import org.junit.Test;

public class PerformanceTest extends DatabaseTest {

    private static final int NUMBER_OF_RECORDS = 10000;
    private static final int MAX_TIME_WRITING_SEC = 60;
    private static final int MAX_TIME_READING_SEC = 5;
    private static final double MILLISECONDS_IN_SECOND = 1000.0;

    @Test
    public void testPerformance() {
        FolkloreEntityCollections collections = new FolkloreEntityCollections();
        collections.clear();
        collections.initialize();
        FolkloreEntityCollectionsTest.modifyEntityCollections(collections, false, NUMBER_OF_RECORDS);

        long start = System.currentTimeMillis();
        DatabaseOperations.saveToDatabase(collections);
        long end = System.currentTimeMillis();
        double executionTime = (end - start) / MILLISECONDS_IN_SECOND;
        Assert.assertTrue(executionTime < MAX_TIME_WRITING_SEC);

        start = System.currentTimeMillis();
        collections = DatabaseOperations.loadFromDatabase();
        end = System.currentTimeMillis();
        executionTime = (end - start) / MILLISECONDS_IN_SECOND;
        Assert.assertTrue(executionTime < MAX_TIME_READING_SEC);

        Assert.assertEquals(NUMBER_OF_RECORDS, collections.getPieces().size());
    }

}