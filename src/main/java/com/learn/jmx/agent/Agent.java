package com.learn.jmx.agent;

import com.learn.jmx.metrics.SystemConfig;

import javax.management.*;
import java.lang.management.ManagementFactory;

public class Agent {

    private static final int DEFAULT_NO_THREADS=10;
    private static final String DEFAULT_SCHEMA="default";

    public static void main(String[] args) {
        try {
            MBeanServer mbs = ManagementFactory.getPlatformMBeanServer();

            SystemConfig mBean = new SystemConfig(DEFAULT_NO_THREADS, DEFAULT_SCHEMA);
            ObjectName name = new ObjectName("com.learn.jmx.metrics:type=SystemConfig");
            mbs.registerMBean(mBean, name);

            do{
                Thread.sleep(3000);
                System.out.println("Thread Count="+mBean.getThreadCount()+":::Schema Name="+mBean.getSchemaName());
            }while(mBean.getThreadCount() !=0);

        } catch (MalformedObjectNameException | InstanceAlreadyExistsException | MBeanRegistrationException | NotCompliantMBeanException | InterruptedException ex) {
            System.out.println("Error!!! " + ex.getMessage());
        }
    }
}
