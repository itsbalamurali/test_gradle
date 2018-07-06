package com.girmiti.mobilepos.logger;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.InjectMocks;

/**
 * << Add Comments Here >>
 *
 * @author Girmiti Software
 * @date 04-Aug-2017 11:00:00 am
 * @version 1.0
 */
public class ConsoleLoggerTest{

    @InjectMocks
    ConsoleLogger consoleLogger =new ConsoleLogger();

        @Test
        public void init() throws Exception {
        consoleLogger.init();
        consoleLogger.close();
    }
    @Test
    public void newLogger() throws Exception {
        Assert.assertNotNull(consoleLogger.newLogger("print"));
    }

    @Test
    public void writeLog() throws Exception {
        consoleLogger.writeLog("hello");

    }
}