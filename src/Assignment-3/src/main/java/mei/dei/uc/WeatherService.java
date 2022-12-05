package mei.dei.uc;

import java.io.FileInputStream;
import java.io.IOException;
import java.time.Duration;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

import mei.dei.uc.model.Measurement;
import mei.dei.uc.model.WeatherStation;
import org.apache.kafka.clients.consumer.*;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;

import mei.dei.uc.model.Alert;
import org.json.JSONObject;

public class WeatherService {
    private static final String DB_INFO_TOPIC = "DBInfo";

    public static void main(String[] args) {
        Properties consumerProperties = WeatherService.getBrokerProperties();
        consumerProperties.put(ConsumerConfig.GROUP_ID_CONFIG, "WeatherServiceConsumer");

        try (Consumer<String, String> dbinfo = new KafkaConsumer<>(consumerProperties)) {
            dbinfo.subscribe(Collections.singletonList(DB_INFO_TOPIC));

            Properties producerProperties = WeatherService.getBrokerProperties();
            HashMap<String, Thread> weatherStations = new HashMap<>();

            while (true) {
                HashMap<String, WeatherStation> active = new HashMap<>();

                // Listen for new Weather Stations
                ConsumerRecords<String, String> records = dbinfo.poll(Duration.ofSeconds(50000));
                for (ConsumerRecord<String, String> record : records) {
                    JSONObject json = new JSONObject(record.value());
                    String name = json.getJSONObject("payload").getString("name");
                    String location = json.getJSONObject("payload").getString("location");
                    if (!active.containsKey(name)) {
                        WeatherStation ws = new WeatherStation(name, location);
                        active.put(name, ws);
                    }
                }

                // Log the active stations
                System.out.println("=> Active Stations (Threads): " + active.size());
                for (WeatherStation station : active.values()) {
                    System.out.println(" -> " + station);
                }

                // Add newly inserted weather stations
                for (WeatherStation station : active.values()) {
                    if (!weatherStations.containsKey(station.getName())) {
                        Thread t = new Thread(new WeatherStationService(station, producerProperties));
                        weatherStations.put(station.getName(), t);
                        t.start();
                    }
                }

                // Remove inactive weather stations
                ArrayList<String> inactive = new ArrayList<>();
                for (String station : weatherStations.keySet()) {
                    if (!active.containsKey(station)) {
                        inactive.add(station);
                        active.remove(station);
                    }
                }
                for (String name : inactive) {
                    Thread t = weatherStations.remove(name);
                    t.interrupt();
                }
            }
        }
    }

    private static class WeatherStationService implements Runnable {

        private static final String WEATHER_TOPIC = "Weather";
        private static final String ALERTS_TOPIC = "Alerts";

        private final WeatherStation station;
        private final Properties properties;

        public WeatherStationService(WeatherStation station, Properties properties) {
            this.station = station;
            this.properties = (Properties) properties.clone();
            this.properties.put(ProducerConfig.CLIENT_ID_CONFIG, "WeatherService-" + this.station.getName());
        }

        public void run() {
            Producer<String, String> producer = new KafkaProducer<>(properties);
            try {
                while (!Thread.interrupted()) {
                    // Generate Random Measurements and Alerts
                    int n = ThreadLocalRandom.current().nextInt(0, 10);
                    for (int i = 0; i < n; ++i) {
                        // Fetch Measurements
                        Measurement measurement = station.getMeasurement();
                        producer.send(new ProducerRecord<>(WEATHER_TOPIC,
                                "measurement", measurement.toJson().toString()));

                        // Fetch Alerts
                        for (Alert alert : station.alerts()) {
                            producer.send(new ProducerRecord<>(ALERTS_TOPIC,
                                    "alert", alert.toJson().toString()));
                        }
                    }
                    Thread.sleep(10000);
                }
            } catch (InterruptedException | CloneNotSupportedException e) {
                System.out.println("!! Stopping Weather Station: " + this.station);
                producer.close();
            }
        }
    }

    private static Properties getBrokerProperties() {
        Properties properties = new Properties();
        try {
            properties.load(new FileInputStream("resources/broker.properties"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return properties;
    }
}