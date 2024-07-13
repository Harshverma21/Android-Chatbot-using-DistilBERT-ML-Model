import tensorflow as tf
from transformers import TFAutoModel, AutoTokenizer

# Load the pre-trained model and tokenizer
model_name = "./distilbert-base-uncased"
model = TFAutoModel.from_pretrained(model_name)
tokenizer = AutoTokenizer.from_pretrained(model_name)

# Define a simple function to save the model as a TensorFlow SavedModel
class MyModel(tf.Module):
    def __init__(self, model):
        super(MyModel, self).__init__()
        self.model = model

    @tf.function(input_signature=[tf.TensorSpec([None, None], tf.int32)])
    def __call__(self, input_ids):
        output = self.model(input_ids)
        return output.last_hidden_state

# Create an instance of the MyModel class
my_model = MyModel(model)

# Save the model
tf.saved_model.save(my_model, "./saved_model")

# Save the tokenizer
tokenizer.save_pretrained("./saved_model")
