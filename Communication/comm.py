# simple code for reading and writing using serial commmunication

import serial # if you get library not found, run pip install pyserial (should work)
import time

SerialObj = serial.Serial('COM4') # need to figure out which com you are using

SerialObj.baudrate = 9600 # baudrate, must be equal both on ino and here
SerialObj.bytesize = 8 # bit translated over 8
SerialObj.parity = 'N' # parity = method of checking, don't need it
SerialObj.stopbits = 1 # needed for stopping comms, only need 1 bit

time.sleep(3) # to make sure the arduino doesn't automatically reset

SerialObj.write(b'A') # writing A as a byte

SerialObj.timeout = 3 # timeout for reading
ReceivedByte = SerialObj.read() 

SerialObj.close()
