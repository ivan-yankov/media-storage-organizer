package org.yankov.mso.database;

import org.junit.Assert;
import org.junit.Test;
import org.yankov.mso.application.ApplicationContext;
import org.yankov.mso.datamodel.Instrument;

import java.util.Optional;

public class DatabaseManagerTest extends DatabaseTest {

    private String instrumentName = "Тестов инструмент";

    @Test
    public void test() {
        Instrument originalInstrument = new Instrument(instrumentName);

        ApplicationContext.getInstance().getDatabaseManager().executeOperation(session -> {
            session.saveOrUpdate(originalInstrument);
            return null;
        });

        Optional<Object> instrumentObj = ApplicationContext.getInstance().getDatabaseManager().executeOperation(
            session -> session.get(Instrument.class, originalInstrument.getId()));

        Assert.assertTrue(instrumentObj.isPresent());
        Instrument instrument = (Instrument) instrumentObj.get();

        Assert.assertEquals(instrumentName, instrument.getName());
    }

}
