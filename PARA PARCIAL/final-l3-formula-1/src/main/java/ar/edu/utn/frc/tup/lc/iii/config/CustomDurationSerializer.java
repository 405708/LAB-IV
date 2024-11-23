package ar.edu.utn.frc.tup.lc.iii.config;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import java.io.IOException;
import java.time.Duration;

public class CustomDurationSerializer extends StdSerializer<Duration> {

    public CustomDurationSerializer() {
        this(null);
    }

    public CustomDurationSerializer(Class<Duration> t) {
        super(t);
    }

    @Override
    public void serialize(Duration duration, JsonGenerator gen, SerializerProvider provider) throws IOException {
        long hours = duration.toHours();
        long minutes = duration.toMinutes() % 60;
        long seconds = duration.getSeconds() % 60;
        long millis = duration.toMillis() % 1000;

        if(hours > 0L) {
            gen.writeString(String.format("%d:%02d:%02d.%03d", hours, minutes, seconds, millis));
        } else {
            gen.writeString(String.format("%02d:%02d.%03d", minutes, seconds, millis));
        }
    }
}