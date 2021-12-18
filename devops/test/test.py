#!/usr/bin/env python3
import logging
import os
import requests
import sys

LOG_FORMAT = "%(asctime)s %(name)s %(levelname)s %(threadName)s - %(message)s"
logger = logging.getLogger(__name__)

api_url = os.getenv("API_HOST", "http://host.docker.internal:8080")
keycloak_url = os.getenv("KEYCLOAK_URL", "http://host.docker.internal:8082")
keycloak_username = os.getenv("KEYCLOAK_USERNAME", "test-user")
keycloak_password = os.getenv("KEYCLOAK_PASSWORD", "password")
realm_name = os.getenv("KEYCLOAK_REALM", "petka_realm")
client_id = os.getenv("CLIENT_ID", "petka_client")
client_secret = os.getenv("CLIENT_SECRET", "petka")
auth_header = None
id = None


def post():
    pet = {"age": 0, "kind": "string", "name": "string", "type": "DOMESTIC"}
    response = requests.post(f"{api_url}/pets", json=pet, headers=auth_header)
    response.raise_for_status()
    global id
    id = response.json()["id"]
    logger.debug(f"POST: {response.status_code} {response.json()}")


def get():
    response = requests.get(f"{api_url}/pets/{id}", headers=auth_header)
    response.raise_for_status()
    logger.debug(f"GET: {response.status_code} {response.json()}")


def get_all():
    response = requests.get(f"{api_url}/pets", headers=auth_header)
    response.raise_for_status()
    logger.debug(f"GET ALL: {response.status_code}")


def patch():
    response = requests.patch(f"{api_url}/pets/{id}", json={"age": 12}, headers=auth_header)
    response.raise_for_status()
    logger.debug(f"PATCH: {response.status_code} {response.json()}")


def delete():
    response = requests.delete(f"{api_url}/pets/{id}", headers=auth_header)
    response.raise_for_status()
    logger.debug(f"DELETE: {response.status_code}")


def chaos_monkey_enable():
    payload = {
        "latencyRangeStart": 2000,
        "latencyRangeEnd": 5000,
        "latencyActive": "true",
        "exceptionsActive": "false",
        "killApplicationActive": "false"
    }

    response = requests.post(f"{api_url}/actuator/chaosmonkey/assaults", json=payload, headers=auth_header)
    response.raise_for_status()
    logger.debug(f"chaos monkey: {response.content.decode()}")


def chaos_monkey_disable():
    payload = {
        "latencyRangeStart": 2000,
        "latencyRangeEnd": 5000,
        "latencyActive": "false",
        "exceptionsActive": "false",
        "killApplicationActive": "false"
    }

    response = requests.post(f"{api_url}/actuator/chaosmonkey/assaults", json=payload, headers=auth_header)
    response.raise_for_status()
    logger.debug(f"chaos monkey: {response.content.decode()}")


def get_token():
    data = f"client_id={client_id}&username={keycloak_username}&password={keycloak_password}&grant_type=password" \
           f"&client_secret={client_secret}"

    response = requests.post(f"{keycloak_url}/auth/realms/{realm_name}/protocol/openid-connect/token",
                             headers={"Content-Type": "application/x-www-form-urlencoded"}, data=data)
    response.raise_for_status()
    global auth_header
    access_token = response.json()["access_token"]
    auth_header = {"Authorization": f"Bearer {access_token}", "Content-Type": "application/json"}
    print(f"get token {response}")


if __name__ == "__main__":
    logging.basicConfig(level=logging.DEBUG, format=LOG_FORMAT)
    logger.debug("Start of the tests")

    try:
        get_token()
        chaos_monkey_enable()
        post()
        get()
        get_all()
        patch()
        delete()
        chaos_monkey_disable()
    except:
        chaos_monkey_disable()
        sys.exit(-1)
    logger.debug("End of the tests")
