package com.example.chatbot;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.tensorflow.lite.DataType;
import org.tensorflow.lite.Interpreter;
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private EditText editText;
    private Button buttonSend;
    private MessageAdapter adapter;
    private List<String> messageList;
    private Interpreter tflite;
    private Tokenizer tokenizer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recyclerView);
        editText = findViewById(R.id.editText);
        buttonSend = findViewById(R.id.buttonSend);
        messageList = new ArrayList<>();
        adapter = new MessageAdapter(messageList);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        tokenizer = new Tokenizer(this);
        loadModel();

        buttonSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userInput = editText.getText().toString();
                if (!userInput.isEmpty()) {
                    messageList.add("User: " + userInput);
                    adapter.notifyDataSetChanged();
                    recyclerView.scrollToPosition(messageList.size() - 1);
                    editText.setText("");

                    // Process input with TFLite model
                    String response = getResponse(userInput);
                    messageList.add("Bot: " + response);
                    adapter.notifyDataSetChanged();
                    recyclerView.scrollToPosition(messageList.size() - 1);
                }
            }
        });
    }

    private void loadModel() {
        try {
            FileInputStream fis = new FileInputStream(getAssets().openFd("distilbert.tflite").getFileDescriptor());
            FileChannel fileChannel = fis.getChannel();
            MappedByteBuffer tfliteModel = fileChannel.map(FileChannel.MapMode.READ_ONLY, 0, fileChannel.size());
            tflite = new Interpreter(tfliteModel);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String getResponse(String input) {
        int[] inputIds = tokenizer.tokenize(input);
        int[] flattenedInputIds = flattenArray(inputIds);

        // Prepare input tensor
        TensorBuffer inputTensorBuffer = TensorBuffer.createFixedSize(new int[]{1, inputIds.length}, DataType.INT32);
        inputTensorBuffer.loadArray(flattenedInputIds);

        // Prepare output tensor
        TensorBuffer outputTensorBuffer = TensorBuffer.createFixedSize(new int[]{1, 512}, DataType.FLOAT32);

        // Run inference
        tflite.run(inputTensorBuffer.getBuffer(), outputTensorBuffer.getBuffer());

        // Process the output tensor
        float[] outputArray = outputTensorBuffer.getFloatArray();

        // Convert the model output to a readable response
        return processOutput(outputArray);
    }

    private String processOutput(float[] outputArray) {
        // Here, we'll assume the output is a sequence of token IDs
        // You need to determine how the model's output is structured and adapt accordingly
        int[] tokenIds = new int[outputArray.length];
        for (int i = 0; i < outputArray.length; i++) {
            tokenIds[i] = (int) outputArray[i];
        }

        // Detokenize the token IDs to a readable string
        return tokenizer.detokenize(tokenIds);
    }

    private int[] flattenArray(int[] array) {
        return array; // No need to flatten since it's already a 1D array
    }
}
