# -*- coding: utf-8 -*-
"""
Created on Wed Oct 16 19:53:12 2013

@author: xiln
"""
import glob
from os.path import basename
#==============================================================================
# 
# testoutput parser for structured graphing
#==============================================================================

datafiles = glob.glob("*.data")
print datafiles

method = range(len(datafiles))
actionselect = range(len(datafiles))
nr_preds = range(len(datafiles))
nr_runs =  range(len(datafiles))
parameter = range(len(datafiles))
alpha = range(len(datafiles))
gamma = range(len(datafiles))

for i in range(len(datafiles)): 
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