package com.github.zaneway.vector.chroma;

import jakarta.annotation.Resource;
import java.util.List;
import org.springframework.ai.chroma.vectorstore.ChromaApi;
import org.springframework.ai.chroma.vectorstore.ChromaVectorStore;
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
    ChromaVectorStore store = ChromaVectorStore.builder(chromaApi, ollamaEmbeddingModel)
        .collectionName("zaneway")
        .databaseName("pki")
        .tenantName("zaneway")
        .initializeSchema(true)
        .initializeImmediately(true)
        .build();
    List<Document> documents = store.similaritySearch(data);
    return documents;
  }

  public void add(List<Document> documents,String collectionsName, String databasesName, String tenantName) {
    ChromaVectorStore store = ChromaVectorStore.builder(chromaApi, ollamaEmbeddingModel)
        .collectionName(collectionsName)
        .databaseName(databasesName)
        .tenantName(tenantName)
        .initializeSchema(true)
        .initializeImmediately(true)
        .build();
    store.add(documents);
  }

  public VectorStore getVector(String collectionsName, String databasesName, String tenantName) {
    return ChromaVectorStore.builder(chromaApi, ollamaEmbeddingModel)
        .collectionName(collectionsName)
        .databaseName(databasesName)
        .tenantName(tenantName)
        .initializeSchema(true)
        .initializeImmediately(true)
        .build();
  }

  public VectorStore getDefaultVector() {
    return ChromaVectorStore.builder(chromaApi, ollamaEmbeddingModel)
        .collectionName("zaneway")
        .databaseName("pki")
        .tenantName("zaneway")
        .initializeSchema(true)
        .initializeImmediately(true)
        .build();
  }

}
