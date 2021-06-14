package embulk.practice.timestamp;

import org.embulk.util.timestamp.TimestampFormatter;
import org.junit.Test;

import java.time.Instant;
import java.time.format.DateTimeParseException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

public class TimestampParserTest
{
    @Test
    public void basicParserTest(){
        Instant p;
        TimestampFormatter parser = TimestampFormatter
                .builder("%Y-%m-%d %H:%M:%S.%N %z",true)
                .setDefaultZoneFromString("UTC")
                .setDefaultDateFromString("1970-01-01")
                .build();

        p = parser.parse("1970-01-01 00:00:00.000000 UTC");
        assertEquals("hogehoge",Instant.ofEpochSecond(0),p);

        p = parser.parse("1970-01-01 00:00:00.000000 Z");
        assertEquals("hogehoge",Instant.ofEpochSecond(0),p);

        p = parser.parse("1970-01-01 09:00:00.000000 +09:00");
        assertEquals("hogehoge",Instant.ofEpochSecond(0),p);

//        assertTrue(false, "hogehoge");
    }

    @Test
    public void exceptionParserTest()
    {
        Instant p;
        TimestampFormatter parser = TimestampFormatter
                .builder("%Y-%m-%d %H:%M:%S.%N %z",true)
                .setDefaultZoneFromString("UTC")
                .setDefaultDateFromString("1970-01-01")
                .build();

        assertThrows("hogehoge",DateTimeParseException.class, () -> parser.parse("hoghoge"));
//        p = parser.parse("hogehoge");

    }

}
