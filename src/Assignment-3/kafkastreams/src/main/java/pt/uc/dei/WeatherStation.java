package pt.uc.dei;

import java.util.Properties;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import java.util.concurrent.TimeUnit;
import java.util.Random;
import org.json.JSONObject;

public class WeatherStation {
    public static void main(String[] args) throws Exception{

        String StdWeatherTopic = "StandardWeather";
        String AlertsTopic = "WeatherAlerts";

        String stationId = "Station1";  // pass as arg
        
        Random rnd = new Random(42);
        Boolean alertOn = false;
        int changeOfAlert = 25;       // %
        int alertDuration = 20;       // number of ticks
        int countDuration = 0;

        // create instance for properties to access producer configs
        Properties props = new Properties(); //Assign localhost id
        props.put("bootstrap.servers", "broker1:9092");
        //Set acknowledgements for producer requests. props.put("acks", "all");
        //If the request fails, the producer can automatically retry,
        props.put("retries", 0);
        //Specify buffer size in config
        props.put("batch.size", 16384);
        //Reduce the no of requests less than 0
        props.put("linger.ms", 1);
        //The buffer.memory controls the total amount of memory available to the producer for buffering.
        props.put("buffer.memory", 33554432);
        props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        
        Producer<String, String> producer = new KafkaProducer<>(props);
        try {
            while(true) {   
                Long temp = randomMeasurement(rnd);
                String message = new JSONObject()
                                            .put("StationId", stationId)
                                            .put("Location", "Lisbon")
                                            .put("Measurement", Long.toString(temp))
                                            .toString();

                producer.send(new ProducerRecord<String, String>(StdWeatherTopic, "Standard Weather", message));

                //System.out.println("Sent: " + message);

                if(alertOn || (!alertOn && rnd.nextInt(100) < changeOfAlert)) {
                    alertOn = true;
                    String alertMessage = new JSONObject()
                                            .put("StationId", stationId)
                                            .put("Location", "Lisbon")
                                            .put("Type", (countDuration != alertDuration - 1) ? "Red" : "Green")
                                            .toString();
                    // System.out.println("ALERT: " + alertMessage + countDuration);
                    producer.send(new ProducerRecord<String, String>(AlertsTopic, "Alert Weather", alertMessage));
                    countDuration += 1;
                    if (countDuration >= alertDuration) {
                        alertOn = false;
                        countDuration = 0;
                        // System.out.println("Reseted");
                    }
                }


                TimeUnit.SECONDS.sleep(1);
            }
        } 
        finally {
            System.out.println("Closing");
            producer.close();
        }
    }

    private static Long randomMeasurement(Random rnd) {   // allows temperatures between 0 to 200º Fahrenheit (or -20 to 90º Celsius) with normal distribution
        return (long) (rnd.nextGaussian()*35+70);
    }
}