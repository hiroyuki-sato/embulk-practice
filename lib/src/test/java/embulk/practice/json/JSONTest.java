package embulk.practice.json;

import org.embulk.util.json.JsonParser;
import org.junit.Test;
import org.msgpack.value.Value;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import static org.junit.Assert.fail;

public class JSONTest
{
    @Test
    public void basicJSONParseTest()
    {
        JsonParser parser = new JsonParser();
        String json = "{\"hoge\":\"HOGE\",\"fuga\":\"FUGA\"}";

        Value obj;
        obj = parser.parse(json);
        System.out.println(obj);
    }

    @Test
    public void basicJsonStreamTest()
    {
        String json = "{\"hoge\":\"HOGE2\",\"fuga\":\"FUGA2\"}";
        InputStream stream = new ByteArrayInputStream(json.getBytes(StandardCharsets.UTF_8));
        JsonParser parser = new JsonParser();
        try {
            Value v;
            JsonParser.Stream jsonStream = parser.open(stream);
            while( (v = jsonStream.next()) != null) {
                System.out.println(v);
            };

        } catch (IOException ex) {
            fail();
        }
    }
}
