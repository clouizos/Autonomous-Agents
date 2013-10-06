# -*- coding: utf-8 -*-
"""
Created on Tue Oct  1 21:23:01 2013

@author: pathos
"""

import matplotlib.pyplot as plt

f = open('convQ_0.1_0.2_0.1softmax.data', 'r')
f2 = open('convQ_0.01_0.2_0.1softmax.data', 'r')
f3 = open('convQ_0.001_0.2_0.1softmax.data', 'r')
f4 = open('convQ_1.0E-4_0.2_0.1softmax.data', 'r')
f5 = open('convQ_1.0E-5_0.2_0.1softmax.data', 'r')
f6 = open('convQ_0.1_0.2_0.1egreedy.data', 'r')

data = f.read().splitlines()
data2 = f2.read().splitlines()
data3 = f3.read().splitlines()
data4 = f4.read().splitlines()
data5 = f5.read().splitlines()
data6 = f6.read().splitlines()
f.close()
f2.close()
f3.close()
f4.close()
f5.close()
f6.close()
#print len(data)
data = data[0:20000:1]
data2 = data2[0:20000:1]
data3 = data3[0:20000:1]
data4 = data4[0:20000:1]
data5 = data5[0:20000:1]
data6 = data6[0:20000:1]

#print len(data)
for i in range(len(data)):
    data[i] = float(data[i])
    data2[i] = float(data2[i])
    data3[i] = float(data3[i])
    data4[i] = float(data4[i])
    data5[i] = float(data5[i])
    data6[i] = float(data6[i])
    
ddata = range(200)
ddata2 = range(200)
ddata3 = range(200)
ddata4 = range(200)
ddata5 = range(200)
ddata6 = range(200)
    
for j in range(len(data)/100):
    ddata[j] = sum(data[j*100:j*100+100])/100
    ddata2[j] = sum(data2[j*100:j*100+100])/100
    ddata3[j] = sum(data3[j*100:j*100+100])/100
    ddata4[j] = sum(data4[j*100:j*100+100])/100
    ddata5[j] = sum(data5[j*100:j*100+100])/100
    ddata6[j] = sum(data6[j*100:j*100+100])/100
    
#print data
plt.figure(1)
plt.plot(ddata[80:100], 'r', label = '0.1')
plt.ylim([4,60])
plt.ylabel('episode size')
plt.xlabel('last 20 iterations x 100')
plt.legend()
#plt.figure(2)
plt.plot(ddata2[80:100], 'b', label = '0.01')
plt.legend()
#plt.figure(3)
plt.plot(ddata3[80:100], 'g', label = '0.001')
plt.legend()
#plt.figure(4)
plt.plot(ddata4[80:100], 'm', label = '1x10^-4')
plt.legend()
#plt.figure(5)
plt.plot(ddata5[80:100], 'c', label = '1x10^-5')
plt.legend()
#plt.figure(6)
plt.plot(ddata6[80:100], 'k', label = 'e:0.1')
plt.legend()


plt.figure(6)
plt.plot(ddata, 'r', label = '0.1')
plt.ylim([0,100])
plt.ylabel('episode size')
plt.xlabel('iterations x 100')
plt.legend()
#plt.figure(2)
plt.plot(ddata2, 'b', label = '0.01')
plt.legend()
#plt.figure(3)
plt.plot(ddata3, 'g', label = '0.001')
plt.legend()
#plt.figure(4)
plt.plot(ddata4, 'm', label = '1x10^-4')
plt.legend()
#plt.figure(5)
plt.plot(ddata5, 'c', label = '1x10^-5')
plt.legend()
#plt.figure(6)
plt.plot(ddata6, 'k', label = 'e:0.1')
plt.legend()

plt.figure(7)
plt.plot(ddata[0:20], 'r', label = '0.1')
plt.ylim([0,250])
plt.ylabel('episode size')
plt.xlabel('first 20 iterations x 100')
plt.legend()
#plt.figure(2)
plt.plot(ddata2[0:20], 'b', label = '0.01')
plt.legend()
#plt.figure(3)
plt.plot(ddata3[0:20], 'g', label = '0.001')
plt.legend()
#plt.figure(4)
plt.plot(ddata4[0:20], 'm', label = '1x10^-4')
plt.legend()
#plt.figure(5)
plt.plot(ddata5[0:20], 'c', label = '1x10^-5')
plt.legend()
#plt.figure(6)
plt.plot(ddata6[0:20], 'k', label = 'e:0.1')
plt.legend()
plt.show()