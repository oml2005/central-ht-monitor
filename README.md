# Central HT Monitor
A system that monitors temperature and humidity sensors in a warehouse using Kafka.

## What it does
This project listens for sensor data coming in over UDP and sends alerts when temperatures or humidity get too high.

- Temperature sensors send data to port 3344
- Humidity sensors send data to port 3355
- Alerts trigger when temp > 35°C or humidity > 50%

## Files in this project
- `WarehouseService.java` - Listens for UDP messages and sends them to Kafka
- `CentralMonitoringService.java` - Reads from Kafka and checks for alarms
- `CentralMonitoringServiceTest.java` - Tests for the alarm logic
- `pom.xml` - Maven config file with dependencies

## How to run it
1 .  Build the project according to Maven
2.   Run the Central Monitor consumer and Warehouse producer services
3.   For local testing, aunch kafka on localhost:9092
4.   Launch Zookeeper

### You need these installed first (alrelady in pom.xml file):
- Java 8
- Apache Kafka
- Maven


## Testing
Once all services up and running, from terminal you can use netcat : 
issue commands like this : 
echo "sensor_id=t1; value=25.5" | nc -u localhost 3344

setting the sensor id, read value and sensor listening port

use several values on sensors to explore the system responses 
