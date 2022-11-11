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
    df = data.groupby(["dataset", "type"]).mean().reset_index()
    plot = sns.barplot(data=df, x="type", y="time", hue="dataset")
    plot.set(xlabel="Strategy", ylabel="Time (ms)", title="Report Generation Speed")
    for i in plot.containers:
        plot.bar_label(i,)
    plt.savefig("../../../docs/Assignment-2/data/report-generation-speed.pdf")


def spread(data):
    df = data[data.groupby(["dataset", "type"]).time.transform(
        lambda x: (x < x.quantile(0.95)) & (x > (x.quantile(0.05)))).eq(1)
    ].reset_index()
    df.sort_values(by=["type", "dataset"], inplace=True)

    plt.figure()
    plot = sns.violinplot(data=df, x="type", y="time",
                          hue="dataset", showfliers=False,
                          scale="count", inner="quartile")
    plot.set(xlabel="Strategy", ylabel="Time (ms)",
             title="Report Generation Spread")
    plt.savefig("../../../docs/Assignment-2/data/report-generation-spread.pdf")


if __name__ == "__main__":
    data = pd.read_csv("../../../docs/Assignment-2/data/data.csv")
    print(data)
    average(data)
    spread(data)
