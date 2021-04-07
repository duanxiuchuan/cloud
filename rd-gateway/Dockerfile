FROM openjdk:8u212-jre
MAINTAINER lh@erongdu.com

COPY ./target/rd-gateway-2.1-RELEASE.jar /rd/rd-gateway-2.1-RELEASE.jar
#ADD agent/ /agent

#ENTRYPOINT ["java", "-javaagent:/agent/skywalking-agent.jar", "-Dskywalking.agent.service_name=rd-gateway", "-Dskywalking.collector.backend_service=skywalkingIp:11800", "-jar", "/rd/rd-gateway-2.1-RELEASE.jar"]
CMD ["java","-jar","/rd/rd-gateway-2.1-RELEASE.jar"]
