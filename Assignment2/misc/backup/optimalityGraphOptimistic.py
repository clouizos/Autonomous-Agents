# -*- coding: utf-8 -*-
"""
Created on Tue Oct  1 21:23:01 2013

@author: pathos
"""

import matplotlib.pyplot as plt

f = open('optimality_0.1_0.2_0.9VIinit.data', 'r')

data = f.read().splitlines()

f.close()

#print len(data)
data = data[0:100:1]

#print len(data)

data[i] = float(data[i])

    
#for j in range(len(data)/100):
#    data[j] = sum(data[j*100:j*100+100])/100
#    data2[j] = sum(data2[j*100:j*100+100])/100
#    data3[j] = sum(data3[j*100:j*100+100])/100
#    data4[j] = sum(data4[j*100:j*100+100])/100
#    data5[j] = sum(data5[j*100:j*100+100])/100
#print data
plt.figure(1)
plt.plot(data, 'r', label = 'e: 0.1, a: 0.2, g: 0.9 Optimistic Initialization')
plt.ylim([0,1])
plt.ylabel('episode size')
plt.xlabel('iterations x 200')
plt.legend(loc=3)
plt.show()