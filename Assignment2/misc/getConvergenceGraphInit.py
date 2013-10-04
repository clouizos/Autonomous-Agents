# -*- coding: utf-8 -*-
"""
Created on Fri Oct  4 22:30:48 2013

@author: xiln
"""

# -*- coding: utf-8 -*-
"""
Created on Tue Oct  1 21:23:01 2013

@author: pathos
"""

import matplotlib.pyplot as plt

f = open('convergenceQLearning_0.1_0.2_0.9init0.data', 'r')
f2 = open('convergenceQLearning_0.1_0.2_0.9init10.data', 'r')
f3 = open('convergenceQLearning_0.1_0.2_0.9init15.data', 'r')


data = f.read().splitlines()
data2 = f2.read().splitlines()
data3 = f3.read().splitlines()

f.close()
f2.close()
f3.close()

#print len(data)
data = data[0:20000:1]
data2 = data2[0:20000:1]
data3 = data3[0:20000:1]

#print len(data)
for i in range(len(data)):
    data[i] = float(data[i])
    data2[i] = float(data2[i])
    data3[i] = float(data3[i])

    
#for j in range(len(data)/100):
#    data[j] = sum(data[j*100:j*100+100])/100
#    data2[j] = sum(data2[j*100:j*100+100])/100
#    data3[j] = sum(data3[j*100:j*100+100])/100
#    data4[j] = sum(data4[j*100:j*100+100])/100
#    data5[j] = sum(data5[j*100:j*100+100])/100
#print data
plt.figure(1)
plt.plot(data, 'r', label = '0')
plt.ylim([0,40])
plt.ylabel('episode size')
plt.xlabel('iterations x 200')
plt.legend(loc=2)
plt.figure(2)
plt.plot(data2, 'b', label = '10')
plt.ylim([0,40])
plt.ylabel('episode size')
plt.xlabel('iterations x 200')
plt.legend(loc=2)
plt.figure(3)
plt.plot(data3, 'g', label = '15')
plt.ylim([0,40])
plt.ylabel('episode size')
plt.xlabel('iterations x 200')
plt.legend(loc=2)
plt.show()
#plt.figure(4)
