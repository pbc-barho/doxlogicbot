import numpy as np
from matplotlib.pyplot import figure, show
import pandas as pd
from scipy.optimize import curve_fit
import math
import matplotlib.pyplot as plt


# Summing up all elements in a list
def sum(x):
    sum = 0
    for i in range(0, len(x)):
        sum = sum + x[i]*x[i]
    return sum

# Calculating the error of a data point
def error(x, y, a):
    return np.sqrt((1/sum(x))*(sum(y-a*x)/(len(x)-1)))

# Removing data points that are larger than the time out
def remove_time_out(datacolumn):
    datacolumn = [i for i in datacolumn if i < 10000]
    return datacolumn

# Reading and plotting all data
def scatter(nr_con):
    fig = figure(figsize=(10, 10))
    ax1 = fig.add_subplot(1, 1, 1)

    if nr_con:
        data1 = pd.read_csv("data_plotting/nrcon/1_nrcon_scatter.csv", header=None, delimiter=';')
        rt1old = data1[1].tolist()
        rt1 = [int(x) / 1000000 for x in rt1old]
        rt1 = remove_time_out(rt1)
        x1 = np.repeat(1, len(rt1))

        data2 = pd.read_csv("data_plotting/nrcon/2_nrcon_scatter.csv", header=None, delimiter=';')
        rt2old = data2[1].tolist()
        rt2 = [int(x) / 1000000 for x in rt2old]
        rt2 = remove_time_out(rt2)
        x2 = np.repeat(2, len(rt2))

        data3 = pd.read_csv("data_plotting/nrcon/3_nrcon_scatter.csv", header=None, delimiter=';')
        rt3old = data3[1].tolist()
        rt3 = [int(x) / 1000000 for x in rt3old]
        rt3 = remove_time_out(rt3)
        x3 = np.repeat(3, len(rt3))

        data4 = pd.read_csv("data_plotting/nrcon/4_nrcon_scatter.csv", header=None, delimiter=';')
        rt4old = data4[1].tolist()
        rt4 = [int(x) / 1000000 for x in rt4old]
        rt4 = remove_time_out(rt4)
        x4 = np.repeat(4, len(rt4))

        data5 = pd.read_csv("data_plotting/nrcon/5_nrcon_scatter.csv", header=None, delimiter=';')
        rt5old = data5[1].tolist()
        rt5 = [int(x) / 1000000 for x in rt5old]
        rt5 = remove_time_out(rt5)
        x5 = np.repeat(5, len(rt5))

        data6 = pd.read_csv("data_plotting/nrcon/6_nrcon_scatter.csv", header=None, delimiter=';')
        rt6old = data6[1].tolist()
        rt6 = [int(x) / 1000000 for x in rt6old]
        rt6 = remove_time_out(rt6)
        x6 = np.repeat(6, len(rt6))

        ax1.scatter(x1, rt1, marker='x', color='blue')
        ax1.scatter(x2, rt2, marker='x', color='blue')
        ax1.scatter(x3, rt3, marker='x', color='blue')
        ax1.scatter(x4, rt4, marker='x', color='blue')
        ax1.scatter(x5, rt5, marker='x', color='blue')
        ax1.scatter(x6, rt6, marker='x', color='blue')

        ax1.set_title("Run time per number of connectives", fontsize=24)
        ax1.set_xlabel('Nr. of connectives', fontsize=22)
        plt.xticks(np.arange(0, 7, 1))

    else:
        data0 = pd.read_csv("data_plotting/modal/modal0_changed.csv", header=None, delimiter=';')
        rt0old = data0[0].tolist()
        rt0 = [int(x) / 1000000 for x in rt0old]
        x0 = np.repeat(0, len(rt0))

        data1 = pd.read_csv("data_plotting/modal/modal1_changed.csv", header=None, delimiter=';')
        rt1old = data1[0].tolist()
        rt1 = [int(x) / 1000000 for x in rt1old]
        x1 = np.repeat(1, len(rt1))

        data2 = pd.read_csv("data_plotting/modal/modal2_changed.csv", dtype='float', header=None, delimiter=';')
        rt2old = data2[0].tolist()
        rt2 = [int(x) / 1000000 for x in rt2old]
        x2 = np.repeat(2, len(rt2))

        data3 = pd.read_csv("data_plotting/modal/modal3_changed.csv", header=None, delimiter=';')
        rt3old = data3[0].tolist()
        rt3 = [int(x) / 1000000 for x in rt3old]
        x3 = np.repeat(3, len(rt3))

        data4 = pd.read_csv("data_plotting/modal/modal4_changed.csv", header=None, delimiter=';')
        rt4old = data4[0].tolist()
        rt4 = [int(x) / 1000000 for x in rt4old]
        x4 = np.repeat(4, len(rt4))

        ax1.scatter(x0, rt0, marker='x', color='blue')
        ax1.scatter(x1, rt1, marker='x', color='blue')
        ax1.scatter(x2, rt2, marker='x', color='blue')
        ax1.scatter(x3, rt3, marker='x', color='blue')
        ax1.scatter(x4, rt4, marker='x', color='blue')

        ax1.set_title("Run time per modal depth", fontsize=24)
        ax1.set_xlabel('Modal depth', fontsize=22)
        plt.xticks(np.arange(0, 5, 1))

    ax1.set_ylabel('Time [ms]', fontsize=22)
    ax1.grid(which='major', linestyle='-', linewidth='0.5', color='red')
    ax1.tick_params(axis='x', labelsize=18)
    ax1.tick_params(axis='y', labelsize=18)
    ax1.minorticks_on()
    ax1.grid(which='minor', linestyle=':', linewidth='0.5', color='black')

    plt.show()

