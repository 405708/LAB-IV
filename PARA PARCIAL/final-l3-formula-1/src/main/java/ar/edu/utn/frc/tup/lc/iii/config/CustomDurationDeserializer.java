package ar.edu.utn.frc.tup.lc.iii.config;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;
import java.time.Duration;

public class CustomDurationDeserializer extends JsonDeserializer<Duration> {

    @Override
    public Duration deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {
        String durationString = p.getText();
        String[] parts = durationString.split(":");
        if (parts.length == 3) {
            long hours = Long.parseLong(parts[0]);
            long minutes = Long.parseLong(parts[1]);
            String[] secAndMillis = parts[2].split("\\.");
            long seconds = Long.parseLong(secAndMillis[0]);
            long millis = Long.parseLong(secAndMillis[1]);
            return Duration.ofHours(hours).plusMinutes(minutes).plusSeconds(seconds).plusMillis(millis);
        } else if (parts.length == 2) {
            long minutes = Long.parseLong(parts[0]);
            String[] secAndMillis = parts[1].split("\\.");
            long seconds = Long.parseLong(secAndMillis[0]);
            long millis = Long.parseLong(secAndMillis[1]);
            return Duration.ofMinutes(minutes).plusSeconds(seconds).plusMillis(millis);
        } else {
            throw new IOException("Invalid duration format");
        }
    }
}
