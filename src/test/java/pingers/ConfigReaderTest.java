package pingers;

import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class ConfigReaderTest {

    @Test
    public void given_valid_file_when_read_string_property_should_return_value() throws IOException {

        // Act
        String value = ConfigReader.readAsString("hosts");

        // Assert
        assertEquals("jasmin.com,oranum.com", value);
    }

    @Test
    public void given_valid_file_when_read_array_property_should_return_value() throws IOException {

        // Act
        String[] value = ConfigReader.readAsStringArray("hosts");

        // Assert
        assertArrayEquals(new String[]{ "jasmin.com","oranum.com" }, value);
    }

    @Test
    public void given_valid_file_when_read_int_property_should_return_value() throws IOException {

        // Act
        int value = ConfigReader.readAsInt("pingDelay");

        // Assert
        assertEquals(500, value);
    }
}
