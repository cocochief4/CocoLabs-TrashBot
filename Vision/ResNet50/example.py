
# TF for image classification model

import sys
sys.path.append('.')

import tensorflow
import numpy
from PIL import Image
import pathConfig

MODEL_PATH = pathConfig.MSI_PATH + 'Vision\\ResNet50'
sys.path.append(MODEL_PATH)

model = tensorflow.saved_model.load(MODEL_PATH)
classes = [ "no-trash" ,  "trash" , ]

img = Image.open(MODEL_PATH + "\\image.jpg").convert('RGB')
img = img.resize((300, 300 * img.size[1] // img.size[0]))
inp_numpy = numpy.array(img)[None]
print(inp_numpy.shape)


inp = tensorflow.constant(inp_numpy, dtype='float32')

class_scores = model(inp)[0].numpy()


print("")
print("class_scores", class_scores)
print("Class : ", classes[class_scores.argmax()])