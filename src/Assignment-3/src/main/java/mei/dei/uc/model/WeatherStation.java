package mei.dei.uc.model;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Random;

public class WeatherStation {
    private static final float SEVERE_ALERT_GENERATION_PROBABILITY = 0.3F;

    private final String name;
    private final String location;
    private final ArrayList<Alert> alerts;

    private Random rng;

    public WeatherStation(String name, String location, long seed) {
        this(name, location);
        this.rng = new Random(seed);
    }

    public WeatherStation(String name, String location) {
        this.name = name;
        this.location = location;
        this.rng = new Random(42);
        this.alerts = new ArrayList<>();
    }

    public Measurement getMeasurement() {
        double temperature = rng.nextGaussian() * 35 + 70;
        if (rng.nextFloat(1) < WeatherStation.SEVERE_ALERT_GENERATION_PROBABILITY) {
            this.alerts.add(new Alert(this, Alert.AlertType.SEVERE, Alert.Event.values()[rng.nextInt(Alert.Event.values().length)]));
        }
        return new Measurement(this, temperature);
    }

    public ArrayList<Alert> alerts() throws CloneNotSupportedException {
        ArrayList<Alert> alerts = new ArrayList<>();

        // Update Alerts
        for (Alert alert : this.alerts) {
            if (alert.getType() == Alert.AlertType.SEVERE) {
                alerts.add(alert.clone());
                alert.setType(Alert.AlertType.NORMAL);
            }
        }

        // Add Alert to the list when finished
        Timestamp now = Timestamp.from(Instant.now());
        ArrayList<Alert> tmp = new ArrayList<>();
        for (Alert alert : alerts) {
            if (now.equals(alert.getEnd())) {
                tmp.add(alert);
                this.alerts.remove(alert);
            }
        }
        alerts.addAll(tmp);
        return alerts;
    }

    public String getName() {
        return name;
    }

    public String getLocation() {
        return location;
    }

    public Random getRng() {
        return this.rng;
    }

    @Override
    public String toString() {
        return "WeatherStation(name=" + this.getName() + ", location=" + this.getLocation() + ")";
    }
}