# Determining and removing outliers
def filter(datacolumn, nr_con):
    # exclude outliers based on the time limit
    datacolumn = [i for i in datacolumn if i < 10000]

    sorted(datacolumn)
    Q1, Q3 = np.percentile(datacolumn , [25,75])
    IQR = Q3 - Q1
    low = Q1 - (1.5 * IQR)
    up = Q3 + (1.5 * IQR)
    datacolumn = [i for i in datacolumn if i < up]
    datacolumn = [i for i in datacolumn if i > low]

    # Additional command to remove other outliers
    if nr_con:
        datacolumn = [i for i in datacolumn if i < 10000]
    else:
        datacolumn = [i for i in datacolumn if i < 11]

    return datacolumn

# Plotting the data without any outliers
def scatter_outliers(nr_con):
    fig = figure(figsize=(10, 10))
    ax1 = fig.add_subplot(1, 1, 1)

    if nr_con:
        data1 = pd.read_csv("data_plotting/nrcon/1_nrcon_scatter.csv", header=None, delimiter=';')
        rt1old = data1[1].tolist()
        rt1 = [int(x) / 1000000 for x in rt1old]
        rt1 = filter(rt1, nr_con)
        x1 = np.repeat(1, len(rt1))

        data2 = pd.read_csv("data_plotting/nrcon/2_nrcon_scatter.csv", header=None, delimiter=';')
        rt2old = data2[1].tolist()
        rt2 = [int(x) / 1000000 for x in rt2old]
        rt2 = filter(rt2, nr_con)
        x2 = np.repeat(2, len(rt2))

        data3 = pd.read_csv("data_plotting/nrcon/3_nrcon_scatter.csv", header=None, delimiter=';')
        rt3old = data3[1].tolist()
        rt3 = [int(x) / 1000000 for x in rt3old]
        rt3 = filter(rt3, nr_con)
        x3 = np.repeat(3, len(rt3))

        data4 = pd.read_csv("data_plotting/nrcon/4_nrcon_scatter.csv", header=None, delimiter=';')
        rt4old = data4[1].tolist()
        rt4 = [int(x) / 1000000 for x in rt4old]
        rt4 = filter(rt4, nr_con)
        x4 = np.repeat(4, len(rt4))

        data5 = pd.read_csv("data_plotting/nrcon/5_nrcon_scatter.csv", header=None, delimiter=';')
        rt5old = data5[1].tolist()
        rt5 = [int(x) / 1000000 for x in rt5old]
        rt5 = filter(rt5, nr_con)
        x5 = np.repeat(5, len(rt5))

        data6 = pd.read_csv("data_plotting/nrcon/6_nrcon_scatter.csv", header=None, delimiter=';')
        rt6old = data6[1].tolist()
        rt6 = [int(x) / 1000000 for x in rt6old]
        rt6 = filter(rt6, nr_con)
        x6 = np.repeat(6, len(rt6))

        ax1.scatter(x1, rt1, marker='x', color='blue')
        ax1.scatter(x2, rt2, marker='x', color='blue')
        ax1.scatter(x3, rt3, marker='x', color='blue')
        ax1.scatter(x4, rt4, marker='x', color='blue')
        ax1.scatter(x5, rt5, marker='x', color='blue')
        ax1.scatter(x6, rt6, marker='x', color='blue')

        ax1.set_title("Run time per number of connectives \n without outliers", fontsize=24)
        ax1.set_xlabel('Nr. of connectives', fontsize=22)
        plt.xticks(np.arange(0, 7, 1))

    else:
        data0 = pd.read_csv("data_plotting/modal/modal0_changed.csv", header=None, delimiter=';')
        rt0old = data0[0].tolist()
        rt0 = [int(x) / 1000000 for x in rt0old]
        rt0 = filter(rt0, nr_con)
        x0 = np.repeat(0, len(rt0))

        data1 = pd.read_csv("data_plotting/modal/modal1_changed.csv", header=None, delimiter=';')
        rt1old = data1[0].tolist()
        rt1 = [int(x) / 1000000 for x in rt1old]
        rt1 = filter(rt1, nr_con)
        x1 = np.repeat(1, len(rt1))

        data2 = pd.read_csv("data_plotting/modal/modal2_changed.csv", header=None, delimiter=';')
        rt2old = data2[0].tolist()
        rt2 = [int(x) / 1000000 for x in rt2old]
        rt2 = filter(rt2, nr_con)
        x2 = np.repeat(2, len(rt2))

        data3 = pd.read_csv("data_plotting/modal/modal3_changed.csv", header=None, delimiter=';')
        rt3old = data3[0].tolist()
        rt3 = [int(x) / 1000000 for x in rt3old]
        rt3 = filter(rt3, nr_con)
        x3 = np.repeat(3, len(rt3))

        data4 = pd.read_csv("data_plotting/modal/modal4_changed.csv", header=None, delimiter=';')
        rt4old = data4[0].tolist()
        rt4 = [int(x) / 1000000 for x in rt4old]
        rt4 = filter(rt4, nr_con)
        x4 = np.repeat(4, len(rt4))

        ax1.scatter(x0, rt0, marker='x', color='blue')
        ax1.scatter(x1, rt1, marker='x', color='blue')
        ax1.scatter(x2, rt2, marker='x', color='blue')
        ax1.scatter(x3, rt3, marker='x', color='blue')
        ax1.scatter(x4, rt4, marker='x', color='blue')

        ax1.set_title("Run time per modal depth \n without outliers", fontsize=24)
        ax1.set_xlabel('Modal depth', fontsize=22)
        plt.xticks(np.arange(0, 5, 1))

    ax1.set_ylabel('Time [ms]', fontsize=22)
    ax1.grid(which='major', linestyle='-', linewidth='0.5', color='red')
    ax1.tick_params(axis='x', labelsize=18)
    ax1.tick_params(axis='y', labelsize=18)
    ax1.minorticks_on()
    ax1.grid(which='minor', linestyle=':', linewidth='0.5', color='black')

    plt.show()

