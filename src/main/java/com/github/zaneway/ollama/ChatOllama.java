package com.github.zaneway.ollama;

import jakarta.annotation.Resource;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.ChatClient.Builder;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.memory.MessageWindowChatMemory;
import org.springframework.ai.ollama.OllamaChatModel;
import org.springframework.ai.ollama.OllamaEmbeddingModel;
import org.springframework.ai.rag.advisor.RetrievalAugmentationAdvisor;
import org.springframework.ai.rag.retrieval.search.VectorStoreDocumentRetriever;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.stereotype.Component;

@Component
public class ChatOllama {

  @Resource
  private OllamaChatModel ollamaChatModel;

  @Resource
  private OllamaEmbeddingModel ollamaEmbeddingModel;
  @Resource
  private VectorStore vectorStore;
  public String chat(String msg){
    Builder builder = ChatClient.builder(ollamaChatModel);
    MessageWindowChatMemory chatMemory = MessageWindowChatMemory.builder().maxMessages(1000).build();
    //实现rag
    VectorStoreDocumentRetriever.Builder retriever = VectorStoreDocumentRetriever.builder()
        .vectorStore(vectorStore).similarityThreshold(0.5).topK(10);
    RetrievalAugmentationAdvisor retrievalAugmentationAdvisor = RetrievalAugmentationAdvisor.builder()
        .documentRetriever(retriever.build()).build();

    builder.defaultAdvisors(MessageChatMemoryAdvisor.builder(chatMemory).build(),retrievalAugmentationAdvisor);
    ChatClient client = builder.build();
    return client.prompt(msg).call().content();
  }


}
