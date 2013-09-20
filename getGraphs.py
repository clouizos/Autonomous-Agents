# -*- coding: utf-8 -*-
"""
Created on Fri Sep 20 14:53:16 2013

@author: pathos
"""
import matplotlib.pyplot as plt
gamma = [0.1, 0.5, 0.7, 0.9]

valuesVIteration = [18, 25, 27, 29]
valuesPIteration = [108, 234, 331, 773]
fig = plt.figure()
ax = fig.add_subplot(1,1,1)
plt.plot(gamma, valuesVIteration, 'r')
plt.plot(gamma, valuesPIteration, 'g')
ax.set_yscale('log')
plt.show()