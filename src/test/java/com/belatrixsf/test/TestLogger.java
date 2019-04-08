package com.belatrixsf.test;

import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.logging.*;

import static org.junit.Assert.assertTrue;

public class TestLogger {

    private static Logger log = Logger.getLogger("TestLog"); // matches the logger in the affected class
    private static OutputStream logCapturingStream;
    private static StreamHandler customLogHandler;

    @Before
    public void attachLogCapturer() {
        logCapturingStream = new ByteArrayOutputStream();
        Handler[] handlers = log.getParent().getHandlers();
        customLogHandler = new StreamHandler(logCapturingStream, handlers[0].getFormatter());
        log.addHandler(customLogHandler);
    }

    @Test
    public void test() {
        Logger logger = Logger.getLogger("TestLog");
        logger.setLevel(Level.ALL);
        SimpleFormatter simpleFormatter = new SimpleFormatter();

        FileHandler fileHandler = null;
        try {
            fileHandler = new FileHandler("fileLog.log", true);
            fileHandler.setFormatter(simpleFormatter);
            logger.addHandler(fileHandler);

            logger.info("Logger test");

            String capturedLog = getTestCapturedLog();
            assertTrue(capturedLog.contains("Logger test"));

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String getTestCapturedLog() throws IOException {
        customLogHandler.flush();
        return logCapturingStream.toString();
    }
}