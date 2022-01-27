#!/usr/bin/python3

import argparse
import logging
import os
import requests
import signal
import subprocess

logging.basicConfig(
    level=logging.DEBUG,
    format="%(asctime)s [%(levelname)s] %(message)s"
)

"""
Script that performs action over all pods in k8n service via port-forward.
"""
PORT = None
PATH = None
ACTION = None
LABEL = None


def get_pods(selector):
    pods = subprocess.check_output(f'kubectl get pods -l {selector}', shell=True).splitlines()
    return list(map(lambda x: x.split()[0].decode(), pods[1:]))


def port_forward(pod):
    proc = subprocess.Popen(f'kubectl port-forward {pod} {PORT}:{PORT}', shell=True, stdout=subprocess.PIPE,
                            preexec_fn=os.setsid)
    print(f"Port forward {pod} \n{proc.stdout.readline().decode()}")
    return proc


def kill_subprocess(proc):
    # Send the signal to all the process groups
    os.killpg(os.getpgid(proc.pid), signal.SIGTERM)


def do_action():
    _ = requests.post(f"http://localhost:{PORT}{PATH}?action={ACTION}")
    response = requests.get(f"http://localhost:{PORT}{PATH}")
    print(f"\033[1m\033[91mResult: {response.json()['result']}\033[0m")


def main():
    for pod in get_pods(LABEL):
        try:
            proc = port_forward(pod)
            do_action()
        finally:
            kill_subprocess(proc)


if __name__ == "__main__":
    parser = argparse.ArgumentParser()
    parser.add_argument("-l", "--label", type=str, help="K8n selector", default="app=pets")
    parser.add_argument("-a", "--action", type=str, help="Action", default="action")
    parser.add_argument("-p", "--port", type=int, help="Port", default=8080)
    parser.add_argument("-u", "--path", type=str, help="Path", default="/operator/pets")

    args = parser.parse_args()
    PORT = args.port
    PATH = args.path
    ACTION = args.action
    LABEL = args.label

    main()
