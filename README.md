

 This project involves implementing a Small Language Model (SLM) / Large Language Model LLM that generates avatar responses locally within an Android application. DistilBERT was chosen for this purpose, and it was converted to a TensorFlow Lite model to enable efficient local processing. The final implementation was integrated into an Android Studio project to build a chatbot.

  Objectives:
 Research and select an appropriate language model. Convert the selected model to TensorFlow Lite. Integrate the TensorFlow Lite model within an Android app. Develop and test the chatbot functionality.

Research on SLMs and LLMs:
 Summary of Research Various Small Language Models (SLMs) and Large Language Models (LLMs) were evaluated for their suitability in local deployment on Android devices. Key 
considerations included model size, inference speed, and compatibility with TensorFlow Lite.

 Models Considered:
 LLAMA-3 using Tensor virtual machine, 
 Microsoft phi-2, 
 GPT-2, 
 MobileBERT,
 DistilBERT,
 Gemini Nano, 
 MT5
 
 Conclusion:
 DistilBERT was chosen due to its balance between performance and efficiency, making it well-suited for local deployment on mobile devices.
 
Model Selection:

 DistilBERT is a smaller, faster, cheaper version of BERT, developed by Hugging Face. It retains 97% of BERTʼs language understanding capabilities while being 60% faster and 40% smaller.
