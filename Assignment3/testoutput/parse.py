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
method = range(nrfiles)
actionselect = range(nrfiles)
nr_preds = range(nrfiles)
nr_runs =  range(nrfiles)
parameter = range(nrfiles)
alpha = range(nrfiles)
gamma = range(nrfiles)

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
    method[i] = tokens[0]
    actionselect[i] = tokens[1]
    nr_preds[i] = tokens[2]
    nr_runs[i] = tokens[3]
    # parameter for actionselect(epsilon or tau)
    parameter[i] = tokens[4]
    alpha[i] = tokens[5]
    gamma[i] = tokens[6]
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
        ddata[i][k] = sum(data[i][k*100:j*100+100])/100
    # Plotting
    #print data
    plt.figure(i)
    plt.plot(ddata[i], colors[i], label = method[i]+' '+actionselect[i])
    plt.ylim([0,8000])
    plt.ylabel('catupre times')
    plt.xlabel('iterations x 100')
    plt.legend(loc=4)

plt.show()