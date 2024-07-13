package com.example.chatbot;

import android.content.Context;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

public class Tokenizer {

    private Map<String, Integer> vocab;
    private Map<Integer, String> invVocab;
    private static final String VOCAB_FILE = "vocab.txt";

    public Tokenizer(Context context) {
        vocab = new HashMap<>();
        invVocab = new HashMap<>();
        loadVocab(context);
    }

    private void loadVocab(Context context) {
        try {
            InputStream is = context.getAssets().open(VOCAB_FILE);
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            String line;
            int index = 0;
            while ((line = reader.readLine()) != null) {
                vocab.put(line.trim(), index);
                invVocab.put(index, line.trim());
                index++;
            }
            reader.close();
            is.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public int[] tokenize(String text) {
        String[] tokens = text.toLowerCase().split(" ");
        int[] tokenIds = new int[tokens.length];
        for (int i = 0; i < tokens.length; i++) {
            tokenIds[i] = vocab.getOrDefault(tokens[i], vocab.get("[UNK]"));
        }
        return tokenIds;
    }

    public String detokenize(int[] tokenIds) {
        StringBuilder result = new StringBuilder();
        for (int tokenId : tokenIds) {
            result.append(invVocab.get(tokenId)).append(" ");
        }
        return result.toString().trim();
    }
}

