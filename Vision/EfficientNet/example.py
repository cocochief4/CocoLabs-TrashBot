
# TF for image classification model

import tensorflow
import numpy
from PIL import Image
import pathConfig

model = tensorflow.saved_model.load(str(pathConfig.MSI_PATH, 'Vision/EfficientNet'))
classes = [ "no-trash" ,  "trash" , ]

img = Image.open("image.jpg").convert('RGB')
img = img.resize((300, 300 * img.size[1] // img.size[0]), Image.ANTIALIAS)
inp_numpy = numpy.array(img)[None]


inp = tensorflow.constant(inp_numpy, dtype='float32')

class_scores = model(inp)[0].numpy()


print("")
print("class_scores", class_scores)
print("Class : ", classes[class_scores.argmax()])