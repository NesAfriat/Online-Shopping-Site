package com.example.seprojectweb.ScenarioTests;

import com.example.seprojectweb.Domain.PersistenceManager;
import com.example.seprojectweb.Logger;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class LoggerTest {

    private static Logger logger;


    @BeforeAll
    static void beforeAll() {
        PersistenceManager.setDBConnection("test");
        logger = Logger.getInstance();
    }



    @Test
    void writeEvents() {
        logger.logEvent("event 1\n");
        logger.logEvent("event 2\n");
        logger.writeEvents();
    }
}