package com.most.core.pub.message.kafka;

import com.most.core.pub.tools.file.PropertiesTool;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.config.KafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.listener.ConcurrentMessageListenerContainer;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * Created by awx on 2018/7/4/004.
 */
@Configuration
@EnableKafka
public class KafkaConsumerConfig {

    public KafkaConsumerConfig(){

    }

    @Bean
    KafkaListenerContainerFactory<ConcurrentMessageListenerContainer<Integer, String>>
    kafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<Integer, String> factory =
                new ConcurrentKafkaListenerContainerFactory();
        factory.setConsumerFactory(consumerFactory());
        factory.setConcurrency(3);
        factory.getContainerProperties().setPollTimeout(3000);
        return factory;
    }

    @Bean
    public ConsumerFactory<Integer, String> consumerFactory() {
        return new DefaultKafkaConsumerFactory(consumerProperties());
    }

    @Bean
    public Map<String, Object> consumerProperties() {
        Properties properties = PropertiesTool.getProperties("kafka.properties");
        Map<String, Object> props= new HashMap<String, Object>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, properties.getProperty("kafka.consumer.bootstrap.servers"));
        props.put(ConsumerConfig.GROUP_ID_CONFIG,  properties.getProperty("kafka.consumer.group.id"));
        props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG,  properties.getProperty("kafka.consumer.enable.auto.commit"));
        props.put(ConsumerConfig.AUTO_COMMIT_INTERVAL_MS_CONFIG, properties.getProperty("kafka.consumer.auto.commit.interval.ms"));
        props.put(ConsumerConfig.SESSION_TIMEOUT_MS_CONFIG,  properties.getProperty("kafka.consumer.session.timeout.ms"));
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG,  properties.getProperty("kafka.consumer.key.deserializer"));
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG,  properties.getProperty("kafka.consumer.value.deserializer"));
        return props;
    }

    @Bean
    public KafkaConsumerListener kafkaConsumerListener(){
        return new KafkaConsumerListener();
    }

}