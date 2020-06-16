package com.griddynamics.connectedapps;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

public class MqttService implements AutoCloseable {

    public static String firebaseToken = "eyJhbGciOiJSUzI1NiIsImtpZCI6IjRlMjdmNWIwNjllYWQ4ZjliZWYxZDE0Y2M2Mjc5YmRmYWYzNGM1MWIiLCJ0eXAiOiJKV1QifQ.eyJuYW1lIjoiQW50b24gUnVtaWFudHNldiIsInBpY3R1cmUiOiJodHRwczovL2xoNC5nb29nbGV1c2VyY29udGVudC5jb20vLXp6Mmt4dGFXZUlNL0FBQUFBQUFBQUFJL0FBQUFBQUFBQUFBL0FNWnV1Y2tIeEtFNnRrQmluMDQ5bDdPWHhEbUd4c2hnbGcvcGhvdG8uanBnIiwiaXNzIjoiaHR0cHM6Ly9zZWN1cmV0b2tlbi5nb29nbGUuY29tL2dkLWdjcC1ybmQtY29ubmVjdGVkLWFwcHMiLCJhdWQiOiJnZC1nY3Atcm5kLWNvbm5lY3RlZC1hcHBzIiwiYXV0aF90aW1lIjoxNTkyMzAxNzgzLCJ1c2VyX2lkIjoiZ3ZWbmxmZUVuM1kyU0hMSEJwdVVGSTFhTTJOMiIsInN1YiI6Imd2Vm5sZmVFbjNZMlNITEhCcHVVRkkxYU0yTjIiLCJpYXQiOjE1OTIzMDE3ODMsImV4cCI6MTU5MjMwNTM4MywiZW1haWwiOiJhcnVtaWFudHNldkBncmlkZHluYW1pY3MuY29tIiwiZW1haWxfdmVyaWZpZWQiOnRydWUsImZpcmViYXNlIjp7ImlkZW50aXRpZXMiOnsiZ29vZ2xlLmNvbSI6WyIxMDA5MzQ2OTQ0NjU1MTI0OTM0MzAiXSwiZW1haWwiOlsiYXJ1bWlhbnRzZXZAZ3JpZGR5bmFtaWNzLmNvbSJdfSwic2lnbl9pbl9wcm92aWRlciI6Imdvb2dsZS5jb20ifX0.H_N3ssR5scLmUmIjnOSQhkuTD5CVHzN-SbMmhRUy5MdLO7MuqSgQzjvquuP0JnCbABSOVeumyISPrgGFMPyYuzSr2j7AkOBAzo60Xp8OwWAnnjfqg3e7avy9tlF1DP8UKLOmofvZELMWerdzbjvSxkSXVxlS3kIGslHitP5FQx8Gw5Y55-w63PBjsw3u6Uu5lHJ2OVwG_SfNTtRQlR4rUvNWWit4hOIivjxvpInd1gpfZ5uTggzMbuuXY2QI6WmXxtUx9TpTRSwehZ3SQ_7tKiVfmInnN_RxeKV4_IIPjgBEfri-O5sk968HE41QRYEnlGjce_sdndRy4W4GxN608g";
    static String mqttBridgeHostname = "mqtt.googleapis.com";
    static short mqttBridgePort = 8883;
    static String messageType = "events";
    static String projectId = "gd-gcp-rnd-connected-apps";
    int waitTime = 120;
    private List<String> attachedDevices = new ArrayList<>();

    private MqttClient mqttClient;
    private final RunnableEx init;


    public MqttService(String privateKeyFile, String clientId, List<String> devices)  {
        init = () -> {
            System.out.println("MQTT service starting");
            startMqtt(clientId, privateKeyFile);
            devices.forEach(this::attachDevice);
        };
        try {
            init.run();
        } catch (Exception e) {
            System.err.println("Can not start MQTT client" + e);
            throw new RuntimeException(e);
        }
    }



    @Override
    public void close() {

        if (mqttClient != null) {
            try {
                attachedDevices.forEach(this::detachDeviceFromGateway);
                if (mqttClient.isConnected()) {
                    mqttClient.disconnect();
                }
                mqttClient.close();
                System.out.println("MQTT service closed");
            } catch (MqttException e) {
                System.err.println("Exception on closing MQTT"  + e);
            }

        }
    }

