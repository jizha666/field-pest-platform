import os
from pathlib import Path

import cv2
import numpy as np
from flask import Flask, jsonify, request
from ultralytics import YOLO


APP_DIR = Path(__file__).resolve().parent
MODEL_PATH = Path(os.getenv("YOLO_MODEL_PATH", str(APP_DIR / "best v8s.pt")))
RUNS_DIR = Path(os.getenv("YOLO_RUNS_DIR", str(APP_DIR / "runs")))

if not MODEL_PATH.is_file():
    raise FileNotFoundError(
        f"YOLO model not found: {MODEL_PATH}. "
        "Place the authorized model file here or set YOLO_MODEL_PATH."
    )

app = Flask(__name__)
model = YOLO(str(MODEL_PATH))


@app.post("/upload")
def detect():
    if "file" not in request.files:
        return jsonify({"error": "Invalid input"}), 400

    file = request.files["file"].read()
    image = cv2.imdecode(np.frombuffer(file, np.uint8), cv2.IMREAD_COLOR)
    if image is None:
        return jsonify({"error": "Invalid image"}), 400

    results = model.predict(
        image,
        save=True,
        project=str(RUNS_DIR),
        name="predict",
        exist_ok=True,
        verbose=False,
    )
    number = sum(len(result.boxes) for result in results)
    return jsonify({"message": "File uploaded successfully", "number": number}), 200


if __name__ == "__main__":
    app.run(host="0.0.0.0", port=int(os.getenv("PORT", "5000")))
