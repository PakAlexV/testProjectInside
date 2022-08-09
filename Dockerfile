FROM adoptopenjdk/openjdk16:ubi as builder
ADD . /src
WORKDIR /src
RUN ./mvnw package -DskipTests
RUN ls /src/target

FROM alpine:3.15 as packager
RUN apk --no-cache add openjdk16-jdk openjdk16-jmods binutils
ENV JAVA_MINIMAL="/opt/java-minimal"
RUN /usr/lib/jvm/java-16-openjdk/bin/jlink \
    --verbose \
    --add-modules \
        java.base,java.sql,java.naming,java.desktop,java.management,java.security.jgss,java.instrument \
    --compress 2 --strip-debug --no-header-files --no-man-pages \
    --release-info="add:IMPLEMENTOR=radistao:IMPLEMENTOR_VERSION=radistao_JRE" \
    --output "$JAVA_MINIMAL"

FROM alpine:3.15
LABEL maintainer="Test Inside"
ENV JAVA_HOME=/opt/java-minimal
ENV PATH="$PATH:$JAVA_HOME/bin"
COPY --from=packager "$JAVA_HOME" "$JAVA_HOME"
COPY --from=builder /src/target/test-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8090
ENTRYPOINT ["java","-jar","/app.jar"]