    public void sendData(String deviceId, Double singleValue) {
        if (!"events".equals(messageType) && !"state".equals(messageType)) {
            System.out.println("Invalid message type, must ether be 'state' or events'");
            return;
        }
        final String dataTopic = String.format("/devices/%s/%s", deviceId, messageType);
        try {
            MqttMessage message = new MqttMessage(singleValue.toString().getBytes(StandardCharsets.UTF_8.name()));
            message.setQos(1);
            mqttClient.publish(dataTopic, message);
        } catch (Exception e) {
            System.err.println("Sending error, reconnect" + e);
            close();
            try {
                init.run();
                //mqttClient.publish(dataTopic, message);
            } catch (Exception ex) {
                System.err.println("init error" + ex);
            }

        }
    }



    private static String createJwtRsa(String projectId, String privateKeyFile)
            throws NoSuchAlgorithmException, IOException, InvalidKeySpecException {
        Instant now = Instant.now();
        JwtBuilder jwtBuilder =
                Jwts.builder()
                        .setIssuedAt(new Date(now.toEpochMilli()))
                        .setExpiration(new Date(now.plus(20, ChronoUnit.MINUTES).toEpochMilli()))
                        .setAudience(projectId);

        byte[] keyBytes = Files.readAllBytes(Paths.get(privateKeyFile));
        PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory kf = KeyFactory.getInstance("RSA");

        return jwtBuilder.signWith(SignatureAlgorithm.RS256, kf.generatePrivate(spec)).compact();
    }

    protected void startMqtt(
            String clientId, String privateKeyFile)
            throws NoSuchAlgorithmException, IOException, MqttException, InterruptedException,
            InvalidKeySpecException {
        final String mqttServerAddress =
                String.format("ssl://%s:%s", mqttBridgeHostname, mqttBridgePort);


        MqttConnectOptions connectOptions = new MqttConnectOptions();
        connectOptions.setMqttVersion(MqttConnectOptions.MQTT_VERSION_3_1_1);
        Properties sslProps = new Properties();
        sslProps.setProperty("com.ibm.ssl.protocol", "TLSv1.2");
        connectOptions.setSSLProperties(sslProps);

        connectOptions.setUserName("unused");
        connectOptions.setPassword(createJwtRsa(projectId, privateKeyFile).toCharArray());


        System.out.println(String.format("%s", clientId));

        // Create a client, and connect to the Google MQTT bridge.
        mqttClient = new MqttClient(mqttServerAddress, clientId, new MemoryPersistence());

        long initialConnectIntervalMillis = 500L;
        long maxConnectIntervalMillis = 6000L;
        long maxConnectRetryTimeElapsedMillis = 900000L;
        float intervalMultiplier = 1.5f;

        long retryIntervalMs = initialConnectIntervalMillis;
        long totalRetryTimeMs = 0;

        while ((totalRetryTimeMs < maxConnectRetryTimeElapsedMillis) && !mqttClient.isConnected())  {
            try {
                mqttClient.connect(connectOptions);
            } catch (MqttException e) {
                int reason = e.getReasonCode();

                if (reason == MqttException.REASON_CODE_CONNECTION_LOST
                        || reason == MqttException.REASON_CODE_SERVER_CONNECT_ERROR) {
                    System.out.println("Retrying MQTT connect in " + retryIntervalMs / 1000.0 + " seconds.");
                    Thread.sleep(retryIntervalMs);
                    totalRetryTimeMs += retryIntervalMs;
                    retryIntervalMs *= intervalMultiplier;
                    if (retryIntervalMs > maxConnectIntervalMillis) {
                        retryIntervalMs = maxConnectIntervalMillis;
                    }
                } else {
                    System.err.println("Error MQTT connection" + e);
                    throw e;
                }
            }
        }
    }



    public void attachDevice(String deviceId) {
        final String attachTopic = String.format("/devices/%s/attach", deviceId);
        System.out.println("Attaching: {}" + attachTopic);
        String attachPayload = "{}";
        MqttMessage message = null;
        try {
            message = new MqttMessage(attachPayload.getBytes(StandardCharsets.UTF_8.name()));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        message.setQos(1);
        try {
            mqttClient.publish(attachTopic, message);
            attachedDevices.add(deviceId);
        } catch (MqttException e) {
            System.err.println("Con not attache device {}" + deviceId + e);
        }
    }

    private void detachDeviceFromGateway(String deviceId)  {
        // [START iot_detach_device]
        final String detachTopic = String.format("/devices/%s/detach", deviceId);
        System.out.println("Detaching: {}" + detachTopic);
        String attachPayload = "{}";
        MqttMessage message = null;
        try {
            message = new MqttMessage(attachPayload.getBytes(StandardCharsets.UTF_8.name()));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        message.setQos(1);
        try {
            mqttClient.publish(detachTopic, message);
        } catch (MqttException e) {
            System.err.println("Con not detach device {}" + deviceId + e);
        }
        // [END iot_detach_device]
    }

}