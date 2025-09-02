package com.furkankayam.repository.influx;

import com.furkankayam.config.InfluxDBConfig;
import jakarta.annotation.PostConstruct;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Scope;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import com.influxdb.client.*;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@Service
@RequiredArgsConstructor
public class InfluxDBManager {

    private final InfluxDBConfig influxDBConfig;
    private InfluxDBClient influxDBClient;
    InfluxDBClientOptions options;

    @PostConstruct
    public void init() {
        options =
                InfluxDBClientOptions.builder()
                        .url(influxDBConfig.getInfluxUrl())
                        .authenticateToken(influxDBConfig.getInfluxToken().toCharArray())
                        .bucket(influxDBConfig.getInfluxBucket())
                        .org(influxDBConfig.getInfluxOrg())
                        .build();

        influxDBClient = InfluxDBClientFactory.create(options);
        OrganizationsApi organizationsApi = influxDBClient.enableGzip().getOrganizationsApi();
        var organization =
                organizationsApi.findOrganizations().stream()
                        .filter(org -> org.getName().equals(influxDBConfig.getInfluxOrg()))
                        .findFirst()
                        .orElseGet(
                                () ->
                                        organizationsApi.createOrganization(
                                                influxDBConfig.getInfluxOrg()));
        log.info("InfluxDB organization created: {}", organization.getName());

        BucketsApi bucketsApi = influxDBClient.enableGzip().getBucketsApi();
        var bucket =
                bucketsApi.findBuckets().stream()
                        .filter(b -> b.getName().equals(influxDBConfig.getInfluxBucket()))
                        .findFirst()
                        .orElseGet(
                                () ->
                                        bucketsApi.createBucket(
                                                influxDBConfig.getInfluxBucket(),
                                                organization.getId()));
        log.info("InfluxDB bucket created: {}", bucket.getName());
        bucket =
                bucketsApi.findBuckets().stream()
                        .filter(b -> b.getName().equals(influxDBConfig.getMetricsBucket()))
                        .findFirst()
                        .orElseGet(
                                () ->
                                        bucketsApi.createBucket(
                                                influxDBConfig.getMetricsBucket(),
                                                organization.getId()));
        log.info("InfluxDB bucket created: {}", bucket.getName());
    }

    @Bean
    @Scope("singleton")
    public InfluxDBClient influxDBClient() {
        if (influxDBClient == null) {
            influxDBClient = InfluxDBClientFactory.create(options);
        }
        return influxDBClient;
    }

    @Scheduled(initialDelay = 10000, fixedDelay = 10000)
    public boolean checkHealthInfluxDB() {
        var res = influxDBClient.ping();
        if (res) {
            log.debug("InfluxDB is healthy");
            return true;
        } else {
            log.warn("InfluxDB is not healthy");
            return false;
        }
    }
}
