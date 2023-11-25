import sys
import cv2
import os
import numpy
import keyboard
from PIL import Image

sys.path.append('.')
import pathConfig

TRASH_PATH = 'trash/'
NO_TRASH_PATH = 'no-trash/'
PATH = pathConfig.SMACINTOSH_PATH + 'Vision/Trash-Dataset/'

# [height, width]
RESIZE = [300, 300]

# Define video capture object
vid = cv2.VideoCapture(1)

index  = 0
record = 'false'

def callback (event):
    global record
    name = event.name
    if name == 'i':
        record = 'trash'
        return
    elif name == 'o':
        record = 'no-trash'
        return
    elif name == 'p':
        record = 'false'
        return
    else:
        return

while(True):
    keyboard.on_release(callback=callback)

    ret, image = vid.read()

    # shape is in (height, width, depth)
    height = image.shape[0]
    width = image.shape[1]

    crop_height = [int(((height-RESIZE[0])/2)), int((RESIZE[0]+((height-RESIZE[0]))/2))]
    crop_width = [int(((width-RESIZE[1])/2)), int((RESIZE[1]+((width-RESIZE[1]))/2))]

    crop = image[int(((height-RESIZE[0])/2)):int((RESIZE[0]+((height-RESIZE[0]))/2)), int(((width-RESIZE[1])/2)):int((RESIZE[1]+((width-RESIZE[1]))/2))]
    inference = numpy.array(crop)[None]

    cv2.imshow("Feed", crop)

    # Check whether or not to save the image
    if record == 'trash':
        path = PATH + TRASH_PATH + 'image_' + str(index) + '.jpg'
        print(cv2.imwrite(path, crop))
        index += 1
        print(index)
    elif record == 'no-trash':
        path = PATH + NO_TRASH_PATH + 'image_' + str(index) + '.jpg'
        print(cv2.imwrite(path, crop))
        index += 1
        print(index)
    
    # break key
    if cv2.waitKey(1) & 0xFF == ord('q'):
        break

vid.release()
cv2.destroyAllWindows()