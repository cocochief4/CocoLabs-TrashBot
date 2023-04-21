import sys
sys.path.append('.')
import cv2
import numpy as np
import tensorflow as tf
import pathConfig

import serial # if you get library not found, run pip install pyserial (should work)
import time

# SerialObj = serial.Serial('COM22') # need to figure out which com you are using

# SerialObj.baudrate = 115200 # baudrate, must be equal both on ino and here
# SerialObj.bytesize = 8 # bit translated over 8
# SerialObj.parity = 'N' # parity = method of checking, don't need it
# SerialObj.stopbits = 1 # needed for stopping comms, only need 1 bit


# [height, width]
RESIZE = [300, 300]

#Define paths
MOBILENET='/MobileNet/'
RESNET50='/ResNet50/'
EFFICIENTNET='/EfficientNet/'
PATH = pathConfig.SMACINTOSH_PATH + 'Vision/'
sys.path.append(PATH + RESNET50)

# Load the model
model = tf.saved_model.load(PATH + RESNET50)
classes = [ "no-trash" ,  "trash" , ]

# Define video capture object
vid = cv2.VideoCapture(1)

time.sleep(3) # to make sure the arduino doesn't automatically reset

while(True):
    ret, image = vid.read()

    # shape is in (height, width, depth)
    height = image.shape[0]
    width = image.shape[1]

    crop_height = [int(((height-RESIZE[0])/2)), int((RESIZE[0]+((height-RESIZE[0]))/2))]
    crop_width = [int(((width-RESIZE[1])/2)), int((RESIZE[1]+((width-RESIZE[1]))/2))]

    crop = image[int(((height-RESIZE[0])/2)):int((RESIZE[0]+((height-RESIZE[0]))/2)), int(((width-RESIZE[1])/2)):int((RESIZE[1]+((width-RESIZE[1]))/2))]
    inference = np.array(crop)[None]

    cv2.imshow("Feed", crop)

    inp = tf.constant(inference, dtype='float32', name='input')

    class_scores = model(inp)[0].numpy()

    if (classes[class_scores.argmax()] == "trash"):
        # SerialObj.write(b'T')
        print("Write T")
    elif (classes[class_scores.argmax()] == "no-trash"):
        # SerialObj.write(b'F')
        print("Write F")


    print("")
    print("class_scores", class_scores)
    print("Class : ", classes[class_scores.argmax()])
    
    # break key
    if cv2.waitKey(1) & 0xFF == ord('q'):
        break

vid.release()
cv2.destroyAllWindows()