package pingers;

import org.apache.logging.log4j.LogManager;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import static java.util.Objects.nonNull;

public class ConfigReader {

    private static final org.apache.logging.log4j.Logger logger = LogManager.getLogger();

    private static final String CONFIG_FILE_NAME = "config.properties";

    private static final String STRING_LIMITER = ",";

    private ConfigReader() {
        throw new IllegalStateException("This is a helper class. It is not necessary instantiated it.");
    }

    public static String readAsString(String key) throws IOException {

        InputStream inputStream = ConfigReader.class.getClassLoader().getResourceAsStream(CONFIG_FILE_NAME);

        if (nonNull(inputStream)) {

            Properties properties = new Properties();

            properties.load(inputStream);

            return properties.getProperty(key);
        }

        throw new FileNotFoundException(CONFIG_FILE_NAME + " not found");
    }

    public static String[] readAsStringArray(String key) throws IOException {

        return readAsString(key).split(STRING_LIMITER);
    }

    public static Integer readAsInt(String key) throws IOException {

        Integer converted = null;

        try {
            String rawValue = readAsString(key);

            if (nonNull(rawValue)) {
                converted = Integer.parseInt(rawValue);
            }
        } catch (NumberFormatException numberFormatException) {
            converted = null;

            logger.error(numberFormatException);
        }

        return converted;
    }
}
