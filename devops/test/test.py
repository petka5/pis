#!/usr/bin/env python3
import logging
import os
import requests

LOG_FORMAT = "%(asctime)s %(name)s %(levelname)s %(threadName)s - %(message)s"
logger = logging.getLogger(__name__)

api_url = os.getenv("API_HOST", "http://localhost:8080") + "/pets"
id = None


def main():
    logger.debug("Hello World!")
    post()
    get()
    get_all()
    patch()
    delete()


def post():
    pet = {"age": 0, "kind": "string", "name": "string", "type": "DOMESTIC"}
    response = requests.post(api_url, json=pet)
    global id
    id = response.json()["id"]
    logger.debug("POST: " + str(response.status_code) + str(response.json()))


def get():
    response = requests.get(api_url + "/" + id)
    logger.debug("GET: " + str(response.status_code) + str(response.json()))


def get_all():
    response = requests.get(api_url)
    logger.debug("GET ALL: " + str(response.status_code))


def patch():
    response = requests.patch(api_url + "/" + id, json={"age": 12})
    logger.debug("PATCH: " + str(response.status_code) + str(response.json()))


def delete():
    response = requests.delete(api_url + "/" + id)
    logger.debug("DELETE: " + str(response.status_code))


if __name__ == "__main__":
    logging.basicConfig(level=logging.DEBUG, format=LOG_FORMAT)
    main()
