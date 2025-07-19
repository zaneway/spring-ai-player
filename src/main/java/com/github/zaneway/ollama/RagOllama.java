package com.github.zaneway.ollama;

import com.github.zaneway.vector.chroma.ChromaDB;
import jakarta.annotation.Resource;
import java.util.List;
import org.springframework.ai.document.Document;
import org.springframework.stereotype.Service;

@Service
public class RagOllama {
  @Resource
  private ChromaDB chromaDB;
  public void add(String data){
    chromaDB.add(data);
  }

  public String query(String data){
    List<Document> query = chromaDB.query(data);
    return query.get(0).getText();
  }

}
