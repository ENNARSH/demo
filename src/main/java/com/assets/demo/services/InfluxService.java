package com.assets.demo.services;

import org.influxdb.InfluxDB;
import org.influxdb.InfluxDBFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class InfluxService {

    @Value("${influx.url}")
    private String url;

    @Value("${influx.username}")
    private String username;

    @Value("${influx.password}")
    private String password;


    private InfluxDB getInfluxDB() {
        return InfluxDBFactory.connect(url, username, password);
    }

    public void createRetentionPolicy(String retentionName, String databaseName) {
        InfluxDB influxDB = getInfluxDB();
        influxDB.createRetentionPolicy(retentionName, databaseName, "30d", 1, false);
    }

    public void createDatabase(String databaseName) {
        InfluxDB influxDB = getInfluxDB();
        influxDB.createDatabase(databaseName);
    }
}
