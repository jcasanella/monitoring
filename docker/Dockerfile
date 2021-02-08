FROM openjdk:8-jdk-alpine AS APP_BUILD

LABEL version="1.0"
LABEL author="Jordi Casanella"
LABEL description="Docker image to export jmx metrics from a java app build with gradle"

RUN apk add --no-cache curl tar bash git

ARG USER_HOME_DIR="/root"

# installing maven
ARG MAVEN_VERSION=3.6.3
ARG MAVEN_BASE_URL=https://archive.apache.org/dist/maven/maven-3/${MAVEN_VERSION}/binaries
# extracted from maven binaries folder https://archive.apache.org/dist/maven/maven-3/$MAVEN_VERSION/binaries/apache-maven-$MAVEN_VERSION-bin.zip.sha512
ARG MAVEN_SHA=1c095ed556eda06c6d82fdf52200bc4f3437a1bab42387e801d6f4c56e833fb82b16e8bf0aab95c9708de7bfb55ec27f653a7cf0f491acebc541af234eded94d

RUN mkdir -p /usr/share/maven /usr/share/maven/ref \
  && echo "Downloading maven" \
  && curl -fsSL -o /tmp/apache-maven.zip ${MAVEN_BASE_URL}/apache-maven-${MAVEN_VERSION}-bin.zip \
  \
  && echo "Checking download hash" \
  && echo "${MAVEN_SHA}  /tmp/apache-maven.zip" | sha512sum -c - \
  \
  && echo "Unziping maven" \
  && unzip -d /usr/share/maven /tmp/apache-maven.zip \
  \
  && echo "Cleaning and setting links" \
  && rm -f /tmp/apache-maven.zip \
  && ln -s /usr/share/maven/bin/mvn /usr/bin/mvn

ENV MAVEN_HOME /usr/share/maven/apache-maven-$MAVEN_VERSION
ENV MAVEN_CONFIG "$USER_HOME_DIR/.m2"

ENV PATH $PATH:$MAVEN_HOME/bin

# installing jmx exporter
WORKDIR /jmx_exporter

RUN echo "Clonning jmx export" \
  && git clone https://github.com/prometheus/jmx_exporter.git \
  && cd jmx_exporter \
  && mvn package

# installing gradle
ARG GRADLE_VERSION=6.8.2
ARG GRADLE_BASE_URL=https://services.gradle.org/distributions
# extracted from https://services.gradle.org/distributions/gradle-$GRADLE_VERSION-bin.zip.sha256
ARG GRADLE_SHA=8de6efc274ab52332a9c820366dd5cf5fc9d35ec7078fd70c8ec6913431ee610

RUN mkdir -p /usr/share/gradle /usr/share/gradle/ref \
  && echo "Downlaoding gradle hash" \
  && curl -fsSL -o /tmp/gradle.zip ${GRADLE_BASE_URL}/gradle-${GRADLE_VERSION}-bin.zip \
  \
  && echo "Checking download hash" \
  && echo "${GRADLE_SHA}  /tmp/gradle.zip" | sha256sum -c - \
  \
  && echo "Unziping gradle" \
  && unzip -d /usr/share/gradle /tmp/gradle.zip \
   \
  && echo "Cleaning and setting links" \
  && rm -f /tmp/gradle.zip \
  && ln -s /usr/share/gradle/gradle-${GRADLE_VERSION} /usr/bin/gradle

# Define environmental variables required by gradle
ENV GRADLE_VERSION $GRADLE_VERSION
ENV GRADLE_HOME /usr/bin/gradle
ENV GRADLE_USER_HOME /cache

ENV PATH $PATH:$GRADLE_HOME/bin

VOLUME $GRADLE_USER_HOME

# Copy java project
WORKDIR /project_code
COPY ./ ./
RUN  echo "Building App" \
  && gradle build --no-daemon

CMD [""]

# Create small image with the build
FROM openjdk:8u212-jre-alpine3.9

WORKDIR /app

COPY --from=APP_BUILD /project_code/build/libs/*.jar ./
COPY --from=APP_BUILD /jmx_exporter/jmx_exporter/jmx_prometheus_javaagent/target/*.jar ./
COPY --from=APP_BUILD /jmx_exporter/jmx_exporter/jmx_prometheus_httpserver/target/*.jar ./

EXPOSE 8080/tcp

CMD [""]

ENTRYPOINT ["java", "-jar", "/app/JMXMetrics-1.0-SNAPSHOT.jar"]

