#!/usr/bin/python3

import argparse
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


def get_list_of_sub_dirs(curr_dir):
    """
    Get all sub dirs in given directory
    :param curr_dir: directory with git projects
    :type curr_dir: string
    :return: list of sub dirs
    :rtype: list
    """
    return [f.path for f in os.scandir(curr_dir) if f.is_dir()]


def filter_git_dirs(*dirs):
    """
    Check if there is a .git dir in specific dir.
    :param dirs: List of directory to filter.
    :type dirs: list
    :return: True if there is .git dir, otherwise False
    :rtype boolean
    """
    return ".git" in map(os.path.basename, *map(get_list_of_sub_dirs, dirs))


def check_is_master_branch(curr_dir):
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
    sub_dirs = get_list_of_sub_dirs(args.dir)
    git_dirs = list(filter(filter_git_dirs, sub_dirs))
    master_branch_dirs = list(filter(check_is_master_branch, git_dirs))
    for mb_dir in master_branch_dirs:
        print(f"{Color.BOLD}{'#' * 5}{' ' * 3}{mb_dir}{' ' * 3}{'#' * 5}{Color.RED}")
        checkout(mb_dir)
        print(f"{Color.END}")


if __name__ == "__main__":
    main()
