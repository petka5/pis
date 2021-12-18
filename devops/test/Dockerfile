FROM python:3.9.9

COPY test.py ./

COPY modules.txt ./
RUN pip3 install --no-cache-dir -r modules.txt

ENV API_HOST=http://pis:8080

RUN chmod +x test.py
ENTRYPOINT ["./test.py"]
