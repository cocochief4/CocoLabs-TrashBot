import cv2
import numpy as np

# vid = cv2.VideoCapture(0)

# while (True): 
#     ret, img = vid.read();
img = cv2.imread('attempt.png')

rgb_planes = cv2.split(img)

result_planes = []
result_norm_planes = []
for plane in rgb_planes:
    dilated_img = cv2.dilate(plane, np.ones((7,7), np.uint8))
    bg_img = cv2.medianBlur(dilated_img, 21)
    diff_img = 255 - cv2.absdiff(plane, bg_img)
    new_norm = None
    norm_img = cv2.normalize(diff_img, None, alpha=0, beta=255, norm_type=cv2.NORM_MINMAX)
    # print(norm_img)
    result_planes.append(diff_img)
    result_norm_planes.append(norm_img)
    
result = cv2.merge(result_planes)
result_norm = cv2.merge(result_norm_planes)

# cv2.imshow("img", img)
# cv2.imshow("corrected", result)
cv2.imwrite('yo.png', result)
cv2.imwrite('yo_norm.png', norm_img)
    
    # # break key
    # if cv2.waitKey(1) & 0xFF == ord('q'):
    #     break