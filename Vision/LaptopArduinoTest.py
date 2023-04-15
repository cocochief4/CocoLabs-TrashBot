# simple code for reading and writing using serial commmunication

import serial # if you get library not found, run pip install pyserial (should work)
import time
import keyboard

SerialObj = serial.Serial('COM23') # need to figure out which com you are using

SerialObj.baudrate = 115200 # baudrate, must be equal both on ino and here
SerialObj.bytesize = 8 # bit translated over 8
SerialObj.parity = 'N' # parity = method of checking, don't need it
SerialObj.stopbits = 1 # needed for stopping comms, only need 1 bit

while True:

    time.sleep(3) # to make sure the arduino doesn't automatically reset

    SerialObj.write(b'A') # writing A as a byte
    print("A")

    SerialObj.timeout = 3 # timeout for reading
    ReceivedByte = SerialObj.read()

    try:
        if keyboard.is_pressed('q'):
            print("break")
            break
    except:
        continue

SerialObj.close()
