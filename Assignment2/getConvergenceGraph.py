# -*- coding: utf-8 -*-
"""
Created on Tue Oct  1 21:23:01 2013

@author: pathos
"""

import matplotlib.pyplot as plt

f = open('convergenceOnMC.data', 'r')

data = f.read().splitlines()
for i in range(len(data)):
    data[i] = int(data[i])
#print data
plt.plot(data)
plt.show()