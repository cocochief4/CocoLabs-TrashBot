# NOT FOR COLLECTING METRICS!

import sys
sys.path.append('.')
import cv2
import numpy as np
import scipy as sp
import tensorflow as tf
import pathConfig
import os

import serial # if you get library not found, run pip install pyserial (should work)
import time


# [height, width]
RESIZE = [75, 75]

#Define paths
MOBILENET='/MobileNet/'
RESNET50='/ResNet50/'
RESNET50_FULL = '/ResNet50-Full'
EFFICIENTNET='/EfficientNet/'
PATH = pathConfig.SMACINTOSH_PATH + 'Vision/'
sys.path.append(PATH + RESNET50)

# Load the model
model = tf.saved_model.load(PATH + MOBILENET)
classes = [ "no-trash" ,  "trash" , ]

def process_images(folder_path, isTrash):
    print (folder_path)
    # Check if the given path is a directory
    if not os.path.isdir(folder_path):
        print("Error: The provided path is not a directory.")
        return
    
    # Get a list of all files in the directory
    files = os.listdir(folder_path)
    
    acc = 0;
    # Iterate through each file in the directory
    for file_name in files:
        file_path = os.path.join(folder_path, file_name)
        
        # Check if the file is a PNG image
        if file_name.lower().endswith(".jpg"):
            try:
                # Read the image using OpenCV
                img = cv2.imread(file_path)
                
                # Process the image as needed (you can add your own processing logic here)
                # For example, you can resize, crop, or apply any image processing operation
                
                inference = np.array(img)[None]
                
                inp = tf.constant(inference, dtype='float32', name='input')

                class_scores = model(inp)[0].numpy()
                
                if (isTrash == True):
                    if (classes[class_scores.argmax()] == "trash"):
                        acc = acc + 1
                else:
                    if (classes[class_scores.argmax()] == "no-trash"):
                        acc = acc + 1;
                
                # Print information about the image
                
            except Exception as e:
                print(f"Error processing {file_name}: {str(e)}")
        
        # If the file is not a PNG image, you can skip it or add additional checks
    
    print(acc)
    acc = acc/len(files)
    return acc

print(process_images(PATH + "Trash-Dataset2/no-trash", False))
print(process_images(PATH + "Trash-Dataset2/trash", True))
    

