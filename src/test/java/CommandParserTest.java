import com.engine.commands.ParsedCommand;
import com.engine.services.CommandParser;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

//This test was written by DeepSeek
class CommandParserTest {

    private CommandParser parser;

    @BeforeEach
    void setup() {
        parser = new CommandParser();
    }

    @Test
    @DisplayName("Should parse a simple command without options")
    void shouldParseSimpleCommand() {
        // Given
        String input = "help";

        // When
        ParsedCommand result = parser.parse(input);

        // Then
        assertEquals("help", result.name(), "Command name should be 'help'");
        assertArrayEquals(new String[0], result.args(), "Should have no arguments");
    }

    @Test
    @DisplayName("Should parse a command with quoted argument and multiple options")
    void shouldParseCommandWithQuotedArgumentAndOptions() {
        // Given
        String input = "add-task \"Learn JUnit\" --project \"Java Core\" --priority HIGH --tag test --tag java";

        // When
        ParsedCommand result = parser.parse(input);

        // Then
        assertEquals("add-task", result.name());

        String[] args = result.args();
        assertAll(
                () -> assertEquals(9, args.length, "Should have 9 tokens"),
                () -> assertEquals("Learn JUnit", args[0]),
                () -> assertEquals("--project", args[1]),
                () -> assertEquals("Java Core", args[2]),
                () -> assertEquals("--priority", args[3]),
                () -> assertEquals("HIGH", args[4]),
                () -> assertEquals("--tag", args[5]),
                () -> assertEquals("test", args[6]),
                () -> assertEquals("--tag", args[7]),
                () -> assertEquals("java", args[8])
        );
    }

    @Test
    @DisplayName("Should throw exception for empty input")
    void shouldThrowExceptionForEmptyInput() {
        // Given
        String input = "   ";

        // When / Then
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> parser.parse(input),
                "Should throw exception"
        );
        assertEquals("Empty command line arguments", exception.getMessage());
    }

    @Test
    @DisplayName("Should throw exception for unclosed quote")
    void shouldThrowExceptionForUnclosedQuote() {
        // Given
        String input = "add-task \"Unclosed quote";

        // When / Then
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> parser.parse(input)
        );
        assertTrue(exception.getMessage().contains("Quotation may not follow quotes"),
                "Error message should indicate a quote problem");
    }

    @Test
    @DisplayName("Parser should allow unknown command names (validation is not its responsibility)")
    void shouldParseUnknownCommand() {
        // Given
        String input = "unknown-command --foo bar";

        // When
        ParsedCommand result = parser.parse(input);

        // Then
        assertEquals("unknown-command", result.name());
        assertArrayEquals(new String[]{"--foo", "bar"}, result.args(),
                "Parser should not validate command existence");
    }
}