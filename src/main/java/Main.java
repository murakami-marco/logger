import com.belatrixsf.handlers.JDBCHandler;

import java.io.IOException;
import java.util.logging.*;

public class Main {

    private static final String DRIVER = "org.postgresql.Driver";
    private static final String CONNECTION = "jdbc:postgresql://localhost:5432/logtest";

    public static void main(String[] args) {
        Logger logger = Logger.getLogger("TestLog");
        logger.setLevel(Level.ALL);
        SimpleFormatter simpleFormatter = new SimpleFormatter();

        try {
            FileHandler fileHandler = new FileHandler("fileLog.log", true);
            fileHandler.setFormatter(simpleFormatter);
            logger.addHandler(fileHandler);

            ConsoleHandler consoleHandler = new ConsoleHandler();
            consoleHandler.setFormatter(simpleFormatter);
            logger.addHandler(consoleHandler);

            JDBCHandler jdbcHandler = new JDBCHandler(DRIVER, CONNECTION, "postgres", "postgres@postgres");
            logger.addHandler(jdbcHandler);

            logger.info("Logger test");
            logger.severe("Error");
            logger.warning("Warning Test");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
