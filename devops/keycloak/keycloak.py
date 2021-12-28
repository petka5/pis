#!/usr/bin/env python3

import requests

keycloak_url = "http://host.docker.internal:8082"
keycloak_username = "petka"
keycloak_password = "petka"
auth_header = None
realm_name = "petka_realm"
client_id = "petka_client"
client_secret = "petka"
role_organization = "organization"
role_operator = "operator"
username_organization = "organization"
username_operator = "operator"
password_organization = "password"
password_operator = "password"


class color:
    PURPLE = '\033[95m'
    CYAN = '\033[96m'
    DARKCYAN = '\033[36m'
    BLUE = '\033[94m'
    GREEN = '\033[92m'
    YELLOW = '\033[93m'
    RED = '\033[91m'
    BOLD = '\033[1m'
    UNDERLINE = '\033[4m'
    END = '\033[0m'


def get_token(username, password):
    data = f"client_id={client_id}&username={username}&password={password}&grant_type=password" \
           f"&client_secret={client_secret}"

    response = requests.post(f"{keycloak_url}/auth/realms/{realm_name}/protocol/openid-connect/token",
                             headers={"Content-Type": "application/x-www-form-urlencoded"}, data=data)

    print(f"{color.BOLD}{color.RED}{username.upper()} token{color.END}\n {response.json()['access_token']}")


def create_user(username, role, data):
    response = requests.post(f"{keycloak_url}/auth/admin/realms/{realm_name}/users/", headers=auth_header, json=data)
    print(f"create user {response}")

    users = requests.get(f"{keycloak_url}/auth/admin/realms/{realm_name}/users/", headers=auth_header)
    user_id = list(filter(lambda d: d['username'] == username, users.json()))[0]["id"]

    roles = requests.get(f"{keycloak_url}/auth/admin/realms/{realm_name}/roles/", headers=auth_header)
    role_id = list(filter(lambda d: d['name'] == role, roles.json()))[0]["id"]
    mapping = [{"name": role, "id": role_id}]
    mapping_response = requests.post(
        f"{keycloak_url}/auth/admin/realms/{realm_name}/users/{user_id}/role-mappings/realm",
        headers=auth_header, json=mapping)
    print(f"user mapping {mapping_response}")


def create_role(role):
    response = requests.post(f"{keycloak_url}/auth/admin/realms/{realm_name}/roles", headers=auth_header,
                             json={"name": role})
    print(f"create role {response}")


def create_client():
    response = requests.post(f"{keycloak_url}/auth/admin/realms/{realm_name}/clients/",
                             headers=auth_header,
                             json={"clientId": client_id, "secret": client_secret, "directAccessGrantsEnabled": "true",
                                   "redirectUris": [f"{keycloak_url}/*"]})
    print(f"create client {response}")

    mapper = {"protocol": "openid-connect",
              "config": {
                  "id.token.claim": "true",
                  "access.token.claim": "true",
                  "userinfo.token.claim": "true",
                  "multivalued": "",
                  "aggregate.attrs": "",
                  "user.attribute": "orgId",
                  "claim.name": "orgId",
                  "jsonType.label": "String"},
              "name": "orgId",
              "protocolMapper": "oidc-usermodel-attribute-mapper"}

    response = requests.get(f"{keycloak_url}/auth/admin/realms/{realm_name}/clients?clientId={client_id}",
                            headers=auth_header)

    id_client = list(filter(lambda d: d['clientId'] == client_id, response.json()))[0]["id"]

    response = requests.post(
        f"{keycloak_url}/auth/admin/realms/{realm_name}/clients/{id_client}/protocol-mappers/models",
        headers=auth_header, json=mapper)
    print(f"create client mapper {response}")


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
    response = requests.post(f"{keycloak_url}/auth/admin/realms", headers=auth_header, json=data)
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
    create_role(role_organization)
    create_role(role_operator)
    create_user(username_organization, role_organization, {
        "username": username_organization,
        "lastName": "Organization",
        "firstName": "Petka",
        "email": "organization@petka.com",
        "enabled": "true",
        "credentials": [{
            "type": "password",
            "value": password_organization,
            "temporary": "false"
        }],
        "attributes": {
            "orgId": ["3fa85f64-5717-4562-b3fc-2c963f66afa6"]
        }
    })
    create_user(username_operator, role_operator, {
        "username": username_operator,
        "lastName": "Operator",
        "firstName": "Petka",
        "email": "operator@petka.com",
        "enabled": "true",
        "credentials": [{
            "type": "password",
            "value": password_operator,
            "temporary": "false"
        }],
        "attributes": {
            "orgId": ["2fa85f64-5717-4562-b3fc-2c963f66afa5"]
        }
    })

    get_token(username_organization, password_organization)
    get_token(username_operator, password_operator)
