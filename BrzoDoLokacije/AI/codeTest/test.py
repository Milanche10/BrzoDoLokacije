'''
import cv2
import sys

# Get user supplied values
imagePath = "milan2.jpg"
cascPath = "haarcascade_frontalface_default.xml"

# Create the haar cascade
faceCascade = cv2.CascadeClassifier(cascPath)

# Read the image
image = cv2.imread(imagePath)
gray = cv2.cvtColor(image, cv2.COLOR_BGR2GRAY)

# Detect faces in the image
faces = faceCascade.detectMultiScale(
    gray,
    scaleFactor=1.1,
    minNeighbors=5,
    minSize=(30, 30)
    #flags = cv2.CV_HAAR_SCALE_IMAGE
)

print("Found {0} faces!".format(len(faces)))

# Draw a rectangle around the faces
for (x, y, w, h) in faces:
    cv2.rectangle(image, (x, y), (x+w, y+h), (0, 255, 0), 2)

cv2.imshow("Faces found", image)
cv2.waitKey(0)
'''
#-------------------------------------------------------------------------------------

'''
import cv2
import numpy as np
import face_recognition

img_bgr = face_recognition.load_image_file('milan.jpg')
img_rgb = cv2.cvtColor(img_bgr,cv2.COLOR_BGR2RGB)
cv2.imshow('bgr', img_bgr)
cv2.imshow('rgb', img_rgb)
cv2.waitKey(0)

#--------- Detecting Face -------
face = face_recognition.face_locations(img_rgb)[0]
copy = img_rgb.copy()
# ------ Drawing bounding boxes around Faces------------------------
cv2.rectangle(copy, (face[3], face[0]),(face[1], face[2]), (255,0,255), 2)
cv2.imshow('copy', copy)
cv2.imshow('MODI',img_rgb)
cv2.waitKey(0)

#------to find the face location
face = face_recognition.face_locations(img_rgb)[0]
train_encode = face_recognition.face_encodings(img_rgb)[0]

#----- lets test an image
test = face_recognition.load_image_file('milan2.jpg')
test = cv2.cvtColor(test, cv2.COLOR_BGR2RGB)
test_encode = face_recognition.face_encodings(test)[0]
print(face_recognition.compare_faces([train_encode],test_encode))
cv2.rectangle(img_rgb, (face[3], face[0]),(face[1], face[2]), (255,0,255), 1)
cv2.imshow('img_modi', img_rgb)
cv2.waitKey(0)

'''