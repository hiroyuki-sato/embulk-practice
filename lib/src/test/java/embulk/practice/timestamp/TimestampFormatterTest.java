package embulk.practice.timestamp;

import org.embulk.util.timestamp.TimestampFormatter;
import org.junit.Test;

import java.time.Instant;

import static org.junit.Assert.assertEquals;

public class TimestampFormatterTest
{
    @Test
    public void basicParserTest(){
        Instant p = Instant.ofEpochSecond(0);
        TimestampFormatter formatter = TimestampFormatter
                .builder("%Y-%m-%d %H:%M:%S.%N %z",true)
                .setDefaultZoneFromString("UTC")
                .setDefaultDateFromString("1970-01-01")
                .build();
        TimestampFormatter formatter2 = TimestampFormatter
                .builder("%Y-%m-%d %H:%M:%S.%N %z",true)
                .setDefaultZoneFromString("+09:00")
                .setDefaultDateFromString("1970-01-01")
                .build();

        String format_string = formatter.format(p);
        String format_string2 = formatter2.format(p);

        assertEquals("1970-01-01 00:00:00.000000000 +0000",format_string);
        assertEquals("1970-01-01 00:00:00.000000000 +0900",format_string2);
    }


}
