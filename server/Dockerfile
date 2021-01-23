FROM azul/zulu-openjdk:14.0.1

EXPOSE 8080

COPY build/libs/server-*.jar lib/
COPY start.sh ./
RUN chmod +x start.sh

ENTRYPOINT ["./start.sh"]
