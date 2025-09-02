package com.furkankayam.config;

import jakarta.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Configuration
@Data
@Slf4j
@RequiredArgsConstructor
public class InfluxDBConfig {

    private final Environment environment;

    @Value("${influx.url}")
    private String influxUrl;

    @Value("${influx.token}")
    private String influxToken;

    @Value("${influx.bucket}")
    private String influxBucket;

    @Value("${influx.org}")
    private String influxOrg;

    @Value("${management.influx.metrics.export.bucket}")
    private String metricsBucket;

    private String profile;

    @PostConstruct
    public void init() {
        if (environment.getActiveProfiles().length != 0) {
            profile = environment.getActiveProfiles()[0];
        } else {
            profile = "default";
        }
        log.info("{} is started. Active profile: {}", this.getClass().getSimpleName(), profile);
    }
}
