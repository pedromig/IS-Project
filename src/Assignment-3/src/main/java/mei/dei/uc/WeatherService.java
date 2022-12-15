package mei.dei.uc;

import java.time.Duration;
import java.util.*;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadLocalRandom;

import mei.dei.uc.model.Alert;
import mei.dei.uc.model.Measurement;
import mei.dei.uc.model.WeatherStation;
import org.apache.kafka.clients.consumer.*;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerConfig;

import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.json.JSONObject;

@SuppressWarnings("BusyWait")
public class WeatherService {
    private static final String BOOTSTRAP_SERVERS = "localhost:9092,localhost:9093";
    private static final String DB_INFO_TOPIC_STATIONS = "DBInfoStations";
    private static final String DB_INFO_TOPIC_LOCATIONS = "DBInfoLocations";
    private static final String WEATHER_TOPIC = "Weather";
    private static final String ALERTS_TOPIC = "Alerts";
    private static final Integer EVENT_RATE = 10;
    private static final Integer SLEEP_TIMEOUT = 5000;

    public static void main(String[] args) throws InterruptedException {
        HashMap<String, WeatherStation> stations = new HashMap<>();
        HashSet<String> locations;

        try (Consumer<String, String> locationConsumer = WeatherService.createConsumer(DB_INFO_TOPIC_LOCATIONS);
             Consumer<String, String> stationConsumer = WeatherService.createConsumer(DB_INFO_TOPIC_STATIONS);
             Producer<String, String> alertProducer = WeatherService.createProducer();
             Producer<String, String> weatherProducer = WeatherService.createProducer()) {

            while (!Thread.interrupted()) {

                HashSet<String> liveStationRecords = new HashSet<>();
                HashSet<String> liveLocationRecords = new HashSet<>();

                // Listen for Weather Stations
                ConsumerRecords<String, String> stationRecords = stationConsumer.poll(Duration.ofMillis(5000));
                for (ConsumerRecord<String, String> record : stationRecords) {
                    JSONObject json = new JSONObject(record.value());
                    String station = json.getJSONObject("payload").getString("name");
                    liveStationRecords.add(station);
                    if (stations.get(station) == null) {
                        stations.put(station, new WeatherStation(station));
                    }
                }

                // Listen for Locations
                ConsumerRecords<String, String> locationRecords = locationConsumer.poll(Duration.ofMillis(5000));
                for (ConsumerRecord<String, String> record : locationRecords) {
                    JSONObject json = new JSONObject(record.value());
                    String location = json.getJSONObject("payload").getString("name");
                    liveLocationRecords.add(location);
                }

                // Update Live Locations & Stations
                locations = liveLocationRecords;
                for (WeatherStation station : stations.values()) {
                    if (liveStationRecords.contains(station.getName())) {
                        station.online();
                    } else {
                        station.offline();
                    }
                }

                ArrayList<String> remove = new ArrayList<>();
                for (WeatherStation station : stations.values()) {
                    if (!station.live()) {
                        remove.add(station.getName());
                    }
                }

                for (String station : remove) {
                    stations.remove(station);
                }

                // Generate Events
                ArrayList<String> currentLocations = new ArrayList<>(locations.stream().toList());
                for (WeatherStation station : stations.values()) {
                    for (int j = 0; j < WeatherService.EVENT_RATE; ++j) {
                        if (currentLocations.size() == 0)
                            break;
                        // Pick a Random Location
                        int l = ThreadLocalRandom.current().nextInt(0, currentLocations.size() - 1);

                        // Generate Measurements
                        Measurement measurement = station.getMeasurement(currentLocations.get(l));
                        weatherProducer.send(new ProducerRecord<>(WEATHER_TOPIC,
                                "measurement", measurement.toJson().toString()));

                        // Emmit Alerts
                        for (Alert alert : station.alerts()) {
                            alertProducer.send(new ProducerRecord<>(ALERTS_TOPIC,
                                    "alert", alert.toJson().toString()));
                        }
                    }
                }
                Thread.sleep(SLEEP_TIMEOUT);
            }
        }
    }

    public static <K, V> Consumer<K, V> createConsumer(String topic) {
        // Setup Instance Properties
        Properties properties = new Properties();
        properties.put(ConsumerConfig.GROUP_ID_CONFIG, "WeatherServiceConsumer-" + topic);
        properties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, WeatherService.BOOTSTRAP_SERVERS);
        properties.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, true);
        properties.put(ConsumerConfig.ALLOW_AUTO_CREATE_TOPICS_CONFIG, true);
        properties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        properties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);

        // Create Consumer Instance and Subscribe to topic
        Consumer<K, V> consumer = new KafkaConsumer<>(properties);
        consumer.subscribe(Collections.singletonList(topic));
        return consumer;
    }

    private static <K, V> Producer<K, V> createProducer() {
        Properties properties = new Properties();
        properties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, WeatherService.BOOTSTRAP_SERVERS);
        properties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        properties.put(ProducerConfig.BUFFER_MEMORY_CONFIG, 33554432);
        properties.put(ProducerConfig.RETRIES_CONFIG, 0);
        properties.put(ProducerConfig.ACKS_CONFIG, "all");
        properties.put(ProducerConfig.LINGER_MS_CONFIG, 1);
        properties.put(ProducerConfig.BATCH_SIZE_CONFIG, 16384);

        return new KafkaProducer<>(properties);
    }

}

