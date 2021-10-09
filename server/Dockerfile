FROM azul/zulu-openjdk:17.0.0

EXPOSE 8080

COPY build/libs/server-*.jar lib/
COPY start.sh ./
RUN chmod +x start.sh

ENTRYPOINT ["./start.sh"]
