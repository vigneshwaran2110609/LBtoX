package com.example.LBtoX.utils;

import javax.management.MBeanServerConnection;
import javax.management.ObjectName;
import java.lang.management.ManagementFactory;

public class QueueUtils {

    public static long getQueueSize(String queueName) {
        try {
            MBeanServerConnection connection = ManagementFactory.getPlatformMBeanServer();
            ObjectName objectName = new ObjectName(
                    "org.apache.activemq:type=Broker,brokerName=localhost,destinationType=Queue,destinationName=" + queueName
            );

            return (Long) connection.getAttribute(objectName, "QueueSize");
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }
}
