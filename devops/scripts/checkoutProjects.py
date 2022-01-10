#!/usr/bin/python3

import argparse
import glob
import os
import subprocess

"""
This script checkouts all git projects that are in master branch in a given dir
"""


class Color:
    """
    Class that holds color constants.
    """
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


def get_git_dirs(curr_dir):
    """
    Get all sub dirs that have .git dir in them.
    :param curr_dir: Top level directory to search for git sub dirs.
    :type curr_dir: string
    :return: List of directories that match the condition.
    :rtype list
    """
    return [os.path.dirname(d) for d in glob.glob(f'{curr_dir}/*/.git') if os.path.isdir(d)]


def filter_master_branch(curr_dir):
    """
    Check if the current git branch is master.
    :param curr_dir: Current working dir
    :type curr_dir: string
    :return: True if current branch is master, otherwise False
    :rtype: boolean
    """
    branch = subprocess.check_output(['git', 'branch', '--show-current'], cwd=curr_dir).splitlines()
    return "master" == b"".join(branch).decode()


def checkout(curr_dir):
    """
    Executes git pull in the given dir
    :param curr_dir:  Current working dir
    :type curr_dir: string
    """
    process = subprocess.Popen(["git", "pull"], cwd=curr_dir, stdout=subprocess.PIPE)
    for c in iter(lambda: process.stdout.read(1), b''):
        print(c.decode(), end="")


def main():
    """
    Main method
    """
    parser = argparse.ArgumentParser()
    parser.add_argument("-d", "--dir", type=str, help="Directory with git projects", required=True)
    args = parser.parse_args()
    for mb_dir in filter(filter_master_branch, get_git_dirs(args.dir)):
        print(f"{Color.BOLD}{'#' * 5}{' ' * 3}{mb_dir}{' ' * 3}{'#' * 5}{Color.RED}")
        checkout(mb_dir)
        print(f"{Color.END}")

    print("Finished.")


if __name__ == "__main__":
    main()
