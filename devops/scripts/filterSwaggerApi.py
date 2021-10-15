#!/usr/bin/env python3

"""
Filter swagger yaml to exclude paths by given regex.
Useful to generate internal and external sdk from a single yaml file.
"""
import argparse
import re

import yaml

PATHS = "paths"

if __name__ == "__main__":
    parser = argparse.ArgumentParser()
    parser.add_argument("-i", "--input", type=str, help="input swagger file", required=True)
    parser.add_argument("-o", "--output", type=str, help="output swagger file", required=True)
    parser.add_argument("-er", "--exclude-regex", nargs='+', default=[], help="exclude swagger path regex",
                        required=True)
    args = parser.parse_args()

    with open(args.input, 'r') as f:
        doc = yaml.load(f, Loader=yaml.FullLoader)

    p = re.compile("|".join(args.exclude_regex))
    for i in list(doc[PATHS]):
        if not p.search(i):
            continue
        del doc[PATHS][i]

    with open(args.output, 'w') as file:
        documents = yaml.dump(doc, file, default_flow_style=False, sort_keys=False)
    print(doc)
