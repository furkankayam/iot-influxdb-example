# 🌐 IoT InfluxDB Example

<div align="center">
  <h3>⚡ Architecture Schema</h3>
  <img src="./images/schema.png" alt="architecture schema">
</div>

<br>

<details>
<summary>🌡️ ESP32</summary>
<img src="./images/esp32.jpg" alt="esp32">
</details>

<br>

<details>
<summary>💬 MQTT</summary>
<img src="./images/mqttx.png" alt="mqttx">
</details>

<br>

<details>
<summary>📊 InfluxDB</summary>
<img src="./images/influxdb.png" alt="influxdb">
</details>

<br>

<details>
<summary>🐳 Docker</summary>
<img src="./images/docker.png" alt="docker">
</details>

<br>

<details>
<summary>🧪 Postman</summary>
<img src="./images/postman.png" alt="postman">
</details>

<br>

## 📖 About

✅ This project demonstrates how to collect, store, and analyze temperature data from ESP32 microcontrollers via MQTT protocol into InfluxDB, a time series database.

<br>

## 🛠️ Technologies Used

[![ESP32](https://img.shields.io/badge/ESP32-S3-000?style=for-the-badge&logo=espressif&logoColor=white&color=E7352C)](https://www.espressif.com/)
[![MQTT](https://img.shields.io/badge/MQTT-2.0.18-000?style=for-the-badge&logo=mqtt&logoColor=white&color=660066)](https://mqtt.org/)
[![InfluxDB](https://img.shields.io/badge/InfluxDB-2.7.9-000?style=for-the-badge&logo=influxdb&logoColor=white&color=22ADF6)](https://www.influxdata.com/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.5.5-000?style=for-the-badge&logo=springboot&logoColor=white&color=6DB33F)](https://spring.io/projects/spring-boot)
[![Java](https://img.shields.io/badge/Java-17-000?style=for-the-badge&logo=openjdk&logoColor=white&color=FF9A00)](https://www.oracle.com/java/)
[![Docker](https://img.shields.io/badge/Docker-28.3-000?style=for-the-badge&logo=docker&logoColor=white&color=2496ED)](https://www.docker.com/)

<br>

## ⚙️ ESP32 Configuration

```ino
const char* ssid = "<WIFI_NAME>";
const char* password = "<WIFI_PASSWORD>";
const char* mqtt_server = "<MQTT_SERVER_IP>";
```

<br>

## 🚀 Getting Started

```bash
cd iot-influxdb-example
```

```bash
docker-compose up -d
```

<br>

## 📡 InfluxDB API Endpoints

### 🔍 Get Temperatures

```bash
curl -X 'GET' \
  'http://localhost:8080/api/devices/36?from=-1h' \
  -H 'accept: application/json'
```

<br>

# License

This project is licensed under the MIT License. See the [LICENSE](LICENSE) file for details

Created by [Mehmet Furkan KAYA](https://www.linkedin.com/in/mehmet-furkan-kaya/)
