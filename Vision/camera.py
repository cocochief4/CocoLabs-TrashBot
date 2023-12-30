import sys
sys.path.append('.')
import cv2
import numpy as np
import scipy as sp
import tensorflow as tf
import pathConfig

import serial # if you get library not found, run pip install pyserial (should work)
import time

SerialObj = serial.Serial('COM43') # need to figure out which com you are using Should be COM60 if multiple CH340s

SerialObj.baudrate = 115200 # baudrate, must be equal both on ino and here
SerialObj.bytesize = 8 # bit translated over 8
SerialObj.parity = 'N' # parity = method of checking, don't need it
SerialObj.stopbits = 1 # needed for stopping comms, only need 1 bit


# [height, width]
RESIZE = [75, 75]

#Define paths
MOBILENET='/MobileNet/'
RESNET50='/ResNet50/'
RESNET50_FULL = '/ResNet50-Full'
EFFICIENTNET='/EfficientNet/'
PATH = pathConfig.SMACINTOSH_PATH + 'Vision/'
sys.path.append(PATH + RESNET50)

def preprocessBlur(image):
    # Apply a blur to the brightness of the image and nothing else
    grayImage = cv2.cvtColor(image, cv2.COLOR_BGR2HSV)
    h, s, v = cv2.split(grayImage)
    # v_blurred = cv2.GaussianBlur(v, (101, 101), 0)
    # Scale down the V channel
    v_scaled = cv2.convertScaleAbs(v, alpha=0, beta=100)
    # Merge the H, S and scaled V channels
    img_hsv_scaled = cv2.merge([h, s, v_scaled])
    # img_hsv_blurred = cv2.merge([h, s, v_blurred])
    # Convert the HSV image back to RGB
    # Add the brightness blur back in the color image
    returnImage = cv2.cvtColor(image, cv2.COLOR_BGR2HSV)
    for column in range (RESIZE[0]):
        for row in range (RESIZE[1]):
            returnImage[column][row][2] = img_hsv_scaled[column][row][2]
    returnImage = cv2.cvtColor(returnImage, cv2.COLOR_HSV2BGR)
    return returnImage

def preprocessWarmthFilter(image):
    hsv_image = cv2.cvtColor(image, cv2.COLOR_BGR2HSV)
    h, s, v = cv2. split(hsv_image)

def zoom_at(img, zoom=1, angle=0, coord=None):
    cy, cx = [ i/2 for i in img.shape[:-1] ] if coord is None else coord[::-1]
    rot_mat = cv2.getRotationMatrix2D((cx,cy), angle, zoom)
    result = cv2.warpAffine(img, rot_mat, img.shape[1::-1], flags=cv2.INTER_LINEAR)
    return result

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
    
    crop = cv2.resize(crop, (300,300))
    
    new_crop = zoom_at(crop, 1.5, coord=(150, 150))
    # scaled_crop = new_crop.resize(300, 300);

    # crop = preprocessBlur(crop)
    inference = np.array(crop)[None]

    # cv2.imshow("Original", prepreprocess)
    cv2.imshow("Feed", crop)

    inp = tf.constant(inference, dtype='float32', name='input')

    class_scores = model(inp)[0].numpy()

    if (classes[class_scores.argmax()] == "trash"):
        SerialObj.write(b'T')
        print("Write T")
    elif (classes[class_scores.argmax()] == "no-trash"):
        SerialObj.write(b'F')
        print("Write F")


    print("")
    print("class_scores", class_scores)
    print("Class : ", classes[class_scores.argmax()])
    
    # break key
    if cv2.waitKey(1) & 0xFF == ord('q'):
        break

vid.release()
cv2.destroyAllWindows()