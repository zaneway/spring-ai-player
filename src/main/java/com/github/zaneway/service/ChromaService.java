package com.github.zaneway.service;

import com.github.zaneway.controller.request.rag.MetaDataRequest;
import com.github.zaneway.vector.chroma.ChromaDB;
import jakarta.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.springframework.ai.document.Document;
import org.springframework.stereotype.Service;

@Service
public class ChromaService {

  @Resource
  private ChromaDB chromaDB;

  public void add(String data) {
    chromaDB.add(data);
  }

  public List<Document> query(String data) {
    List<Document> query = chromaDB.query(data);
    return query;
  }


  public void addFileToDb(List<Document> datas, String collectionsName, String databasesName,
      String tenantName) {
    chromaDB.add(datas, collectionsName, databasesName, tenantName);
  }


  public List<Document> wrapperDocuments(MetaDataRequest request) {
    ArrayList<Document> result = new ArrayList<>();
    request.getDatas().stream().parallel().forEach(data -> {
      HashMap<String, Object> metadata = new HashMap<>();
      metadata.put("title", request.getTitle());
      metadata.put("chapter",data.getChapter());
      metadata.put("overview",data.getOverview());
      metadata.put("date", request.getDate());
      metadata.put("tag",request.getTags());
      result.add(new Document(data.getText(), metadata));
    });
    return result;
  }


}
