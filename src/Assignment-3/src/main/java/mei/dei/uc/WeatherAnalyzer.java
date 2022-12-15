package mei.dei.uc;

import java.time.Duration;
import java.util.Objects;
import java.util.Properties;

import mei.dei.uc.model.Alert;
import mei.dei.uc.model.Measurement;
import mei.dei.uc.serdes.AveragePair;
import mei.dei.uc.serdes.CustomSerdes;
import org.apache.kafka.common.serialization.Deserializer;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.common.serialization.Serializer;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.kstream.*;
import org.json.JSONObject;


public class WeatherAnalyzer {
    private static final String BOOTSTRAP_SERVERS = "localhost:9092,localhost:9093";
    private static final String WEATHER_TOPIC = "Weather";
    private static final String ALERTS_TOPIC = "Alerts";
    private static final String RESULTS_WEATHER_TOPIC = "ResultsWeather";
    private static final String RESULTS_ALERT_TOPIC = "ResultsAlert";
    private static final String RESULTS_LOCATION_TOPIC = "ResultsLocation";


    public static void main(String[] args) {
        Properties properties = new Properties();
        properties.put(StreamsConfig.APPLICATION_ID_CONFIG, "WeatherAnalyzer");
        properties.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, BOOTSTRAP_SERVERS);
        properties.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.StringSerde.class.getName());
        properties.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, Serdes.StringSerde.class.getName());

        // Build Streams
        StreamsBuilder builder = new StreamsBuilder();
        KStream<String, String> weather = builder.stream(WEATHER_TOPIC);
        KStream<String, String> alerts = builder.stream(ALERTS_TOPIC);

        // Setup Metrics (Methods)
        getTemperatureReadingsCountPerStation(weather);
        getTemperatureReadingsCountPerLocation(weather);
        getMinMaxTemperatureReadingPerStation(weather);
        getMinMaxTemperatureReadingPerLocation(weather);
        getNumberOfAlertsPerStation(alerts);
        getNumberOfAlertsPerType(alerts);
        // getMinTemperatureOfStationsWithRedAlert(weather, alerts);
        // getMaxTemperatureOfLocationWithAlertEventsInLastHour(weather, alerts);
        // getMinTemperaturePerStationInRedAlertZones(weather, alerts);
       //  getAverageTemperaturePerStation(weather);
       //  getAverageTemperaturePerStationWithRedAlertsLastHour(weather, alerts);

        // Start Streams
        KafkaStreams streams = new KafkaStreams(builder.build(), properties);
        streams.cleanUp();
        streams.start();
    }

    private static void getTemperatureReadingsCountPerStation(KStream<String, String> weather) {
        weather.mapValues(WeatherAnalyzer::parseMeasurement)
                .map((k, v) -> KeyValue.pair(v.getStation().getName(), 1))
                .groupByKey(Grouped.with(Serdes.String(), Serdes.Integer()))
                .count()
                .mapValues((k, v) -> WeatherAnalyzer.jsonToDB(k, "count", "int64", v.toString()))
                .toStream()
                .to(RESULTS_WEATHER_TOPIC);
    }

    private static void getTemperatureReadingsCountPerLocation(KStream<String, String> weather) {
        weather.mapValues(WeatherAnalyzer::parseMeasurement)
                .map((k, v) -> KeyValue.pair(v.getLocation(), 1))
                .groupByKey(Grouped.with(Serdes.String(), Serdes.Integer()))
                .count()
                .mapValues((k, v) -> WeatherAnalyzer.jsonToDB(k, "count", "int64", v.toString()))
                .toStream()
                .to(RESULTS_LOCATION_TOPIC);
    }

    private static void getMinMaxTemperatureReadingPerStation(KStream<String, String> weather) {
        KGroupedStream<String, Double> weatherStream = weather
                .mapValues(WeatherAnalyzer::parseMeasurement)
                .map((k, v) -> KeyValue.pair(v.getStation().getName(), v.getTemperature()))
                .groupByKey(Grouped.with(Serdes.String(), Serdes.Double()));
        toStreamMinMaxTemperature(weatherStream, RESULTS_WEATHER_TOPIC, true);
    }

    private static void getMinMaxTemperatureReadingPerLocation(KStream<String, String> weather) {
        KGroupedStream<String, Double> locationStream = weather
                .mapValues(WeatherAnalyzer::parseMeasurement)
                .map((k, v) -> KeyValue.pair(v.getLocation(), v.getTemperature()))
                .groupByKey(Grouped.with(Serdes.String(), Serdes.Double()));
        toStreamMinMaxTemperature(locationStream, RESULTS_LOCATION_TOPIC, false);
    }

    private static void toStreamMinMaxTemperature(KGroupedStream<String, Double> gstream, String topic, boolean outputCelsius) {
        gstream.reduce(Math::min)
                .mapValues(outputCelsius ? WeatherAnalyzer::toCelsius : (v) -> v)
                .mapValues((k, v) -> WeatherAnalyzer.jsonToDB(k, "minTemperature", "double", v.toString()))
                .toStream()
                .to(topic);

        gstream.reduce(Math::max)
                .mapValues(outputCelsius ? WeatherAnalyzer::toCelsius : (v) -> v)
                .mapValues((k, v) -> WeatherAnalyzer.jsonToDB(k, "maxTemperature", "double", v.toString()))
                .toStream()
                .to(topic);
    }

    private static void getNumberOfAlertsPerStation(KStream<String, String> alerts) {
        alerts.mapValues(WeatherAnalyzer::parseAlert)
                .map((k, v) -> KeyValue.pair(v.getStation().getName(), 1))
                .groupByKey(Grouped.with(Serdes.String(), Serdes.Integer()))
                .count()
                .mapValues((k, v) -> WeatherAnalyzer.jsonToDB(k, "count", "int64", v.toString()))
                .toStream()
                .to(RESULTS_WEATHER_TOPIC);
    }

    private static void getNumberOfAlertsPerType(KStream<String, String> alerts) {
        alerts.mapValues(WeatherAnalyzer::parseAlert)
                .map((k, v) -> KeyValue.pair(v.getType().toString(), 1))
                .groupByKey(Grouped.with(Serdes.String(), Serdes.Integer()))
                .count()
                .mapValues((k, v) -> WeatherAnalyzer.jsonToDB(k, "count", "int64", v.toString()))
                .toStream()
                .to(RESULTS_ALERT_TOPIC);
    }

    private static void getMinTemperatureOfStationsWithRedAlert(KStream<String, String> weather, KStream<String, String> alerts) {
        KStream<String, Double> weatherStream = weather
                .mapValues(WeatherAnalyzer::parseMeasurement)
                .map((k, v) -> KeyValue.pair(v.getStation().getName(), v.getTemperature()));

        KTable<String, String> alertTypeTable = alerts
                .mapValues(WeatherAnalyzer::parseAlert)
                .map((k, v) -> KeyValue.pair(v.getStation().getName(), v.getType()))
                .filter((k, v) -> v.equals(Alert.SEVERE))
                .toTable();

        weatherStream.join(alertTypeTable, (temperature, alert) -> temperature)
                .groupByKey(Grouped.with(Serdes.String(), Serdes.Double()))
                .reduce(Math::min)
                .mapValues(WeatherAnalyzer::toCelsius)
                .mapValues((k, v) -> WeatherAnalyzer.jsonToDB(k, "minTemperatureRedAlerts", "double", v.toString()))
                .toStream()
                .to(RESULTS_WEATHER_TOPIC);
    }

    private static void getMaxTemperatureOfLocationWithAlertEventsInLastHour(KStream<String, String> weather,
                                                                             KStream<String, String> alerts) {
        KStream<String, Double> weatherStream = weather
                .mapValues(WeatherAnalyzer::parseMeasurement)
                .map((k, v) -> KeyValue.pair(v.getLocation(), v.getTemperature()));

        KTable<String, Long> locationWithEventsTable = alerts
                .mapValues(WeatherAnalyzer::parseAlert)
                .map((k, v) -> KeyValue.pair(v.getLocation(), v.getEvent()))
                .groupByKey(Grouped.with(Serdes.String(), Serdes.String()))
                .windowedBy(TimeWindows.ofSizeWithNoGrace(Duration.ofHours(1)))
                .count()
                .toStream()
                .map((k, v) -> KeyValue.pair(k.key(), v))
                .toTable();

        weatherStream.join(locationWithEventsTable, (temperature, eventCount) -> temperature)
                .groupByKey(Grouped.with(Serdes.String(), Serdes.Double()))
                .reduce(Math::max)
                .mapValues(WeatherAnalyzer::toCelsius)
                .mapValues((k, v) -> WeatherAnalyzer.jsonToDB(k, "maxTemperatureAlertEvents", "double", v.toString()))
                .toStream()
                .to(RESULTS_LOCATION_TOPIC);
    }

    public static void getMinTemperaturePerStationInRedAlertZones(KStream<String, String> weather,
                                                                  KStream<String, String> alerts) {
        KStream<String, KeyValue<String, Double>> weatherStream = weather
                .mapValues(WeatherAnalyzer::parseMeasurement)
                .map((k, v) -> KeyValue.pair(v.getLocation(),
                        KeyValue.pair(v.getStation().getName(), v.getTemperature())));

        KTable<String, String> redAlertsLocation = alerts
                .mapValues(WeatherAnalyzer::parseAlert)
                .map((k, v) -> KeyValue.pair(v.getLocation(), v.getType()))
                .filter((k, v) -> Objects.equals(v, Alert.SEVERE))
                .toTable();

        weatherStream.join(redAlertsLocation, (station, eventType) -> station)
                .map((k, v) -> KeyValue.pair(v.key, v.value))
                .groupByKey(Grouped.with(Serdes.String(), Serdes.Double()))
                .reduce(Math::min)
                .mapValues(WeatherAnalyzer::toCelsius)
                .mapValues((k, v) -> WeatherAnalyzer.jsonToDB(k, "minTemperatureRedAlertZones", "double", v.toString()))
                .toStream()
                .to(RESULTS_WEATHER_TOPIC);
    }

    public static void getAverageTemperaturePerStation(KStream<String, String> weather) {
        weather.mapValues(WeatherAnalyzer::parseMeasurement)
                .map((k, v) -> KeyValue.pair(v.getStation().getName(), AveragePair.from(v.getTemperature(), 1)))
                .groupByKey(Grouped.with(Serdes.String(), CustomSerdes.AveragePairSerde()))
                .reduce((acc, x) -> AveragePair.from(acc.key + x.key, acc.value + x.value))
                .mapValues(v -> v.key / v.value)
                .mapValues((k, v) -> WeatherAnalyzer.jsonToDB(k, "avgTemperature", "double", v.toString()))
                .toStream()
                .to(RESULTS_WEATHER_TOPIC);
    }

    public static void getAverageTemperaturePerStationWithRedAlertsLastHour(KStream<String, String> weather,
                                                                            KStream<String, String> alerts) {
        KStream<String, Double> weatherStream = weather
                .mapValues(WeatherAnalyzer::parseMeasurement)
                .map((k, v) -> KeyValue.pair(v.getStation().getName(), v.getTemperature()));

        KTable<String, Long> stationWithRedAlertsLastHour = alerts
                .mapValues(WeatherAnalyzer::parseAlert)
                .map((k, v) -> KeyValue.pair(v.getStation().getName(), v.getType()))
                .filter((k, v) -> v.equals(Alert.SEVERE))
                .groupByKey()
                .windowedBy(TimeWindows.ofSizeWithNoGrace(Duration.ofHours(1)))
                .count()
                .toStream()
                .map((k, v) -> KeyValue.pair(k.key(), v))
                .toTable();

        weatherStream.join(stationWithRedAlertsLastHour, (temperature, alertCount) -> temperature)
                .map((k, v) -> KeyValue.pair(k, AveragePair.from(v, 1)))
                .groupByKey(Grouped.with(Serdes.String(), CustomSerdes.AveragePairSerde()))
                .reduce((acc, x) -> AveragePair.from(acc.key + x.key, acc.value + x.value))
                .mapValues(v -> v.key / v.value)
                .mapValues((k, v) -> WeatherAnalyzer.jsonToDB(k, "avgTemperatureRedAlerts", "double", v.toString()))
                .toStream()
                .to(RESULTS_WEATHER_TOPIC);

    }


    private static String jsonToDB(String id, String field, String type, String value) {
        return "{\"schema\":{\"type\":\"struct\",\"fields\":" +
                "[" +
                "{\"type\":\"string\",\"optional\":false,\"field\":\"id\"}," +
                "{\"type\":\"" + type + "\",\"optional\":true,\"field\":\"" + field + "\"}" +
                "]," +
                "\"optional\":false}," +
                "\"payload\":{\"id\":\"" + id + "\",\"" + field + "\":" + value + "}}";
    }

    private static Double toCelsius(double temperature) {
        return temperature * (9 / 5) + 32;
    }

    private static Measurement parseMeasurement(String json) {
        JSONObject obj = new JSONObject(json);
        return Measurement.fromJson(obj);
    }

    private static Alert parseAlert(String json) {
        JSONObject obj = new JSONObject(json);
        return Alert.fromJson(obj);
    }
}