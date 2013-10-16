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

datafile = basename(datafiles[0])
tokens = datafile.split('_')
method = tokens[0]
actionselect = tokens[1]
nr_preds = tokens[2]
nr_runs = tokens[3]
# parameter for actionselect(epsilon or tau)
parameter = tokens[4]
alpha = tokens[5]
gamma = tokens[6]