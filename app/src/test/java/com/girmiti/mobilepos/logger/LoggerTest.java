package com.girmiti.mobilepos.logger;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.InjectMocks;

/**
 * << Add Comments Here >>
 *
 * @author Girmiti Software
 * @date 28-Jul-2017 11:00:00 pm
 * @version 1.0
 */
public class LoggerTest {

    @InjectMocks
    Logger logger = Logger.getNewLogger("file");

    private com.girmiti.mobilepos.logger.Logger consoleLogger;

    @Test
    public void testLogger() {
        consoleLogger = ConsoleLogger.getNewLogger("com.girmiti.mobilepos.logger.ConsoleLogger");
        logger = Logger.getNewLogger(consoleLogger);
        logger.debug("debug message here");
        logger.info("info message here");
        logger.severe("exception trace here");
        logger.diagnose("exeption detailed here");
        logger.trace("trace message here");
        logger.error("error message here");
        logger.setLevel(1);
        logger.closeLog();
        Assert.assertNotNull(logger);
    }
}