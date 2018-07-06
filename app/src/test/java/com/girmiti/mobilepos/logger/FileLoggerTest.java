package com.girmiti.mobilepos.logger;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.InjectMocks;

import java.io.IOException;

/**
 * << Add Comments Here >>
 *
 * @author Girmiti Software
 * @date 28-Jul-2017 4:00:00 pm
 * @version 1.0
 */
    public class FileLoggerTest {

    @InjectMocks
    private FileLogger fileLogger;

    private String name = "LOGGER";

    @Test
    public void testFileLogger() throws IOException {
        fileLogger = new FileLogger(name);
        fileLogger.newLogger("file logger");
        fileLogger.init();
        fileLogger.writeLog("logger");
        fileLogger.close();
        Assert.assertNotNull(fileLogger);
    }
}
