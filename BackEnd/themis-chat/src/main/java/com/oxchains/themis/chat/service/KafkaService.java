package com.oxchains.themis.chat.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

/**
 * create by huohuo
 * @author huohuo
 */
@Service
public class KafkaService {
    @Value("${kafka.topic}")
    private String topic;
    private final Logger LOG = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private KafkaTemplate<String,String> kafkaTemplate;
    public void send(String message) {
        try {
            kafkaTemplate.send(topic,message);
        }catch (Exception e){
            LOG.error("faild to send message : {}",e);
        }
    }
}
