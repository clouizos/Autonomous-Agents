# -*- coding: utf-8 -*-
"""
Created on Tue Oct  1 21:23:01 2013

@author: pathos
"""

import matplotlib.pyplot as plt

f = open('convergenceQLearning_0.1_0.1_0.9.data', 'r')
f2 = open('convergenceQLearning_0.1_0.2_0.9.data', 'r')
f3 = open('convergenceQLearning_0.1_0.3_0.9.data', 'r')
f4 = open('convergenceQLearning_0.1_0.4_0.9.data', 'r')
f5 = open('convergenceQLearning_0.1_0.5_0.9.data', 'r')

data = f.read().splitlines()
data2 = f2.read().splitlines()
data3 = f3.read().splitlines()
data4 = f4.read().splitlines()
data5 = f5.read().splitlines()
f.close()
f2.close()
f3.close()
f4.close()
f5.close()
#print len(data)
data = data[0:20000:1]
data2 = data2[0:20000:1]
data3 = data3[0:20000:1]
data4 = data4[0:20000:1]
data5 = data5[0:20000:1]
#print len(data)
for i in range(len(data)):
    data[i] = int(data[i])
    data2[i] = int(data2[i])
    data3[i] = int(data3[i])
    data4[i] = int(data4[i])
    data5[i] = int(data5[i])
#print data
plt.figure(1)
plt.plot(data, 'r', label = '0.1')
plt.ylim([0,50])
plt.ylabel('episode size')
plt.xlabel('iterations')
plt.legend()
plt.figure(2)
plt.plot(data2, 'b', label = '0.2')
plt.ylim([0,50])
plt.ylabel('episode size')
plt.xlabel('iterations')
plt.legend()
plt.figure(3)
plt.plot(data3, 'g', label = '0.3')
plt.ylim([0,50])
plt.ylabel('episode size')
plt.xlabel('iterations')
plt.legend()
plt.figure(4)
plt.plot(data4, 'm', label = '0.4')
plt.ylim([0,50])
plt.ylabel('episode size')
plt.xlabel('iterations')
plt.legend()
plt.figure(5)
plt.plot(data5, 'c', label = '0.5')
plt.ylim([0,50])
plt.ylabel('episode size')
plt.xlabel('iterations')
plt.legend()
plt.show()