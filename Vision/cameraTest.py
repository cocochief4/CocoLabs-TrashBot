import cv2
import numpy as np

# [height, width]
RESIZE = [512, 512]

# Define video capture object
vid = cv2.VideoCapture(0)

while(True):
    ret, image = vid.read()
    cv2.imshow("normal", image)
    # shape is in (height, width, depth)
    # print(image.shape)
    height = image.shape[0]
    width = image.shape[1]
    crop_height = [int(((height-RESIZE[0])/2)), int((RESIZE[0]+((height-RESIZE[0]))/2))]
    crop_width = [int(((width-RESIZE[1])/2)), int((RESIZE[1]+((width-RESIZE[1]))/2))]
    print(crop_height, " | ", crop_width)
    crop = image[int(((height-RESIZE[0])/2)):int((RESIZE[0]+((height-RESIZE[0]))/2)), int(((width-RESIZE[1])/2)):int((RESIZE[1]+((width-RESIZE[1]))/2))]
    # image = cv2.resize(image, (512, 512))
    cv2.imshow("image", crop)
    
    # break key
    if cv2.waitKey(1) & 0xFF == ord('q'):
        break

vid.release()
cv2.destroyAllWindows()