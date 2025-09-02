package com.furkankayam.repository.influx;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.furkankayam.dto.DataObjectDto;
import com.furkankayam.config.InfluxDBConfig;
import com.influxdb.client.InfluxDBClient;
import com.influxdb.client.QueryApi;
import com.influxdb.exceptions.InfluxException;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;

import java.util.List;
import java.util.Map;

@Slf4j
@Retryable(
        retryFor = {InfluxException.class},
        maxAttempts = 6,
        backoff = @Backoff(delay = 10000))
public abstract class DeviceInfluxRepository {

    private final InfluxDBConfig config;
    private final ObjectMapper objectMapper;
    private final InfluxDBClient influxDBClient;

    protected DeviceInfluxRepository(InfluxDBConfig config,
                                     ObjectMapper objectMapper,
                                     InfluxDBClient influxDBClient) {
        this.config = config;
        this.objectMapper = objectMapper;
        this.influxDBClient = influxDBClient;
    }

    public abstract void saveEvent(DataObjectDto dataObjectDto);

    @SneakyThrows
    public String getTemperatureData(String deviceId, String from, String to) {
        QueryApi readApi = influxDBClient.getQueryApi();
        String query = """
        from(bucket: "%s")
          |> range(start: %s, stop: %s)
          |> filter(fn: (r) => r["_measurement"] == "device_temperature_data")
          |> filter(fn: (r) => r["_field"] == "temperature")
          |> filter(fn: (r) => r["device_id"] == "%s")
          |> yield(name: "mean")
        """.formatted(
                config.getInfluxBucket(),
                from,
                to,
                deviceId
        );

        log.debug("INFLUX | Query: {}", query);

        var res = readApi.query(query);
        log.debug("INFLUX | Temperature data of device {}: {}", deviceId, res);

        List<Map<String, Object>> simplifiedData = res.stream()
                .flatMap(table -> table.getRecords().stream())
                .map(record -> Map.of(
                        "time", record.getTime(),
                        "temperature", record.getValue()
                ))
                .toList();

        return objectMapper.writeValueAsString(simplifiedData);
    }
}
