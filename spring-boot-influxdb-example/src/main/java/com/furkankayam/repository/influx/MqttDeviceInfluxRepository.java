package com.furkankayam.repository.influx;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.furkankayam.dto.DataObjectDto;
import com.furkankayam.config.InfluxDBConfig;
import com.influxdb.client.InfluxDBClient;
import com.influxdb.client.WriteApiBlocking;
import com.influxdb.client.domain.WritePrecision;
import com.influxdb.client.write.Point;
import com.influxdb.exceptions.InfluxException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
@Slf4j
@Retryable(
        retryFor = {InfluxException.class},
        maxAttempts = 6,
        backoff = @Backoff(delay = 10000))
public class MqttDeviceInfluxRepository extends DeviceInfluxRepository {

    private static final String DEVICE_ID_TAG = "device_id";

    protected final InfluxDBConfig config;
    protected final ObjectMapper objectMapper;
    protected final InfluxDBClient influxDBClient;

    public MqttDeviceInfluxRepository(InfluxDBConfig config,
                                      ObjectMapper objectMapper,
                                      InfluxDBClient influxDBClient) {
        super(config, objectMapper, influxDBClient);
        this.config = config;
        this.objectMapper = objectMapper;
        this.influxDBClient = influxDBClient;
    }

    @Override
    public void saveEvent(DataObjectDto dataObjectDto) {

        WriteApiBlocking writeApi = influxDBClient.getWriteApiBlocking();
        Point point =
                Point.measurement("device_temperature_data")
                        .addTag(
                                MqttDeviceInfluxRepository.DEVICE_ID_TAG,
                                dataObjectDto.getDeviceId());

        point.addField("temperature", dataObjectDto.getTemperature());

        point.time(Instant.now(), WritePrecision.MS);
        writeApi.writePoint(point);
    }
}
