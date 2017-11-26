package org.yankov.mso.database.generic;

import org.junit.Assert;
import org.junit.Test;
import org.yankov.mso.datamodel.generic.Instrument;

import java.util.Optional;

public class DatabaseSessionManagerTest extends DatabaseTest {

    private String instrumentName = "Инструмент";

    @Test
    public void testInstrumentWrite() {
        Instrument originalInstrument = new Instrument(instrumentName);

        databaseSessionManager.executeOperation(
                session -> { session.saveOrUpdate(originalInstrument); return null; }, Assert::fail);

        Optional<Object> instrumentObj = databaseSessionManager.executeOperation(
                session -> session.get(Instrument.class, originalInstrument.getId()), Assert::fail);

        Assert.assertTrue(instrumentObj.isPresent());
        Instrument instrument = (Instrument) instrumentObj.get();

        Assert.assertEquals(instrumentName, instrument.getName());
    }

}
