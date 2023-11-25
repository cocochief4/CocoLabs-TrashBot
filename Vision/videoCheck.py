# check to see what cameras are available
import cv2

index = 0  # index of the first device to check

while True:
    cap = cv2.VideoCapture(index)
    
    # Check if the device was successfully opened
    if cap.isOpened():
        # Get the device name
        device_name = cap.getBackendName()
        print(f"Device {index}: {device_name}")
        cap.release()  # Release the capture device
        
    else:
        break  # No more devices found
    
    index += 1  # Check the next device
