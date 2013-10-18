# -*- coding: utf-8 -*-
"""
Created on Wed Oct 16 19:53:12 2013

@author: xiln
"""
import glob
import matplotlib.pyplot as plt
from os.path import basename
import re
#==============================================================================
# 
# testoutput parser for structured graphing
#==============================================================================

datafiles = glob.glob("*.data2")
nrfiles = len(datafiles)

# parameters
parameter = range(nrfiles)
alpha = range(nrfiles)
gamma = range(nrfiles)
nr_preds = range(nrfiles)
nr_runs =  range(nrfiles)
method = range(nrfiles)
actionselect = range(nrfiles)
endstate = range(nrfiles)

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
    endstate[i] = tokens[7]
    # read data in
    files[i] = open(datafiles[i], 'r')
    data[i] = files[i].read().splitlines()
    files[i].close()
    data[i] = data[i][0:int(nr_runs[i]):1]
    for j in range(len(data[i])):
        data[i][j] = float(data[i][j])
    averaging_step = 100
    # Averaging
    ddata[i] = range(int(nr_runs[i])/averaging_step)
    for k in range(len(data[i])/averaging_step):
        ddata[i][k] = sum(data[i][k*averaging_step:k*averaging_step + averaging_step])/averaging_step
    # Plotting
    step_to_show = 20
    plt.figure(1)
    #plt.plot(ddata[i][len(ddata[i]) - step_to_show:len(ddata[i])], colors[i], label = endstate[i])
    #plt.plot(data[i][len(data[i]) - step_to_show:len(data[i])], colors[i], label = alpha[i])
    #plt.ylim([0,50])
    #plt.ylabel('episode size')
    #plt.xlabel('last 200 iterations')
    #plt.legend()    
    
    #plt.figure(2)
    plt.plot(ddata[i], colors[i], label = re.sub('\.data2$', '', endstate[i]))
    #plt.plot(data[i], colors[i], label = alpha[i])
    #plt.ylim([0,500])
    plt.ylabel('episode size')
    plt.xlabel('iterations')
    plt.legend()
    
    #plt.figure(3)
    #plt.plot(ddata[i][0:step_to_show], colors[i], label = endstate[i])
    #plt.plot(data[i][0:step_to_show], colors[i], label = alpha[i])
    #plt.ylim([0,500])
    #plt.ylabel('episode size')
    #plt.xlabel('first 20 iterations')
    #plt.legend()

plt.show()