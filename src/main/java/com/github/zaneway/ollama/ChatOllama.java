package com.github.zaneway.ollama;

import com.github.zaneway.vector.chroma.ChromaDB;
import jakarta.annotation.Resource;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.ChatClient.Builder;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.memory.MessageWindowChatMemory;
import org.springframework.ai.ollama.OllamaChatModel;
import org.springframework.ai.rag.advisor.RetrievalAugmentationAdvisor;
import org.springframework.ai.rag.generation.augmentation.ContextualQueryAugmenter;
import org.springframework.ai.rag.retrieval.search.VectorStoreDocumentRetriever;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.stereotype.Component;

@Component
public class ChatOllama {

  @Resource
  private OllamaChatModel ollamaChatModel;

  @Resource
  private ChromaDB chromaDB;

  public String chat(String msg) {
    Builder builder = ChatClient.builder(ollamaChatModel);
    MessageWindowChatMemory chatMemory = MessageWindowChatMemory.builder().maxMessages(1000)
        .build();

    VectorStore vector = chromaDB.getDefaultVector();

    //实现rag
    VectorStoreDocumentRetriever.Builder retriever = VectorStoreDocumentRetriever.builder()
        .vectorStore(vector).similarityThreshold(0.8).topK(5);

    //允许上下文查询结果为空
    ContextualQueryAugmenter contextualQueryAugmenter = ContextualQueryAugmenter.builder()
        .allowEmptyContext(true)
        .build();
    //构造advisor
    RetrievalAugmentationAdvisor retrievalAugmentationAdvisor = RetrievalAugmentationAdvisor.builder()
        .documentRetriever(retriever.build())
        .queryAugmenter(contextualQueryAugmenter)
        .build();

    builder.defaultAdvisors(MessageChatMemoryAdvisor.builder(chatMemory).build(),
        retrievalAugmentationAdvisor);

    ChatClient client = builder.build();
    return client.prompt(msg).call().content();
  }


}
