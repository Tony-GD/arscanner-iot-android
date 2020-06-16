package com.griddynamics.connectedapps;

import org.apache.commons.ssl.PKCS8Key;
import org.json.JSONException;
import org.junit.Test;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.security.GeneralSecurityException;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;


public class GenerateDemoData {
    public static final String CLIENT_ID = "projects/gd-gcp-rnd-connected-apps/locations/europe-west1/registries/europe-registry/devices/g-2sYMeJLSp06Jjkm439q1";
    private static final String PRIVATE_KEY_FILE = "/Users/user/projects/AirScanner/arscanner-iot-android/app/src/test/java/com/griddynamics/connectedapps/rsa_private_pkcs8";

    public void rsa() throws IOException, GeneralSecurityException {
        FileInputStream in = new FileInputStream( PRIVATE_KEY_FILE );

        PKCS8Key pkcs8 = new PKCS8Key( in, "changeit".toCharArray() );

        byte[] decrypted = pkcs8.getDecryptedBytes();
        PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec( decrypted );

// A Java PrivateKey object is born.
        PrivateKey privateKey = null;
        PublicKey publicKey = null;
        if ( pkcs8.isDSA() )
        {
//            publicKey = KeyFactory.getInstance("DSA").generatePublic(spec);
            privateKey = KeyFactory.getInstance( "DSA" ).generatePrivate( spec );
        }
        else if ( pkcs8.isRSA() )
        {
//            publicKey = KeyFactory.getInstance("RSA").generatePublic(spec);

            privateKey = KeyFactory.getInstance( "RSA" ).generatePrivate( spec );
        }
// For lazier types (like me):
        privateKey = pkcs8.getPrivateKey();
        publicKey = pkcs8.getPublicKey();
        System.out.println("PRIVATE KEY");
        byte[] privateKeyEncoded = privateKey.getEncoded();
        String privateAsString = new String(privateKeyEncoded);
        byte[] encoded = publicKey.getEncoded();
        String publicKeyAsString = new String(encoded);
        System.out.println(privateAsString);
        System.out.println("PUBLIC KEY");
        System.out.println(publicKeyAsString);
    }

    @Test
    public void main() throws JSONException, IOException, URISyntaxException, InterruptedException {
        List<String> deviceIds = new ArrayList<>();
        String deviceId = "ew94Q8WCVqeErGNE9c4C";
        deviceIds.add("d-" + deviceId);
        Random random = new Random();
        int sleepIntervalMs = 60_000 / deviceIds.size();
        try (MqttService mqttService = new MqttService(
                PRIVATE_KEY_FILE
                , CLIENT_ID, deviceIds)) {
            Runtime.getRuntime().addShutdownHook(new Thread(mqttService::close));
            while (!Thread.currentThread().isInterrupted()) {
                for (String device : deviceIds) {
                    mqttService.sendData(device, random.nextGaussian() * 20 + 30);
                    TimeUnit.MILLISECONDS.sleep(sleepIntervalMs);
                }
            }
        }
    }

}
