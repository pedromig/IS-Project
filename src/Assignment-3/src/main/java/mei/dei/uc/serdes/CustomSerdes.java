package mei.dei.uc.serdes;

import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serdes;

public final class CustomSerdes {

    public static Serde<AveragePair> AveragePairSerde() {
        JSONSerializer<AveragePair> serializer = new JSONSerializer<>();
        JSONDeserializer<AveragePair> deserializer = new JSONDeserializer<>(AveragePair.class);
        return Serdes.serdeFrom(serializer, deserializer);
    }
}