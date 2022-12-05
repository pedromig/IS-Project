package mei.dei.uc.model;


import org.json.JSONObject;

import java.sql.Timestamp;
import java.time.Instant;

public class Measurement {
    private final WeatherStation station;
    private final double temperature;
    private Timestamp timestamp;


    public Measurement(WeatherStation station, double temperature, Timestamp timestamp) {
        this(station, temperature);
        this.timestamp = timestamp;
    }

    public Measurement(WeatherStation station, double temperature) {
        this.station = station;
        this.temperature = temperature;
        this.timestamp = Timestamp.from(Instant.now());
    }

    public WeatherStation getStation() {
        return station;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public double getTemperature() {
        return temperature;
    }

    public JSONObject toJson() {
        return new JSONObject()
                .put("station", this.station.getName())
                .put("location", this.station.getLocation())
                .put("temperature", this.temperature)
                .put("timestamp", this.timestamp.toString());
    }

    public static Measurement fromJson(JSONObject json) {
        return new Measurement(
                new WeatherStation(
                        json.getString("station"),
                        json.getString("location")),
                json.getDouble("temperature"),
                Timestamp.valueOf(json.getString("timestamp")));
    }

    @Override
    public String toString() {
        return "Measurement(" + "station=" + this.station +
                ", temperature=" + this.temperature +
                ", timestamp=" + this.timestamp +
                ')';
    }
}
