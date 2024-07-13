import tensorflow as tf

# Load the TensorFlow SavedModel
saved_model_dir = "./saved_model"
converter = tf.lite.TFLiteConverter.from_saved_model(saved_model_dir)

# Convert the model to TensorFlow Lite format
tflite_model = converter.convert()

# Save the TensorFlow Lite model to a file
with open("model.tflite", "wb") as f:
    f.write(tflite_model)
