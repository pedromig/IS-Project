package mei.dei.uc.model;


import org.json.JSONObject;

import java.sql.Timestamp;
import java.time.Instant;

public class Measurement {
    private final WeatherStation station;
    private final String location;
    private final double temperature;
    private Timestamp timestamp;


    public Measurement(WeatherStation station, String location, double temperature, Timestamp timestamp) {
        this(station, location, temperature);
        this.timestamp = timestamp;
    }

    public Measurement(WeatherStation station, String location, double temperature) {
        this.station = station;
        this.location = location;
        this.temperature = temperature;
        this.timestamp = Timestamp.from(Instant.now());
    }

    public String getLocation() {
        return this.location;
    }

    public WeatherStation getStation() {
        return station;
    }

    public double getTemperature() {
        return temperature;
    }

    public Timestamp getTimestamp() {
        return this.timestamp;
    }

    public JSONObject toJson() {
        return new JSONObject()
                .put("station", this.station.getName())
                .put("location", this.location)
                .put("temperature", this.temperature)
                .put("timestamp", this.timestamp.toString());
    }

    public static Measurement fromJson(JSONObject json) {
        return new Measurement(
                new WeatherStation(json.getString("station")),
                json.getString("location"),
                json.getDouble("temperature"),
                Timestamp.valueOf(json.getString("timestamp")));
    }

    @Override
    public String toString() {
        return "Measurement(" + "station=" + this.station.getName() +
                "location=" + this.location +
                ", temperature=" + this.temperature +
                ", timestamp=" + this.timestamp +
                ')';
    }
}
