package com.most.core.pub.message.kafka;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;

/**
 * Created by awx on 2018/7/4/004.
 * this is a example
 */
public class KafkaConsumerListener {
    /**
     * @param data
     */
//    @KafkaListener(groupId="test" ,topics = "test")
//    void listener(ConsumerRecord<String, String> data){
//        System.out.println("消费者线程："+Thread.currentThread().getName()+"[ 消息 来自kafkatopic："+data.topic()+",分区："+data.partition()
//                +" ，委托时间："+data.timestamp()+"]消息内容如下：");
//        System.out.println(data.value());
//    }
}