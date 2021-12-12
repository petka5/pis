#!/usr/bin/env python3

import json

import requests

access_token = None


def get_token():
    data = "client_id=petka_client&username=test-user&password=password&grant_type=password&" \
           "client_secret=petka"

    response = requests.post("http://localhost:8082/auth/realms/petka_realm/protocol/openid-connect/token",
                             headers={"Content-Type": "application/x-www-form-urlencoded"}, data=data)
    print(response)
    print(response.json()["access_token"])


def create_user():
    headers = {"Authorization": "Bearer " + access_token, "Content-Type": "application/json"}
    data = {
        "username": "test-user",
        "lastName": "Petka",
        "firstName": "Petka",
        "email": "petka@petka.com",
        "enabled": "true",
        "credentials": [{
            "type": "password",
            "value": "password",
            "temporary": "false"
        }]
    }

    response = requests.post("http://localhost:8082/auth/admin/realms/petka_realm/users/", headers=headers,
                             data=json.dumps(data))
    print(response)

    users = requests.get("http://localhost:8082/auth/admin/realms/petka_realm/users/", headers=headers)
    user_id = list(filter(lambda d: d['username'] == "test-user", users.json()))[0]["id"]

    roles = requests.get("http://localhost:8082/auth/admin/realms/petka_realm/roles/", headers=headers)
    role_id = list(filter(lambda d: d['name'] == "test-role", roles.json()))[0]["id"]
    mapping = [{"name": "test-role", "id": role_id}]
    mapping_response = requests.post(
        "http://localhost:8082/auth/admin/realms/petka_realm/users/" + user_id + "/role-mappings/realm",
        headers=headers, data=json.dumps(mapping))
    print(mapping_response)


def create_role():
    headers = {"Authorization": "Bearer " + access_token, "Content-Type": "application/json"}

    data = {"name": "test-role"}
    response = requests.post(
        "http://localhost:8082/auth/admin/realms/petka_realm/roles", headers=headers, data=json.dumps(data))
    print(response)


def create_client():
    # add Direct Access Grants Enabled and client_secret
    headers = {"Authorization": "Bearer " + access_token, "Content-Type": "application/json"}
    response = requests.post("http://localhost:8082/auth/admin/realms/petka_realm/clients/",
                             headers=headers, data=json.dumps(
            {"clientId": "petka_client", "secret": "petka", "directAccessGrantsEnabled": "true",
             "redirectUris": ["http://localhost:8082/*"]}))
    print(response)


def create_realm():
    headers = {"Authorization": "Bearer " + access_token, "Content-Type": "application/json"}
    data = {
        "id": "petka_realm_id",
        "realm": "petka_realm",
        "displayName": "Petka Realm",
        "enabled": 'true',
        "sslRequired": "external",
        "registrationAllowed": 'false',
        "loginWithEmailAllowed": 'true',
        "duplicateEmailsAllowed": 'false',
        "resetPasswordAllowed": 'false',
        "editUsernameAllowed": 'false',
        "bruteForceProtected": 'true'
    }
    response = requests.post("http://localhost:8082/auth/admin/realms", headers=headers, data=json.dumps(data))
    print(response)


def login():
    response = requests.post("http://localhost:8082/auth/realms/master/protocol/openid-connect/token",
                             headers={"Content-Type": "application/x-www-form-urlencoded"},
                             data="username=petka&password=petka&grant_type=password&client_id=admin-cli")
    global access_token
    access_token = response.json()["access_token"]
    print(response)


if __name__ == '__main__':
    login()
    create_realm()
    create_client()
    create_role()
    create_user()
    get_token()
