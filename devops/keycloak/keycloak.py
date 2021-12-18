#!/usr/bin/env python3

import json

import requests

keycloak_url = "http://host.docker.internal:8082"
keycloak_username = "petka"
keycloak_password = "petka"
auth_header = None
realm_name = "petka_realm"
client_id = "petka_client"
client_secret = "petka"
role = "test-role"
username = "test-user"
password = "password"


def get_token():
    data = "client_id=petka_client&username=test-user&password=password&grant_type=password&" \
           "client_secret=petka"

    response = requests.post(f"{keycloak_url}/auth/realms/{realm_name}/protocol/openid-connect/token",
                             headers={"Content-Type": "application/x-www-form-urlencoded"}, data=data)
    print(f"get token {response}")
    print(response.json()["access_token"])


def create_user():
    data = {
        "username": username,
        "lastName": "Petka",
        "firstName": "Petka",
        "email": "petka@petka.com",
        "enabled": "true",
        "credentials": [{
            "type": "password",
            "value": password,
            "temporary": "false"
        }]
    }

    response = requests.post(f"{keycloak_url}/auth/admin/realms/{realm_name}/users/", headers=auth_header,
                             data=json.dumps(data))
    print(f"create user {response}")

    users = requests.get(f"{keycloak_url}/auth/admin/realms/{realm_name}/users/", headers=auth_header)
    user_id = list(filter(lambda d: d['username'] == username, users.json()))[0]["id"]

    roles = requests.get(f"{keycloak_url}/auth/admin/realms/{realm_name}/roles/", headers=auth_header)
    role_id = list(filter(lambda d: d['name'] == role, roles.json()))[0]["id"]
    mapping = [{"name": role, "id": role_id}]
    mapping_response = requests.post(
        f"{keycloak_url}/auth/admin/realms/{realm_name}/users/{user_id}/role-mappings/realm",
        headers=auth_header, data=json.dumps(mapping))
    print(f"user mapping {mapping_response}")


def create_role():
    response = requests.post(
        f"{keycloak_url}/auth/admin/realms/{realm_name}/roles",
        headers=auth_header,
        data=json.dumps({"name": role}))
    print(f"create role {response}")


def create_client():
    response = requests.post(f"{keycloak_url}/auth/admin/realms/{realm_name}/clients/",
                             headers=auth_header, data=json.dumps(
            {"clientId": client_id, "secret": client_secret, "directAccessGrantsEnabled": "true",
             "redirectUris": [f"{keycloak_url}/*"]}))
    print(f"create client {response}")


def create_realm():
    data = {
        "id": "petka_realm_id",
        "realm": realm_name,
        "displayName": "Petka Realm",
        "enabled": 'true',
        "sslRequired": "external",
        "registrationAllowed": 'false',
        "loginWithEmailAllowed": 'true',
        "duplicateEmailsAllowed": 'false',
        "resetPasswordAllowed": 'false',
        "editUsernameAllowed": 'false',
        "bruteForceProtected": 'true',
        "accessTokenLifespan": 1800
    }
    response = requests.post(f"{keycloak_url}/auth/admin/realms", headers=auth_header,
                             data=json.dumps(data))
    print(f"create realm {response}")


def login():
    response = requests.post(
        f"{keycloak_url}/auth/realms/master/protocol/openid-connect/token",
        headers={"Content-Type": "application/x-www-form-urlencoded"},
        data=f"username={keycloak_username}&password={keycloak_password}&grant_type=password&client_id=admin-cli")

    global auth_header
    auth_header = {"Authorization": "Bearer {access_token}".format(access_token=response.json()["access_token"]),
                   "Content-Type": "application/json"}
    print(f"login {response}")


if __name__ == '__main__':
    login()
    create_realm()
    create_client()
    create_role()
    create_user()
    get_token()
