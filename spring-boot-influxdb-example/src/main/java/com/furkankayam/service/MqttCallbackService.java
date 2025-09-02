package com.furkankayam.service;

import com.furkankayam.config.MqttConfig;
import com.furkankayam.dto.DataObjectDto;
import com.furkankayam.repository.influx.MqttDeviceInfluxRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.client.mqttv3.*;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class MqttCallbackService {

    private String deviceId;
    private Double temperature;

    private final MqttConfig mqttConfig;
    private final IMqttClient mqttClient;
    private final MqttDeviceInfluxRepository mqttDeviceInfluxRepository;

    @PostConstruct
    public void init() throws Exception {
        mqttClient.setCallback(mqttCallback);
        mqttClient.subscribe(mqttConfig.getSubscribeTopic());
        log.info("Subscribed to topic: {}", mqttConfig.getSubscribeTopic());
    }

    private final MqttCallback mqttCallback = new MqttCallback() {
        @Override
        public void connectionLost(Throwable cause) {}

        @Override
        public void messageArrived(String topic, MqttMessage message) throws Exception {
            String payload = new String(message.getPayload());

            JSONObject jsonMessage = new JSONObject(payload);

            deviceId = jsonMessage.getString("deviceId");
            temperature = jsonMessage.getDouble("temperature");

            DataObjectDto dataObjectDto = DataObjectDto.builder()
                    .deviceId(deviceId)
                    .temperature(temperature)
                    .build();

            mqttDeviceInfluxRepository.saveEvent(dataObjectDto);
        }

        @Override
        public void deliveryComplete(IMqttDeliveryToken token) {}
    };
}