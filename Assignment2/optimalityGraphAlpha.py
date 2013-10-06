# -*- coding: utf-8 -*-
"""
Created on Tue Oct  1 21:23:01 2013

@author: pathos
"""

import matplotlib.pyplot as plt

f = open('optimalityQ_0.1_0.1_0.9.data', 'r')
f2 = open('optimalityQ_0.1_0.2_0.9.data', 'r')
f3 = open('optimalityQ_0.1_0.3_0.9.data', 'r')
f4 = open('optimalityQ_0.1_0.4_0.9.data', 'r')
f5 = open('optimalityQ_0.1_0.5_0.9.data', 'r')

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
    data[i] = float(data[i])
    data2[i] = float(data2[i])
    data3[i] = float(data3[i])
    data4[i] = float(data4[i])
    data5[i] = float(data5[i])
    
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
plt.plot(ddata, 'r', label = '0.1')
plt.ylim([0,1])
plt.ylabel('optimality')
plt.xlabel('iterations x 100')
plt.legend(loc=4)
#plt.figure(2)
plt.plot(ddata2, 'b', label = '0.2')
plt.legend(loc=4)
#plt.figure(3)
plt.plot(ddata3, 'g', label = '0.3')
plt.legend(loc=4)
#plt.figure(4)
plt.plot(ddata4, 'm', label = '0.4')
plt.legend(loc=4)
#plt.figure(5)
plt.plot(ddata5, 'c', label = '0.5')
plt.legend(loc=4)
plt.show()