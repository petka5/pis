#!/usr/bin/env python3
import json
import logging
import os
import requests

LOG_FORMAT = "%(asctime)s %(name)s %(levelname)s %(threadName)s - %(message)s"
logger = logging.getLogger(__name__)

api_url = os.getenv("API_HOST", "http://localhost:8080")
id = None


def post():
    pet = {"age": 0, "kind": "string", "name": "string", "type": "DOMESTIC"}
    response = requests.post("{api_url}/pets".format(api_url=api_url), json=pet)
    global id
    id = response.json()["id"]
    logger.debug("POST: " + str(response.status_code) + str(response.json()))


def get():
    response = requests.get("{api_url}/pets/{id}".format(api_url=api_url, id=id))
    logger.debug("GET: " + str(response.status_code) + str(response.json()))


def get_all():
    response = requests.get("{api_url}/pets".format(api_url=api_url))
    logger.debug("GET ALL: " + str(response.status_code))


def patch():
    response = requests.patch("{api_url}/pets/{id}".format(api_url=api_url, id=id), json={"age": 12})
    logger.debug("PATCH: " + str(response.status_code) + str(response.json()))


def delete():
    response = requests.delete("{api_url}/pets/{id}".format(api_url=api_url, id=id))
    logger.debug("DELETE: " + str(response.status_code))


def chaos_monkey_enable():
    payload = json.dumps({
        "latencyRangeStart": 2000,
        "latencyRangeEnd": 5000,
        "latencyActive": "true",
        "exceptionsActive": "false",
        "killApplicationActive": "false"
    })

    response = requests.post("{api_url}/actuator/chaosmonkey/assaults".format(api_url=api_url), data=payload,
                             headers={'Content-Type': 'application/json'})
    logger.debug("chaos monkey: {}".format(response.content.decode()))


def chaos_monkey_disable():
    payload = json.dumps({
        "latencyRangeStart": 2000,
        "latencyRangeEnd": 5000,
        "latencyActive": "false",
        "exceptionsActive": "false",
        "killApplicationActive": "false"
    })

    response = requests.post("{api_url}/actuator/chaosmonkey/assaults".format(api_url=api_url), data=payload,
                             headers={'Content-Type': 'application/json'})
    logger.debug("chaos monkey: {}".format(response.content.decode()))


if __name__ == "__main__":
    logging.basicConfig(level=logging.DEBUG, format=LOG_FORMAT)
    logger.debug("Start of the tests")

    chaos_monkey_enable()
    post()
    get()
    get_all()
    patch()
    delete()
    chaos_monkey_disable()
    logger.debug("End of the tests")
