FROM openjdk:11


#WORKDIR /hello/libs/

CMD ["java", "-jar","./build/libs/hello-0.0.1-SNAPSHOT.jar "]