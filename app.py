import cv2
from deepface import DeepFace
import dlib
import numpy as np
from collections import deque
from datetime import datetime
from flask import Flask, request, jsonify
from flask_cors import CORS

PREDICTOR_PATH = "shape_predictor_68_face_landmarks.dat"
PREDICTOR_PATH_2 = "haarcascade_frontalface_default.xml"

detector = dlib.get_frontal_face_detector()
predictor = dlib.shape_predictor(PREDICTOR_PATH)

LEFT_EYE_INDICES = list(range(36, 42))
RIGHT_EYE_INDICES = list(range(42, 48))

FOCUS_BUFFER_SIZE = 4
focus_buffer = deque(maxlen=FOCUS_BUFFER_SIZE)

app = Flask(__name__)
CORS(app)  # allow cross-origin requests if you're opening index.html from a different port

def getEmotion(img):
    gray_image = cv2.cvtColor(img, cv2.COLOR_BGR2GRAY)
    rgb_image = cv2.cvtColor(gray_image, cv2.COLOR_GRAY2RGB)

    face_cascade = cv2.CascadeClassifier(cv2.data.haarcascades + PREDICTOR_PATH_2)
    faces = face_cascade.detectMultiScale(
        gray_image, scaleFactor=1.1, minNeighbors=5, minSize=(30, 30)
    )
    if len(faces) == 0:
        # No faces found
        return None
    # Sort faces by area (largest first)
    faces = sorted(faces, key=lambda f: f[2] * f[3], reverse=True)
    x, y, w, h = faces[0]
    face_roi = rgb_image[y:y+h, x:x+w]

    # DeepFace analysis
    try:
        result = DeepFace.analyze(face_roi, actions=['emotion'], enforce_detection=True)
        dominant_emotion = result[0]["dominant_emotion"]
        return dominant_emotion
    except:
        # In case DeepFace fails to detect a face
        return None

def get_eye_landmarks(landmarks):
    left_eye_pts = [(landmarks.part(i).x, landmarks.part(i).y) for i in LEFT_EYE_INDICES]
    right_eye_pts = [(landmarks.part(i).x, landmarks.part(i).y) for i in RIGHT_EYE_INDICES]
    return (np.array(left_eye_pts, dtype=np.int32),
            np.array(right_eye_pts, dtype=np.int32))

def find_pupil_center(thresh):
    contours, _ = cv2.findContours(thresh, cv2.RETR_EXTERNAL, cv2.CHAIN_APPROX_SIMPLE)
    if contours:
        max_contour = max(contours, key=cv2.contourArea)
        M = cv2.moments(max_contour)
        if M["m00"] != 0:
            cx = int(M["m10"] / M["m00"])
            cy = int(M["m01"] / M["m00"])
            return (cx, cy)
    return None

def is_looking_forward(frame, left_eye_pts, right_eye_pts):
    x_left, y_left, w_left, h_left = cv2.boundingRect(left_eye_pts)
    x_right, y_right, w_right, h_right = cv2.boundingRect(right_eye_pts)

    left_eye_roi = frame[y_left:y_left + h_left, x_left:x_left + w_left]
    right_eye_roi = frame[y_right:y_right + h_right, x_right:x_right + w_right]

    left_gray = cv2.cvtColor(left_eye_roi, cv2.COLOR_BGR2GRAY)
    right_gray = cv2.cvtColor(right_eye_roi, cv2.COLOR_BGR2GRAY)
    left_gray = cv2.GaussianBlur(left_gray, (5, 5), 0)
    right_gray = cv2.GaussianBlur(right_gray, (5, 5), 0)

    _, left_thresh = cv2.threshold(left_gray, 0, 255, cv2.THRESH_BINARY_INV | cv2.THRESH_OTSU)
    _, right_thresh = cv2.threshold(right_gray, 0, 255, cv2.THRESH_BINARY_INV | cv2.THRESH_OTSU)

    kernel = cv2.getStructuringElement(cv2.MORPH_ELLIPSE, (3, 3))
    left_thresh = cv2.morphologyEx(left_thresh, cv2.MORPH_OPEN, kernel)
    right_thresh = cv2.morphologyEx(right_thresh, cv2.MORPH_OPEN, kernel)

    left_center = find_pupil_center(left_thresh)
    right_center = find_pupil_center(right_thresh)

    if left_center is None or right_center is None:
        return False

    def compute_eye_center_ratio(center, eye_w, eye_h):
        cx, cy = center
        return (cx / float(eye_w), cy / float(eye_h))

    left_ratio = compute_eye_center_ratio(left_center, w_left, h_left)
    right_ratio = compute_eye_center_ratio(right_center, w_right, h_right)

    def in_center(ratio):
        rx, ry = ratio
        return 0.25 < rx < 0.75 and 0.25 < ry < 0.75

    return in_center(left_ratio) and in_center(right_ratio)

def process_frame(cv2_img):
    current_time = datetime.now().strftime('%Y-%m-%d %H:%M:%S')
    emotion = getEmotion(cv2_img)
    gray = cv2.cvtColor(cv2_img, cv2.COLOR_BGR2GRAY)
    faces = detector(gray)
    if faces:
        largest_face = max(faces, key=lambda rect: rect.width() * rect.height())
        landmarks = predictor(gray, largest_face)
        left_eye_pts, right_eye_pts = get_eye_landmarks(landmarks)
        focused_now = is_looking_forward(cv2_img, left_eye_pts, right_eye_pts)
        focus_buffer.append(focused_now)
        if sum(focus_buffer) > (len(focus_buffer) // 3):
            eye_status = "Focused on Screen"
        else:
            eye_status = "Not Focused"
    else:
        eye_status = "No faces detected."

    # If emotion is None, you can interpret as "No faces detected" or "Unknown"
    if emotion is None:
        emotion = "No faces detected."

    response_data = {
        "status": 1,  # you can use 1 for success, 0 for error
        "message": "Analysis complete",
        "data": {
            "datetime": current_time,
            "emotion": emotion,
            "eye": eye_status
        }
    }
    return jsonify(response_data)

@app.route('/analyze', methods=['POST'])
def analyze():
    if 'frame' not in request.files:
        return jsonify(status=0, message="No frame found in form data", data={})

    uploaded_file = request.files['frame']
    file_bytes = uploaded_file.read()
    nparr = np.frombuffer(file_bytes, np.uint8)
    cv2_img = cv2.imdecode(nparr, cv2.IMREAD_COLOR)

    if cv2_img is None:
        return jsonify(status=0, message="Failed to decode image", data={})

    return process_frame(cv2_img)

if __name__ == "__main__":
    app.run(debug=True)
