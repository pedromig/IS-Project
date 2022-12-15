package mei.dei.uc.model;

import org.json.JSONObject;

import java.sql.Timestamp;
import java.time.Instant;

public class Alert implements Cloneable {

    public static final String NORMAL = "Normal";
    public static final String SEVERE = "Severe";

    public static final String[] EVENTS = {"Hurricane", "Thunderstorm", "Blizzard", "Drought" };

    private WeatherStation station;
    private String location;
    private Timestamp start;
    private Timestamp end;
    private String type;
    private String event;

    public Alert(WeatherStation station, String location, String type, String event, Timestamp start, Timestamp end) {
        this(station, location, type, event);
        this.start = start;
        this.end = end;
    }

    public Alert(WeatherStation station, String location, String type, String event) {
        this.station = station;
        this.location = location;
        this.type = type;
        this.event = event;
        this.start = Timestamp.from(Instant.now());
        this.end = Timestamp.from(Instant.now().plusSeconds(station.getRng().nextInt(5, 15)));
    }

    public WeatherStation getStation() {
        return station;
    }

    public String getLocation() {
        return location;
    }

    public String getEvent() {
        return event;
    }

    public Timestamp getStart() {
        return start;
    }

    public Timestamp getEnd() {
        return end;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public JSONObject toJson() {
        return new JSONObject()
                .put("station", this.station.getName())
                .put("location", this.location)
                .put("type", this.type)
                .put("event", this.event)
                .put("start", this.start.toString())
                .put("end", this.end.toString());
    }

    public static Alert fromJson(JSONObject json) {
        return new Alert(
                new WeatherStation(json.getString("station")),
                json.getString("location"),
                json.getString("type"),
                json.getString("event"),
                Timestamp.valueOf(json.getString("start")),
                Timestamp.valueOf(json.getString("end")));
    }

    @Override
    public Alert clone() {
        try {
            Alert clone = (Alert) super.clone();
            clone.station = this.station;
            clone.location = this.location;
            clone.type = this.type;
            clone.event = this.event;
            clone.start = this.start;
            clone.end = this.end;
            return clone;
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }
}
