#!/usr/bin/python3

import cv2
import face_recognition

# cascPath = sys.argv[1]
cascPath = "haarcascade_frontalface_default.xml"
faceCascade = cv2.CascadeClassifier(cascPath)

video_capture = cv2.VideoCapture(0)

if __name__ == "__main__":
    petka = None
    while True:
        # Capture frame-by-frame
        ret, frame = video_capture.read()
        # Resize frame of video to 1/4 size for faster face recognition processing
        #frame = cv2.resize(frame, (0, 0), fx=0.50, fy=0.50)

        gray = cv2.cvtColor(frame, cv2.COLOR_BGR2GRAY)

        faces = faceCascade.detectMultiScale(
            gray,
            scaleFactor=1.1,
            minNeighbors=5,
            minSize=(30, 30),
            flags=cv2.CASCADE_SCALE_IMAGE
        )

        # Draw a rectangle around the faces
        for (x, y, w, h) in faces:
            rect = cv2.rectangle(frame, (x, y), (x + w, y + h), (0, 255, 0), 2)

            text = "Unknown"
            face = face_recognition.face_encodings(frame[y:y + h, x:x + w])
            if len(face):
                if petka is None:
                    petka = face[0]
                else:
                    if face_recognition.compare_faces(petka, face):
                        text = "Petka"

            cv2.putText(rect, text, (x, y - 10), cv2.FONT_HERSHEY_SIMPLEX, 0.9, (36, 255, 12), 2)

        # Display the resulting frame
        cv2.imshow('Video', frame)

        if cv2.waitKey(1) & 0xFF == ord('q'):
            break

    # When everything is done, release the capture
    video_capture.release()
    cv2.destroyAllWindows()
