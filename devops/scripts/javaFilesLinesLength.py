#!/usr/bin/env python3

# Print project java files lines distribution.

import glob
import matplotlib.pyplot as plt
import os

if __name__ == "__main__":
    search = "../../server/**/*.java"
    result = {}
    for filename in glob.iglob(search, recursive=True):
        num_lines = sum(1 for line in open(filename))
        result[os.path.basename(filename)] = num_lines

    result = dict(sorted(result.items(), key=lambda item: item[1]))
    print(result)
    plt.bar(*zip(*result.items()))

    plt.xticks(rotation=90)
    plt.yscale("log")
    plt.grid(True)
    plt.tight_layout()
    plt.show()
