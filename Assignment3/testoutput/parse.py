# -*- coding: utf-8 -*-
"""
Created on Wed Oct 16 19:53:12 2013

@author: xiln
"""
import glob
import matplotlib.pyplot as plt
from os.path import basename
#==============================================================================
# 
# testoutput parser for structured graphing
#==============================================================================

datafiles = glob.glob("*.data")
nrfiles = len(datafiles)

# parameters
parameter = range(nrfiles)
alpha = range(nrfiles)
gamma = range(nrfiles)
nr_preds = range(nrfiles)
nr_runs =  range(nrfiles)
method = range(nrfiles)
actionselect = range(nrfiles)

#input
files = range(nrfiles)
# data parse
data = range(nrfiles)
ddata = range(nrfiles)

colors = ['r','b', 'g', 'c', 'm', 'y', 'k']

for i in range(nrfiles): 
    datafile = basename(datafiles[i])
    tokens = datafile.split('_')
    # get the parameters for each file
    # parameter for actionselect(epsilon or tau)
    parameter[i] = tokens[0]
    alpha[i] = tokens[1]
    gamma[i] = tokens[2]
    nr_preds[i] = tokens[3]
    nr_runs[i] = tokens[4]
    method[i] = tokens[5]
    actionselect[i] = tokens[6]
    # read data in
    files[i] = open(datafiles[i], 'r')
    data[i] = files[i].read().splitlines()
    files[i].close()
    data[i] = data[i][0:int(nr_runs[i]):1]
    for j in range(len(data[i])):
        data[i][j] = float(data[i][j])
    # Averaging
    ddata[i] = range(int(nr_runs[i])/100)
    for k in range(len(data[i])/100):
        ddata[i][k] = sum(data[i][k*100:k*100+100])/100
    # Plotting
    #print data
    plt.figure()
    plt.plot(ddata[i][80:100], colors[i], label = alpha[i])
    plt.ylim([0,50])
    plt.ylabel('episode size')
    plt.xlabel('last 20 iterations x 100')
    plt.legend()    
    
    plt.figure(6)
    plt.plot(ddata[i], colors[i], label = alpha[i])
    plt.ylim([0,500])
    plt.ylabel('episode size')
    plt.xlabel('iterations x 100')
    plt.legend()
    
    plt.figure(7)
    plt.plot(ddata[i][0:20], colors[i], label = alpha[i])
    plt.ylim([0,500])
    plt.ylabel('episode size')
    plt.xlabel('first 20 iterations x 100')
    plt.legend()

plt.show()