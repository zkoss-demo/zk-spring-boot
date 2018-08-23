⛔️ **This project is no longer maintained** -> For ZK-Springboot support consider using the org.zkoss.zkspringboot:zkspringboot-starter maven dependency -> https://github.com/zkoss/zkspringboot

# ZK Spring Boot (Deprecated)

Starter Project for ZK with Spring Boot

## Useful Build Commands 
**NOTE:** Using the windows command line (cmd) you have to omit the "./" in front of the commands

with gradle-wrapper
```
#run Zats tests
./gradlew clean test

#run with gradle plugin
./gradlew bootRun

#build self executable jar and run
./gradlew clean build
java -jar build/libs/zk-spring-boot-0.1.0.jar
```
with maven-wrapper
```
#run Zats tests
./mvnw clean test

#run with maven plugin
./mvnw spring-boot:run

#build self executable jar and run
./mvnw clean package
java -jar target/zk-spring-boot-0.1.0.jar
```

test pages are now available under:

http://localhost:8080/mvvm (small MVVM example showing subnavigation and spring service integration)git 

http://localhost:8080/resources (examples of accessing static resources the "springboot way" vs the "zk way")

http://localhost:8080/richlet/test (sample richlet - zk in pure java)

## License
* Demo Code - [Apache License 2.0](http://www.apache.org/licenses/LICENSE-2.0)
