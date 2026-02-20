package com.octaviomunoz.centralhtmonitor;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class CentralMonitoringServiceTest {

    private boolean checkAlarm(String rawData) {
        String id = rawData.split(";")[0].split("=")[1].trim();
        double val = Double.parseDouble(rawData.split(";")[1].split("=")[1].trim());

        if (id.startsWith("t")) return val > 35.0;
        if (id.startsWith("h")) return val > 50.0;
        return false;
    }

    @Test
    void testTemperatureAlarm() {
        assertTrue(checkAlarm("sensor_id=t1; value=36"));
        assertFalse(checkAlarm("sensor_id=t1; value=30"));
    }

    @Test
    void testHumidityAlarm() {
        assertTrue(checkAlarm("sensor_id=h1; value=60"));
        assertFalse(checkAlarm("sensor_id=h1; value=40"));
    }
}