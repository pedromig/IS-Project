package mei.dei.uc;

import java.util.Properties;

import mei.dei.uc.model.Alert;
import mei.dei.uc.model.Measurement;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.kstream.Grouped;
import org.apache.kafka.streams.kstream.KStream;
import org.json.JSONObject;

public class WeatherAnalyzer {

    private static final String WEATHER_TOPIC = "Weather";
    private static final String ALERTS_TOPIC = "Alerts";

    private static final String RESULTS_WEATHER_TOPIC = "ResultsWeather";
    private static final String RESULTS_ALERT_TOPIC = "ResultsAlert";
    private static final String RESULTS_LOCATION_TOPIC = "ResultsLocation";

    public static void main(String[] args) {
        Properties properties = new Properties();
        properties.put(StreamsConfig.APPLICATION_ID_CONFIG, "WeatherAnalyzer");
        properties.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        properties.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.StringSerde.class.getName());
        properties.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, Serdes.StringSerde.class.getName());

        // Build Streams
        StreamsBuilder builder = new StreamsBuilder();
        KStream<String, String> weather = builder.stream(WEATHER_TOPIC);
        KStream<String, String> alerts = builder.stream(ALERTS_TOPIC);

        // Setup Metrics (Methods)
        getTemperatureReadingsCountPerStation(weather);
        // getTemperatureReadingsCountPerLocation(weather);

        // Start Streams
        KafkaStreams streams = new KafkaStreams(builder.build(), properties);
        streams.cleanUp();
        streams.start();
    }

    private static void getTemperatureReadingsCountPerStation(KStream<String, String> weather) {
        weather.mapValues((k, v) -> WeatherAnalyzer.parseMeasurement(v))
                .map((k, v) -> KeyValue.pair(v.getStation().getName(), 1))
                .groupByKey(Grouped.with(Serdes.String(), Serdes.Integer()))
                .reduce(Integer::sum)
                .mapValues((k, v) -> WeatherAnalyzer.jsonToDB(k, "count", "int64", v.toString()))
                .toStream()
                .to(RESULTS_WEATHER_TOPIC);
    }

    private static void getTemperatureReadingsCountPerLocation(KStream<String, String> weather) {
        weather.mapValues((k, v) -> WeatherAnalyzer.parseMeasurement(v))
                .map((k, v) -> KeyValue.pair(v.getStation().getLocation(), 1))
                .groupByKey(Grouped.with(Serdes.String(), Serdes.Integer()))
                .reduce(Integer::sum)
                .mapValues((k, v) -> WeatherAnalyzer.jsonToDB(k, "count", "int64", v.toString()))
                .toStream()
                .to(RESULTS_LOCATION_TOPIC);
    }


    private static <V> String jsonToDB(String id, String field, String type, String value) {
        return "{\"schema\":{\"type\":\"struct\",\"fields\":" +
                "[" +
                "{\"type\":\"string\",\"optional\":false,\"field\":\"id\"}," +
                "{\"type\":\"" + type + "\",\"optional\":false,\"field\":\"" + field + "\"}" +
                "]," +
                "\"optional\":false}," +
                "\"payload\":{\"id\":\"" + id + "\",\"" + field + "\":" + value + "}}";
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