#!/usr/bin/env python3

import pandas as pd
import seaborn as sns
import matplotlib.pyplot as plt

# Default searborn style
sns.set()


def bar(data, fn):
    summary = data.groupby(["dataset", "type", "function"]
                           ).mean().drop("run", axis=1).reset_index()
    df = summary[summary.function == fn]

    plt.figure()
    sns.barplot(data=df, x="type", y="time", hue="dataset")
    plt.show()


if __name__ == "__main__":
    data = pd.read_csv("../data.csv")

    bar(data, fn="read")
    bar(data, fn="write")
