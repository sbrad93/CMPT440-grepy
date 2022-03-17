FROM maven:latest


WORKDIR /grepy
COPY * ./

RUN mvn clean install

CMD ["bash"]
