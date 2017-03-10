# minimal Zulu open JDK on Alpine Linux
# would build custom base image for production service
FROM delitescere/jdk:1.8.0_72

MAINTAINER Matt Reynolds <mattreyuk@gmail.com>
VOLUME /tmp
COPY adtech.jar adtech.jar
EXPOSE 80
# set entropy source for faster (but less random) startup
# see http://wiki.apache.org/tomcat/HowTo/FasterStartUp#Entropy_Source
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar", "adtech.jar"]
