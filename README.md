# Example how to monitoring a Java App using Prometheus and Grafana

This project will help you to understand how to expose metrics and publish them into Prometheus and build a Grafana dashboard to visualize them

## Build Docker for the java App

The build of the java app includes the jmx_exporter agent

https://github.com/prometheus/jmx_exporter

```
docker build -t javaApp_prometheus -f .\docker\Dockerfile .
```

-Dcom.sun.management.jmxremote.port=9999
-Dcom.sun.management.jmxremote.authenticate=false
-Dcom.sun.management.jmxremote.ssl=false

Todo:
- Check if prometheus is running
- Check how to use the jmx_exporter
