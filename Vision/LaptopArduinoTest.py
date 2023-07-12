# simple code for reading and writing using serial commmunication

import serial # if you get library not found, run pip install pyserial (should work)
import time
import cv2

SerialObj = serial.Serial('COM26') # need to figure out which com you are using

SerialObj.baudrate = 115200 # baudrate, must be equal both on ino and here
SerialObj.bytesize = 8 # bit translated over 8
SerialObj.parity = 'N' # parity = method of checking, don't need it
SerialObj.stopbits = 1 # needed for stopping comms, only need 1 bit

time.sleep(3) # to make sure the arduino doesn't automatically reset

writeByte = b'T'

while True:

    if (writeByte == b'T'):
        writeByte = b'F'
    else:
        writeByte = b'T'
        
    SerialObj.write(writeByte) # writing A as a byte
    # print("T")

    # SerialObj.timeout = 3 # timeout for reading
    ReceivedByte = SerialObj.read()
    print(ReceivedByte)

    # Break Key (q)
    if cv2.waitKey(1) & 0xFF == ord('q'):
        break

SerialObj.close()
