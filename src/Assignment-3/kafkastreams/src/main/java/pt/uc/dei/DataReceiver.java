package pt.uc.dei;

import java.time.Duration;
import java.util.Collections;
import java.util.Properties;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.json.JSONException;
import org.json.JSONObject;

public class DataReceiver {
    public static void main(String[] args) throws Exception{
        //Assign topicName to string variable
        String StdWeatherTopic = "StandardWeather";
        String AlertsTopic = "WeatherAlerts";


        // create instance for properties to access producer configs
        Properties props = new Properties();
        //Assign localhost id
        props.put("bootstrap.servers", "broker1:9092"); //Set acknowledgements for producer requests. props.put("acks", "all");
        //If the request fails, the producer can automatically retry,
        props.put("retries", 0);
        //Specify buffer size in config
        props.put("batch.size", 16384);
        //Reduce the no of requests less than 0
        props.put("linger.ms", 1);
        //The buffer.memory controls the total amount of memory available to the producer for buffering.
        props.put("buffer.memory", 33554432);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, "KafkaExampleConsumer");
        props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        props.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");

        Runnable r1 = new TopicListener(StdWeatherTopic, props);
        Runnable r2 = new TopicListener(AlertsTopic, props);
        new Thread(r1).start();
        new Thread(r2).start();

    } 


    private static class TopicListener implements Runnable {
        private String topicName;
        private Properties props;

        public TopicListener(String topicName, Properties props) {
            this.topicName = topicName;
            this.props = props;
        }

        @Override 
        public void run() { 
            System.out.println(topicName + " Listener Running");

            Consumer<String, String> consumer = new KafkaConsumer<>(props); consumer.subscribe(Collections.singletonList(topicName));     // or "AlertsTopic" -> create thread to run both (TO BE IMPLEMENTED)       
            try {
                while (true) {
                    Duration d = Duration.ofSeconds(1000000);
                    ConsumerRecords<String, String> records = consumer.poll(d);
                    for (ConsumerRecord<String, String> record : records) {
                        try {
                            JSONObject jsonObject = new JSONObject(record.value());
                            System.out.println(record.key() + " => " + jsonObject.toString()); 

                        }catch (JSONException err){
                            System.out.println(err);
                            break;
                        }
                    }
                }    
            } finally {
                consumer.close();
            }
        }
    }

}