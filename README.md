# ZK Spring Boot

Starter Project for ZK with Spring Boot

## Useful Build Commands 
**NOTE:** Using the windows command line (cmd) you have to omit the "./" in front of the commands

build self executable jar and run

with gradle-wrapper
```
./gradlew clean build
java -jar build/libs/zk-spring-boot-0.1.0.jar
```
with maven-wrapper
```
./mvnw clean package
java -jar target/zk-spring-boot-0.1.0.jar
```

test pages are now available under:

http://localhost:8080/mvvm.zul (small MVVM example showing subnavigation and spring service integration)

http://localhost:8080/resources.zul (examples of accessing static resources the "springboot way" vs the "zk way")

http://localhost:8080/richlet/test (sample richlet - zk in pure java)

