# salesforce-rest-api-client

how transactional works 

```java
import org.apache.kafka.clients.producer.*;

import java.util.Properties;

public class TransactionalProducerExample {

    public static void main(String[] args) {
        // Configure the producer
        Properties properties = new Properties();
        properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        properties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer");
        properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer");
        properties.put(ProducerConfig.TRANSACTIONAL_ID_CONFIG, "my-transactional-id");

        // Create the KafkaProducer
        KafkaProducer<String, String> producer = new KafkaProducer<>(properties);

        // Initialize the transaction
        producer.initTransactions();

        try {
            // Begin the transaction
            producer.beginTransaction();

            // Produce messages
            for (int i = 0; i < 10; i++) {
                ProducerRecord<String, String> record = new ProducerRecord<>("my-topic", "key" + i, "value" + i);
                producer.send(record);
            }

            // Commit the transaction
            producer.commitTransaction();
            System.out.println("Transaction committed successfully.");
        } catch (ProducerFencedException | OutOfOrderSequenceException | AuthorizationException e) {
            // Handle exceptions
            producer.close();
        } catch (KafkaException e) {
            // Abort the transaction
            producer.abortTransaction();
            System.out.println("Transaction aborted.");
        } finally {
            // Close the producer
            producer.close();
        }
    }
}

```
