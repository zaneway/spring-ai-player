package com.github.zaneway.vector.chroma;

import jakarta.annotation.Resource;
import java.util.List;
import org.springframework.ai.chroma.vectorstore.ChromaApi;
import org.springframework.ai.document.Document;
import org.springframework.ai.ollama.OllamaEmbeddingModel;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.stereotype.Component;

@Component
public class ChromaDB {

  @Resource
  private OllamaEmbeddingModel ollamaEmbeddingModel;
  @Resource
  private ChromaApi chromaApi;
  @Resource
  private VectorStore vectorStore;

  public void add(String data) {
    vectorStore.add(List.of(new Document(data)));
  }

  public List<Document> query(String data) {
    List<Document> documents = vectorStore.similaritySearch(data);
    return documents;
  }

}
