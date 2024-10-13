package EcommerceFlink;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.math3.stat.descriptive.summary.Product;
import org.apache.flink.api.common.serialization.DeserializationSchema;
import org.apache.flink.api.common.typeinfo.TypeInformation;
import org.apache.kafka.common.serialization.StringDeserializer;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class CustomDeserializationSchema implements DeserializationSchema<Product> {

    @Override
    public Product deserialize(byte[] message) throws IOException {
        String jsonString = new String(message, StandardCharsets.UTF_8);
        return parseJsonToProduct(jsonString);
    }

    @Override
    public boolean isEndOfStream(Product nextElement) {
        return false;
    }

    @Override
    public TypeInformation<Product> getProducedType() {
        return TypeInformation.of(Product.class);
    }

    private Product parseJsonToProduct(String jsonString) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.readValue(jsonString, Product.class);
        } catch (IOException e) {
            throw new RuntimeException("Failed to deserialize JSON to Product", e);
        }
    }
}
