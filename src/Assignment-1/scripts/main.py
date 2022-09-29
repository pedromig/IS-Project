#!/usr/bin/env python3

import pandas as pd
import seaborn as sns
import matplotlib.pyplot as plt


plt.rcParams['text.usetex'] = True
plt.rcParams['text.latex.preamble'] = """
    \\usepackage[T1]{fontenc}
    \\usepackage[utf8]{inputenc}
    \\usepackage[mono=false]{libertine}
    \\usepackage[libertine]{newtxmath}
    \\usepackage[scaled=0.97]{zi4}
"""

# Default searborn style
sns.set_theme()
sns.set_style("darkgrid", {"axes.facecolor": ".9"})
sns.set_context("paper", font_scale=0.8,
                rc={"lines.linewidth": 2.5, "figure.figsize": (20, 20),
                    "figure.dpi": 300})


def average(data):
    df = data.groupby(["dataset", "type", "function"]
                      ).mean().drop("run", axis=1).reset_index()

    # Serialization & Deserialization Speeds
    plot = sns.barplot(data=df[df.function == "read"],
                       x="type", y="time", hue="dataset")
    plot.set(xlabel="File Type", ylabel="Time (ms)",
             title="Serialization Speed")
    plt.savefig("read-speed.pdf")

    plt.figure()
    plot = sns.barplot(data=df[df.function == "write"],
                       x="type", y="time", hue="dataset")
    plot.set(xlabel="File Type", ylabel="Time (ms)",
             title="Deserialization Speed")
    plt.savefig("write-speed.pdf")

    # Serialization File Size
    plt.figure()
    plot = sns.barplot(data=df[df.function == "write"],
                       x="type", y="size", hue="dataset")
    plot.set(xlabel="File Type", ylabel="Size (KiB)",
             title="Serialization File Size")
    plt.savefig("write-size.pdf")


def spread(data):

    # Serialization Speed Spread
    plt.figure()
    read = data[data.function == "read"]
    read = data[data.groupby(["dataset", "type", "function"]).time.transform(
        lambda x: (x < x.quantile(0.95)) & (x > (x.quantile(0.05)))).eq(1)
    ].reset_index()
    read.sort_values(by="type", inplace=True)

    plot = sns.violinplot(data=read, x="type", y="time",
                          hue="dataset", showfliers=False,
                          scale="count", inner="quartile")
    plot.set(xlabel="File Type", ylabel="Time (ms)",
             title="Serialization Speed")
    plt.savefig("read-speed-spread.pdf")

    # Deserialization Speed Spread
    plt.figure()
    write = data[data.function == "write"]
    write = data[data.groupby(["dataset", "type", "function"]).time.transform(
        lambda x: (x < x.quantile(0.95)) & (x > (x.quantile(0.05)))).eq(1)
    ].reset_index()
    write.sort_values(by="type", inplace=True)

    plot = sns.violinplot(data=write, x="type", y="time",
                          hue="dataset", showfliers=False,
                          scale="count", inner="quartile")
    plot.set(xlabel="File Type", ylabel="Size (KiB)",
             title="Serialization File Size")
    plt.savefig("write-speed-spread.pdf")

    # Serialization File Size Spread
    plt.figure()
    plot = sns.violinplot(data=write, x="type", y="size",
                          hue="dataset", showfliers=False,
                          scale="count", inner="quartile")
    plot.set(xlabel="File Type", ylabel="Time (ms)",
             title="Serialization File Size")
    plt.savefig("write-size-spread.pdf")


if __name__ == "__main__":
    data = pd.read_csv("../../../docs/Assignment-1/data/data.csv")
    average(data)
    spread(data)
