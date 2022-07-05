package ScenarioTests;

import Domain.InnerLogicException;
import Domain.Market.*;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

public class LoggerTest {

    private static Logger logger;

    @BeforeAll
    static void initLoger() {
        logger = Logger.getInstance();
    }

    @Test
    void writeEvents() {
        logger.logEvent("event 1\n");
        logger.logEvent("event 2\n");
        logger.writeEvents();
    }
}