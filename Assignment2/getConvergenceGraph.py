# -*- coding: utf-8 -*-
"""
Created on Tue Oct  1 21:23:01 2013

@author: pathos
"""

import matplotlib.pyplot as plt

f = open('convergenceOnMC.data', 'r')
f2 = open('convergenceOffMC.data', 'r')
f3 = open('convergenceQLearning.data', 'r')

data = f.read().splitlines()
data2 = f2.read().splitlines()
data3 = f3.read().splitlines()
f.close()
f2.close()
f3.close()
#print len(data)
data = data[0:10000:1]
data2 = data2[0:10000:1]
data3 = data3[0:10000:1]
#print len(data)
for i in range(len(data)):
    data[i] = int(data[i])
    data2[i] = int(data2[i])
    data3[i] = int(data3[i])
#print data
plt.figure(1)
plt.plot(data, 'g', label = 'On Policy MC')
plt.ylim([0,500])
plt.legend()
plt.figure(2)
plt.plot(data2, 'b', label = 'Off Policy MC')
plt.ylim([0,500])
plt.legend()
plt.figure(3)
plt.plot(data3, 'r', label = 'Q-Learning')
plt.ylim([0,500])
plt.legend()
plt.show()