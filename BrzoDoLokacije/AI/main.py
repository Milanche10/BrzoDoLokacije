import cv2
from PIL import Image
import urllib
import numpy as np
import urllib.request
import requests
from io import BytesIO
from io import StringIO
from typing import Union
from pydantic import BaseModel
from fastapi import FastAPI

class Item(BaseModel):
    url: str


app = FastAPI()

@app.post("/image/")
async def read_root(url:Item):
    print(url)
    faces = checkFaces(url.url)
    return {"Faces":faces}


#url = "https://res.cloudinary.com/dgra88cxm/image/upload/v1669730692/rhsgc6ibvuixjugblkr1.jpg"


def checkFaces(imagePath):
# Get user supplied values
    #imagePath = "D:\Milan\Radovi\Programiranje\Look4\look4\BrzoDoLokacije\AI\codeTest\imageTests\milan.jpg"
    
    cascPath = "haarcascade_frontalface_default.xml"
    face_cascade = cv2.CascadeClassifier(cv2.data.haarcascades + 'haarcascade_frontalface_default.xml')
    eye_cascade = cv2.CascadeClassifier(cv2.data.haarcascades + 'haarcascade_eye.xml')
    # Create the haar cascade
    #faceCascade = cv2.CascadeClassifier(cascPath)
    #imagePath = imagePath[1:-1]
    # Read the image
    urllib.request.urlretrieve(imagePath,"gfg.png")
    
    openedImage = Image.open("gfg.png")
    image = np.asarray(openedImage)
    
    #image = cv2.imread(imagePath)
    gray = cv2.cvtColor(image, cv2.COLOR_BGR2GRAY)

    # Detect faces in the image
    faces = face_cascade.detectMultiScale(
        gray,
        scaleFactor=1.1,
        minNeighbors=12,
        minSize=(50, 50)
        #flags = cv2.CV_HAAR_SCALE_IMAGE
    )

    print("Found {0} faces!".format(len(faces)))
    return len(faces)
    #
    # Draw a rectangle around the faces
    #for (x, y, w, h) in faces:
    #    cv2.rectangle(image, (x, y), (x+w, y+h), (0, 255, 0), 2)
    #
    #cv2.imshow("Faces found", image)
    #cv2.waitKey(0)