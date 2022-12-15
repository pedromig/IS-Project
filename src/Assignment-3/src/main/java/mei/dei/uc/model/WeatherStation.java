package mei.dei.uc.model;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Random;

public class WeatherStation {
    private static final float SEVERE_ALERT_GENERATION_PROBABILITY = 0.2F;
    private static final int KEEP_ALIVE = 2;
    private final String name;
    private final ArrayList<Alert> alerts;
    private final Random rng;
    private int live;

    public WeatherStation(String name) {
        this.name = name;
        this.rng = new Random(42);
        this.alerts = new ArrayList<>();
        this.live = KEEP_ALIVE;
    }

    public Measurement getMeasurement(String location) {
        if (rng.nextFloat(1) < WeatherStation.SEVERE_ALERT_GENERATION_PROBABILITY) {
            this.alerts.add(new Alert(this, location,
                    Alert.SEVERE, Alert.EVENTS[rng.nextInt(Alert.EVENTS.length)]));
        }
        double temperature = rng.nextDouble(32, 104);
        return new Measurement(this, location, temperature);
    }

    public ArrayList<Alert> alerts() {
        ArrayList<Alert> alerts = new ArrayList<>();
        // Update Alerts
        for (Alert alert : this.alerts) {
            if (alert.getType().equals(Alert.SEVERE)) {
                alerts.add(alert.clone());
                alert.setType(Alert.NORMAL);
            }
        }

        // Add Alert to the list when finished
        Timestamp now = Timestamp.from(Instant.now());
        ArrayList<Alert> remove = new ArrayList<>();
        for (Alert alert : this.alerts) {
            if (now.compareTo(alert.getEnd()) > 0) {
                alerts.add(alert.clone());
                remove.add(alert);
            }
        }
        // Remove Finished Alerts
        for (Alert alert : remove) {
            this.alerts.remove(alert);
        }
        return alerts;
    }

    public void online() {
        this.live = Math.min(this.live + 1, KEEP_ALIVE);
    }

    public void offline() {
        this.live = Math.max(this.live - 1, 0);
    }

    public boolean live() {
        return this.live != 0;
    }

    public String getName() {
        return this.name;
    }

    public Random getRng() {
        return this.rng;
    }

    @Override
    public String toString() {
        return "WeatherStation(name=" + this.getName() + ")";
    }
}