# Plot the run time and the modal depth for each complexity
def plot(nr_con, plot_time):

    if nr_con:
        complexities_t = [1, 2, 3, 4, 5, 6]
        complexities_nt = [1, 2, 3, 4, 5, 6]

        # The averages + standard deviations for each complexity
        if plot_time:
            # tautologies
            t_time = [5.7, 7.3, 6.9, 6.7, 7.3, 10.4]
            t_time_error = [0.8, 1.2, 0.7, 1.1, 2.1, 5.5]

            # non-tautologies
            nt_time = [7.4, 6.9, 7.3, 7.2, 7.1, 8.5]
            nt_time_error = [2.2, 1.1, 1.7, 1.3, 2.5, 5.2]

        else:
            # tautologies
            t_ram = [76.5, 548.1, 508.0, 417.4, 417.3, 431.5]
            t_ram_error = [0.0, 0.2, 71.6, 35.0, 36.2, 43.2]

            # non-tautologies
            nt_ram = [85.3, 543.5, 504.3, 412.4, 413.1, 460.8]
            nt_ram_error = [10.6, 45.9, 72.4, 32.8, 50.6, 19.0]

    else:
        complexities_t = [0, 1, 2, 3]
        complexities_nt = [0, 1, 2, 3, 4]

        if plot_time:
            # tautologies
            t_time = [6.6, 7.3, 6.6, 7.8]
            t_time_error = [0.6, 0.9, 0.9, 0.4]

            # non-tautologies
            nt_time = [6.6, 7.0, 7.1, 6.6, 7.2]
            nt_time_error = [0.8, 0.8, 1.0, 1.4, 0.1]

        else:
            # tautologies
            t_ram = [436.3, 492.6, 422.9, 461.3]
            t_ram_error = [193.9, 69.2, 35.1, 7.4]

            # non-tautologies
            nt_ram = [441.5, 466.6, 463.0, 386.2, 463.0]
            nt_ram_error = [173.0, 106.1, 63.4, 1.3, 11.6]

    # Plotting the data points
    fig = figure(figsize=(10, 10))
    frame = fig.add_subplot(1, 1, 1)

    if nr_con:
        if plot_time:
            frame.plot(complexities_t, t_time,  "x", color='blue', label='tautologies')
            frame.errorbar(complexities_t, t_time, t_time_error, [0,0,0,0,0,0], fmt="none", color='blue')
            frame.fill_between(complexities_t, np.array(t_time)-np.array(t_time_error),
                               np.array(t_time)+np.array(t_time_error), alpha=0.2)

            frame.plot(complexities_nt, nt_time,  "x", color='red', label='non-tautologies')
            frame.errorbar(complexities_nt, nt_time, nt_time_error, [0,0,0,0,0,0], fmt="none", color='red')
            frame.fill_between(complexities_nt, np.array(nt_time)-np.array(nt_time_error),
                               np.array(nt_time)+np.array(nt_time_error), alpha=0.2)

            frame.set_title("Run time per number of connectives", fontsize=24)
            plt.yticks(np.arange(0, 21, 2.5))
            frame.set_ylabel('Time [ms]', fontsize=22)

        else:
            frame.plot(complexities_t, t_ram, "x", color='blue', label='tautologies')
            frame.errorbar(complexities_t, t_ram, t_ram_error, [0,0,0,0,0,0], fmt="none", color='blue')

            frame.fill_between(complexities_t, np.array(t_ram) - np.array(t_ram_error),
                               np.array(t_ram) + np.array(t_ram_error), alpha=0.2)

            frame.plot(complexities_nt, nt_ram, "x", color='red', label='non-tautologies')
            frame.errorbar(complexities_nt, nt_ram, nt_ram_error, [0,0,0,0,0,0], fmt="none", color='red')
            frame.fill_between(complexities_nt, np.array(nt_ram) - np.array(nt_ram_error),
                               np.array(nt_ram) + np.array(nt_ram_error), alpha=0.2)

            frame.set_title("RAM usage per number of connectives", fontsize=24)
            frame.set_ylabel('RAM usage [kB]', fontsize=22)
            plt.yticks(np.arange(0, 801, 100))

        frame.set_xlabel('Nr. of connectives', fontsize=22)
        frame.legend(loc=2, prop={'size': 18})
        plt.xticks(np.arange(0, 7, 1))

    else:
        if plot_time:
            frame.plot(complexities_t, t_time,  "x", color='blue', label='tautologies')
            frame.errorbar(complexities_t, t_time, t_time_error, [0,0,0,0], fmt="none", color='blue')
            frame.fill_between(complexities_t, np.array(t_time)-np.array(t_time_error),
                               np.array(t_time)+np.array(t_time_error), alpha=0.2)

            frame.plot(complexities_nt, nt_time,  "x", color='red', label='non-tautologies')
            frame.errorbar(complexities_nt, nt_time, nt_time_error, [0,0,0,0,0], fmt="none", color='red')
            frame.fill_between(complexities_nt, np.array(nt_time)-np.array(nt_time_error),
                               np.array(nt_time)+np.array(nt_time_error), alpha=0.2)

            frame.set_title("Run time per modal depth", fontsize=24)
            frame.set_ylabel('Time [ms]', fontsize=22)

            plt.yticks(np.arange(4, 10, 0.5))

        else:
            frame.plot(complexities_t, t_ram, "x", color='blue', label='tautologies')
            frame.errorbar(complexities_t, t_ram, t_ram_error, [0,0,0,0], fmt="none", color='blue')
            frame.fill_between(complexities_t, np.array(t_ram) - np.array(t_ram_error),
                               np.array(t_ram) + np.array(t_ram_error), alpha=0.2)

            frame.plot(complexities_nt, nt_ram, "x", color='red', label='non-tautologies')
            frame.errorbar(complexities_nt, nt_ram, nt_ram_error, [0,0,0,0,0], fmt="none", color='red')
            frame.fill_between(complexities_nt, np.array(nt_ram) - np.array(nt_ram_error),
                               np.array(nt_ram) + np.array(nt_ram_error), alpha=0.2)

            frame.set_title("RAM usage per modal depth", fontsize=24)
            frame.set_ylabel('RAM usage [kB]', fontsize=22)

        frame.set_xlabel('Modal depth', fontsize=22)
        plt.xticks(np.arange(0, 5, 1))
        frame.legend(loc=1, prop={'size': 18})

    frame.grid(which='major', linestyle='-', linewidth='0.5', color='red')
    frame.tick_params(axis='x', labelsize=18)
    frame.tick_params(axis='y', labelsize=18)
    frame.minorticks_on()
    frame.grid(which='minor', linestyle=':', linewidth='0.5', color='black')

    show()

# Choose what to plot
nr_connectives = False # Plotting the data for the number of connectives (True) or for the modal depth (False)
plot_run_time = False # Plotting the run time (True) or the RAM usage (False)

# Run the code fragment for each of the plots
scatter(nr_connectives)
scatter_outliers(nr_connectives)
plot(nr_connectives, plot_run_time)

