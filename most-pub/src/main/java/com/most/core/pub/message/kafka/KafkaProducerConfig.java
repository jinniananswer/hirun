package com.most.core.pub.message.kafka;

import com.most.core.pub.tools.file.PropertiesTool;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * Created by awx on 2018/7/4/004.
 */
@Configuration
@EnableKafka
public class KafkaProducerConfig {

    public KafkaProducerConfig(){

    }

    @Bean
    public ProducerFactory<Integer, String> producerFactory() {
        return new DefaultKafkaProducerFactory(producerProperties());
    }

    @Bean
    public Map<String, Object> producerProperties() {
        Properties properties = PropertiesTool.getProperties("kafka.properties");
        Map<String, Object> props = new HashMap<String, Object>();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, properties.getProperty("kafka.producer.bootstrap.servers"));
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, properties.getProperty("kafka.producer.key.serializer"));
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG,properties.getProperty("kafka.producer.value.serializer"));
        props.put(ProducerConfig.RETRIES_CONFIG,properties.getProperty("kafka.producer.retries"));
        props.put(ProducerConfig.BATCH_SIZE_CONFIG,properties.getProperty("kafka.producer.batch.size","1048576"));
        props.put(ProducerConfig.LINGER_MS_CONFIG,properties.getProperty("kafka.producer.linger.ms"));
        props.put(ProducerConfig.BUFFER_MEMORY_CONFIG,properties.getProperty("kafka.producer.buffer.memory","33554432"));
        props.put(ProducerConfig.ACKS_CONFIG,properties.getProperty("kafka.producer.acks","all"));
        return props;
    }

    @Bean
    public KafkaTemplate<Integer, String> kafkaTemplate() {
        KafkaTemplate kafkaTemplate = new KafkaTemplate<Integer, String>(producerFactory(),true);
        kafkaTemplate.setDefaultTopic("test");
        return kafkaTemplate;
    }

}