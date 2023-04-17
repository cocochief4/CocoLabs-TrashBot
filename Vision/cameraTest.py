import sys
sys.path.append('.')
import cv2
import numpy as np
import tensorflow as tf
import pathConfig

# [height, width]
RESIZE = [300, 300]

#Define paths
MOBILENET='/MobileNet/'
RESNET50='/ResNet50/'
EFFICIENTNET='/EfficientNet/'
PATH = pathConfig.MSI_PATH + 'Vision/'

# Load the model
model = tf.saved_model.load(PATH + MOBILENET)
classes = [ "no-trash" ,  "trash" , ]

# Define video capture object
vid = cv2.VideoCapture(0)

while(True):
    ret, image = vid.read()

    # shape is in (height, width, depth)
    height = image.shape[0]
    width = image.shape[1]

    crop_height = [int(((height-RESIZE[0])/2)), int((RESIZE[0]+((height-RESIZE[0]))/2))]
    crop_width = [int(((width-RESIZE[1])/2)), int((RESIZE[1]+((width-RESIZE[1]))/2))]

    crop = image[int(((height-RESIZE[0])/2)):int((RESIZE[0]+((height-RESIZE[0]))/2)), int(((width-RESIZE[1])/2)):int((RESIZE[1]+((width-RESIZE[1]))/2))]
    print(crop.shape)

    inp = tf.constant(crop, dtype='float32', name='input')

    class_scores = model(inp, False, None)[0].numpy()


    print("")
    print("class_scores", class_scores)
    print("Class : ", classes[class_scores.argmax()])
    
    # break key
    if cv2.waitKey(1) & 0xFF == ord('q'):
        break

vid.release()
cv2.destroyAllWindows()