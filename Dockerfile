FROM bigtruedata/sbt 

ENV DOWNLOAD www.h2database.com/h2-2018-03-18.zip
ENV DATA_DIR /opt/h2-data


# install h2 database
RUN curl ${DOWNLOAD} -o h2.zip \
    && unzip h2.zip -d /opt/ \
    && rm h2.zip \
    && mkdir -p ${DATA_DIR}

EXPOSE 81 1521

# To Run h2 Server
# RUN java -cp /opt/h2/bin/h2*.jar org.h2.tools.Server \
#  	-web -webAllowOthers -webPort 81 \
#  	-tcp -tcpAllowOthers -tcpPort 1521 \
# -baseDir ${DATA_DIR}

# install mongodb
RUN apt-get update
RUN echo "deb http://repo.mongodb.org/apt/debian jessie/mongodb-org/3.6 main" | \
    tee /etc/apt/sources.list.d/mongodb-org-3.6.list && \
    apt-get install -y mongodb

RUN service mongodb start

CMD ["apt-get", "update"]