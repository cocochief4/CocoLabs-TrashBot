import os
import numpy as np

import tensorflow as tf
assert tf.__version__.startswith('2')

from tflite_model_maker import model_spec
from tflite_model_maker import image_classifier
from tflite_model_maker.image_classifier import DataLoader

test_images_path = os.path.join("trashimages", "test")
test_data = DataLoader.from_folder(test_images_path)

train_images_path = os.path.join("trashimages", "train")
train_data = DataLoader.from_folder(train_images_path)

validation_images_path = os.path.join("trashimages", "valid")
validation_data = DataLoader.from_folder(validation_images_path)

model = image_classifier.create(train_data, validation_data=validation_data)

model.summary()

loss, accuracy = model.evaluate(test_data)