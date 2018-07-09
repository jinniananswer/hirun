package com.most.core.pub.message.kafka;

import com.alibaba.fastjson.JSONObject;
import com.most.core.pub.SpringContextUtil;
import com.most.core.pub.data.GenericEntity;
import org.springframework.kafka.core.KafkaTemplate;

/**
 * Created by awx on 2018/7/5/005.
 */
public class KafkaProducter {

    public static void sendMessage(String topic, JSONObject jsonObject) {
        KafkaTemplate<Integer, String> kafkaTemplate = (KafkaTemplate<Integer, String>) SpringContextUtil.getBean("kafkaTemplate");
        kafkaTemplate.send(topic, jsonObject.toJSONString());
    }

    public static void sendMessage(String topic, GenericEntity entity) {
        KafkaTemplate<Integer, String> kafkaTemplate = (KafkaTemplate<Integer, String>) SpringContextUtil.getBean("kafkaTemplate");
        kafkaTemplate.send(topic, entity.toJson().toJSONString());
    }
}
