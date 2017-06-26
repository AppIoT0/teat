/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.postingdata;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import com.microsoft.azure.eventhubs.EventData;
import com.microsoft.azure.eventhubs.EventHubClient;
import com.microsoft.azure.servicebus.ConnectionStringBuilder;
import com.microsoft.azure.servicebus.ServiceBusException;

public class main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws ServiceBusException, ExecutionException, InterruptedException, IOException, URISyntaxException {

        /*
         * Get the values in < > from the registration ticket or from AppIoT.
         * Remove any backslashes in the URL's.
         */
        String sensorId = "615a2d2c-c7eb-404f-ac7a-c98bcdcdfed4";
        String value = "46"; //anything you want
        long timestamp = System.currentTimeMillis();

        String message = "[{id: \"" + sensorId + "\",v:[{m:[" + value + "],t:" + timestamp + "}]}]";

        URI uri = new URI("sb://eappiotsens.servicebus.windows.net");
        String amqpServicePath = "datacollectoroutbox/publishers/e0860812-45ae-4a91-a79a-90b8e6c0954e";
        String amqpSas = "SharedAccessSignature sr=sb%3a%2f%2feappiotsens.servicebus.windows.net%2fdatacollectoroutbox%2fpublishers%2fe0860812-45ae-4a91-a79a-90b8e6c0954e&sig=%2bCiv4TT4Yu10O6pCds49nHGnq98qR25vWnevgaZoSvU%3d&se=4647511929&skn=SendAccessPolicy";

        ConnectionStringBuilder connStr = new ConnectionStringBuilder(uri, amqpServicePath, amqpSas);
        System.out.print("\n\rovo je ispis bree:\n\r "+connStr.toString());
        byte[] payloadBytes = message.getBytes("UTF-8");
        EventData sendEvent = new EventData(payloadBytes);
        System.out.println("\n\rOvo su bytes:\n\r "+payloadBytes.toString());
        System.out.print("\n\rOVO ISPISUJEMO VANI:\n\r "+sendEvent.getBody());

        Map<String, String> properties = new HashMap<String, String>();
        properties.put("DataCollectorId", "e0860812-45ae-4a91-a79a-90b8e6c0954e");
        properties.put("SensorCollectionId", "615a2d2c-c7eb-404f-ac7a-c98bcdcdfed4");
        properties.put("PayloadType", "Measurements");
        sendEvent.setProperties(properties);

        System.out.print("\n\rLLol:\n\r"+sendEvent.getProperties());
        EventHubClient ehClient = EventHubClient.createFromConnectionStringSync(connStr.toString());
        ehClient.sendSync(sendEvent);
        ehClient.close();

        System.out.println("done");
    }
}
