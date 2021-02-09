package com.learn.jmx.agent;

import com.learn.jmx.metrics.SystemConfig;
import com.learn.jmx.server.ServerFactory;

import javax.management.*;
import javax.management.remote.JMXConnectorServer;
import javax.management.remote.JMXServiceURL;
import java.lang.management.ManagementFactory;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class Agent {

    private static final int DEFAULT_NO_THREADS=10;
    private static final String DEFAULT_SCHEMA="default";

    public static void main(String[] args) {
        try {
           // MBeanServer mbs = ManagementFactory.getPlatformMBeanServer();

            SystemConfig mBean = new SystemConfig(DEFAULT_NO_THREADS, DEFAULT_SCHEMA);
            ObjectName name = new ObjectName("com.learn.jmx.metrics:type=SystemConfig");
          //  mbs.registerMBean(mBean, name);

            JMXConnectorServer jmx = ServerFactory.start(9990);
            jmx.getMBeanServer().registerMBean(mBean, name);

            do{
                Thread.sleep(3000);
                System.out.println("Thread Count="+mBean.getThreadCount()+":::Schema Name="+mBean.getSchemaName());
            }while(mBean.getThreadCount() !=0);

        } catch (MalformedObjectNameException | InstanceAlreadyExistsException | MBeanRegistrationException | NotCompliantMBeanException | InterruptedException ex) {
            System.out.println("Error!!! " + ex.getMessage());
        }
    }
}
