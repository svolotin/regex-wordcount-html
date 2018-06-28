# Based on 64-bit 8u171
# FROM openjdk:8u171
FROM openjdk:10.0.1-jdk
# FROM openjdk:11-jdk
COPY /target/wordcounter-swarm.jar /home/wordcounter-swarm.jar
EXPOSE 8080
CMD java -Djava.net.preferIPv4Stack=true -jar /home/wordcounter-swarm.jar
