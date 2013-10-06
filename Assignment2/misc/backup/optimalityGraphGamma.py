# -*- coding: utf-8 -*-
"""
Created on Tue Oct  1 21:23:01 2013

@author: pathos
"""

import matplotlib.pyplot as plt

f = open('optimality_0.1_0.2_0.1.data', 'r')
f2 = open('optimality_0.1_0.2_0.2.data', 'r')
f3 = open('optimality_0.1_0.2_0.5.data', 'r')
f4 = open('optimality_0.1_0.2_0.7.data', 'r')
f5 = open('optimality_0.1_0.2_0.9.data', 'r')

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
data = data[0:100:1]
data2 = data2[0:100:1]
data3 = data3[0:100:1]
data4 = data4[0:100:1]
data5 = data5[0:100:1]
#print len(data)
for i in range(len(data)):
    data[i] = float(data[i])
    data2[i] = float(data2[i])
    data3[i] = float(data3[i])
    data4[i] = float(data4[i])
    data5[i] = float(data5[i])
    
#for j in range(len(data)/100):
#    data[j] = sum(data[j*100:j*100+100])/100
#    data2[j] = sum(data2[j*100:j*100+100])/100
#    data3[j] = sum(data3[j*100:j*100+100])/100
#    data4[j] = sum(data4[j*100:j*100+100])/100
#    data5[j] = sum(data5[j*100:j*100+100])/100
#print data
plt.figure(1)
plt.plot(data, 'r', label = '0.1')
plt.ylim([0,1])
plt.ylabel('episode size')
plt.xlabel('iterations x 200')
plt.legend(loc=2)
#plt.figure(2)
plt.plot(data2, 'b', label = '0.2')
plt.ylim([0,1])
plt.ylabel('episode size')
plt.xlabel('iterations x 200')
plt.legend(loc=2)
#plt.figure(3)
plt.plot(data3, 'g', label = '0.5')
plt.ylim([0,1])
plt.ylabel('episode size')
plt.xlabel('iterations x 200')
plt.legend(loc=2)
#plt.figure(4)
plt.plot(data4, 'm', label = '0.7')
plt.ylim([0,1])
plt.ylabel('episode size')
plt.xlabel('iterations x 200')
plt.legend(loc=2)
#plt.figure(5)
plt.plot(data5, 'c', label = '0.9')
plt.ylim([0,1])
plt.ylabel('episode size')
plt.xlabel('iterations x 200')
plt.legend(loc=2)
plt.show()