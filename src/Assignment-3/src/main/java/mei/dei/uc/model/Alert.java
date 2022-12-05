package mei.dei.uc.model;

import org.json.JSONObject;

import java.sql.Timestamp;
import java.time.Instant;

public class Alert implements Cloneable {
    private WeatherStation station;
    private Timestamp timestamp;

    private Timestamp end;
    private AlertType type;
    private final Event event;


    public Alert(WeatherStation station, AlertType type, Event event, Timestamp timestamp) {
        this(station, type, event);
        this.timestamp = timestamp;
    }

    public Alert(WeatherStation station, AlertType type, Event event) {
        this.station = station;
        this.type = type;
        this.event = event;
        this.timestamp = Timestamp.from(Instant.now());
        this.end = Timestamp.from(Instant.now().plusSeconds(station.getRng().nextInt(0, 20)));
    }


    public enum AlertType {
        NORMAL("Normal"),
        SEVERE("Severe");

        AlertType(String normal) {
        }
    }

    public enum Event {
        HURRICANE("Hurricane"),
        THUNDERSTORM("Thunderstorm"),
        BLIZZARD("Blizzard"),
        DROUGHT("Drought");

        Event(String drought) {
        }
    }


    public Timestamp getEnd() {
        return end;
    }

    public void setType(AlertType type) {
        this.type = type;
    }

    public AlertType getType() {
        return type;
    }

    public JSONObject toJson() {
        return new JSONObject()
                .put("station", this.station.getName())
                .put("location", this.station.getLocation())
                .put("type", this.type.toString())
                .put("event", this.event.toString())
                .put("timestamp", this.timestamp.toString());
    }


    public static Alert fromJson(JSONObject json) {
        return new Alert(new WeatherStation(json.getString("station"), json.getString("location")),
                json.getEnum(AlertType.class, "type"),
                json.getEnum(Event.class, "event"),
                Timestamp.valueOf(json.getString("timestamp")));
    }


    @Override
    public Alert clone() {
        try {
            Alert clone = (Alert) super.clone();
            clone.station = this.station;
            clone.timestamp = this.timestamp;
            clone.type = this.type;
            clone.end = this.end;
            return clone;
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }
}
