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

  public void add(String data) {
    chromaDB.add(data);
  }

  public List<Document> query(String data) {
    List<Document> query = chromaDB.query(data);
    return query;
  }


  public void addFileToDb(List<Document> datas,String collectionsName, String databasesName, String tenantName) {
    chromaDB.add(datas, collectionsName, databasesName, tenantName);
  }

}
