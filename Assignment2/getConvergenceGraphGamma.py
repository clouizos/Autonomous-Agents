# -*- coding: utf-8 -*-
"""
Created on Tue Oct  1 21:23:01 2013

@author: pathos
"""

import matplotlib.pyplot as plt

f = open('convQLearning_0.1_0.2_0.1.data', 'r')
f2 = open('convQLearning_0.1_0.2_0.2.data', 'r')
f3 = open('convQLearning_0.1_0.2_0.5.data', 'r')
f4 = open('convQLearning_0.1_0.2_0.7.data', 'r')
f5 = open('convQLearning_0.1_0.2_0.9.data', 'r')

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
    
ddata = range(200)
ddata2 = range(200)
ddata3 = range(200)
ddata4 = range(200)
ddata5 = range(200)
    
for j in range(len(data)/100):
    ddata[j] = sum(data[j*100:j*100+100])/100
    ddata2[j] = sum(data2[j*100:j*100+100])/100
    ddata3[j] = sum(data3[j*100:j*100+100])/100
    ddata4[j] = sum(data4[j*100:j*100+100])/100
    ddata5[j] = sum(data5[j*100:j*100+100])/100
    
#print data
plt.figure(1)
plt.plot(ddata[80:100], 'r', label = '0.1')
plt.ylim([4,7])
plt.ylabel('episode size')
plt.xlabel('last 20 iterations x 100')
plt.legend()
#plt.figure(2)
plt.plot(ddata2[80:100], 'b', label = '0.2')
plt.legend()
#plt.figure(3)
plt.plot(ddata3[80:100], 'g', label = '0.5')
plt.legend()
#plt.figure(4)
plt.plot(ddata4[80:100], 'm', label = '0.7')
plt.legend()
#plt.figure(5)
plt.plot(ddata5[80:100], 'c', label = '0.9')
plt.legend()


plt.figure(6)
plt.plot(ddata, 'r', label = '0.1')
plt.ylim([0,100])
plt.ylabel('episode size')
plt.xlabel('iterations x 100')
plt.legend()
#plt.figure(2)
plt.plot(ddata2, 'b', label = '0.2')
plt.legend()
#plt.figure(3)
plt.plot(ddata3, 'g', label = '0.5')
plt.legend()
#plt.figure(4)
plt.plot(ddata4, 'm', label = '0.7')
plt.legend()
#plt.figure(5)
plt.plot(ddata5, 'c', label = '0.9')
plt.legend()

plt.figure(7)
plt.plot(ddata[0:20], 'r', label = '0.1')
plt.ylim([0,200])
plt.ylabel('episode size')
plt.xlabel('first 20 iterations x 100')
plt.legend()
#plt.figure(2)
plt.plot(ddata2[0:20], 'b', label = '0.2')
plt.legend()
#plt.figure(3)
plt.plot(ddata3[0:20], 'g', label = '0.5')
plt.legend()
#plt.figure(4)
plt.plot(ddata4[0:20], 'm', label = '0.7')
plt.legend()
#plt.figure(5)
plt.plot(ddata5[0:20], 'c', label = '0.9')
plt.legend()
plt.show()