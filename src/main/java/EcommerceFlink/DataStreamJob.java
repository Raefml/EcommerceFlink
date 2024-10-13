package EcommerceFlink;

import com.fasterxml.jackson.databind.ObjectMapper;
import dto.Product;
import org.apache.flink.api.common.eventtime.WatermarkStrategy;
import org.apache.flink.api.common.typeinfo.TypeInformation;
import org.apache.flink.api.java.typeutils.TypeExtractor;
import org.apache.flink.connector.elasticsearch.sink.Elasticsearch7SinkBuilder;
import org.apache.flink.connector.kafka.source.KafkaSource;
import org.apache.flink.connector.kafka.source.enumerator.initializer.OffsetsInitializer;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.util.serialization.DeserializationSchema;
import org.apache.flink.elasticsearch7.shaded.org.apache.http.HttpHost;
import org.apache.flink.elasticsearch7.shaded.org.elasticsearch.action.index.IndexRequest;
import org.apache.flink.elasticsearch7.shaded.org.elasticsearch.client.Requests;
import org.apache.flink.elasticsearch7.shaded.org.elasticsearch.common.xcontent.XContentType;

import java.io.IOException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DataStreamJob {

    private static final ObjectMapper objectMapper = new ObjectMapper();
    private static final Logger logger = LoggerFactory.getLogger(DataStreamJob.class);

    public static void main(String[] args) throws Exception {
        final StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        String topic = "myteck_products";

        KafkaSource<Product> source = KafkaSource.<Product>builder()
                .setBootstrapServers("localhost:29092")
                .setTopics(topic)
                .setGroupId("flink-group-raef")
                .setStartingOffsets(OffsetsInitializer.earliest())
                .setValueOnlyDeserializer(new CustomDeserializationSchema())
                .build();

        DataStream<Product> productStream = env.fromSource(source, WatermarkStrategy.noWatermarks(), "Kafka source");

        productStream.map(product -> {
            String json = convertProductToJson(product);
            logger.info("Consumed product: {}", json);
            System.out.println("Consumed product: " + json);
            return product;
        }).print();

        productStream.sinkTo(
                new Elasticsearch7SinkBuilder<Product>()
                        .setHosts(new HttpHost("localhost", 9200, "http"))
                        .setEmitter((product, runtimeContext, requestIndexer) -> {
                            if (product != null) {
                                String json = convertProductToJson(product);
                                IndexRequest indexRequest = Requests.indexRequest()
                                        .index("products_v3")
                                        .id(product.getId())
                                        .source(json, XContentType.JSON);
                                logger.info("Request to be indexed: {}", indexRequest);
                                requestIndexer.add(indexRequest);
                                logger.info("Persisting product to Elasticsearch: {}", json);
                                System.out.println("Persisting product to Elasticsearch: " + json);
                            } else {
                                System.out.println("Skipping product with invalid data: " + product);
                                logger.warn("Skipping product with invalid data: {}", product);
                            }
                        })
                        .build()
        ).name("Elasticsearch Sink");

        env.execute("Flink Kafka to Elasticsearch");
    }

    private static String convertProductToJson(Product product) {
        try {
            return objectMapper.writeValueAsString(product);
        } catch (IOException e) {
            System.err.println("Error converting product to JSON: " + e.getMessage());
            logger.error("Error converting product to JSON: {}", e.getMessage());
            return "{error converting product to JSON}";
        }
    }

    public static class CustomDeserializationSchema implements DeserializationSchema<Product> {
        @Override
        public Product deserialize(byte[] message) {
            try {
                Product product = objectMapper.readValue(message, Product.class);
                System.out.println("Deserialized Product: " + product);
                logger.info("Deserialized Product: {}", product);
                return product;
            } catch (IOException e) {
                System.err.println("Deserialization error: " + e.getMessage());
                logger.error("Deserialization error: {}", e.getMessage());
                return null;
            }
        }

        @Override
        public boolean isEndOfStream(Product nextElement) {
            return false;
        }

        @Override
        public TypeInformation<Product> getProducedType() {
            return TypeExtractor.getForClass(Product.class);
        }
    }
}
