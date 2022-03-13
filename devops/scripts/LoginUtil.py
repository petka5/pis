import base64
import requests
import time

"""
Login util class
"""


class LoginUtil:

    def __init__(self, **kwargs):
        self.__url = kwargs.get("url")
        self.__refresh_token = kwargs.get("refresh_token")
        self.__username = kwargs.get("username")
        self.__password = kwargs.get("password")
        self.__auth_token = None
        self.__token_age = 0

    @classmethod
    def with_refresh_token(cls, url, refresh_token):
        return cls(url=url, refresh_token=refresh_token)

    @classmethod
    def with_username_password(cls, url, username, password):
        return cls(url=url, username=username, password=password)

    def get_token(self):
        if self.__auth_token is None or time.time() - self.__token_age > 900:
            self.__token_age = time.time()
            if self.__refresh_token is not None:
                self.__auth_token = self.__get_token_with_refresh_token()
            else:
                self.__auth_token = self.__get_token_with_username_password()

        return self.__auth_token

    def __get_token_with_refresh_token(self):
        response = requests.post(self.__url, data=f"refresh_token={self.__refresh_token}",
                                 headers={'Content-Type': 'application/x-www-form-urlencoded'})

        return response.json()["access_token"]

    def __get_token_with_username_password(self):
        auth_str = '%s:%s' % (self.__username, self.__password)
        b64_auth_str = base64.b64encode(auth_str.encode('ascii')).decode('utf-8')
        headers = {'Authorization': 'Basic %s' % b64_auth_str,
                   'Content-Type': 'application/x-www-form-urlencoded'}

        response = requests.post(self.__url, data="grant_type=client_credentials", headers=headers)

        return response.json()["access_token"]
