package com.github.zaneway.service;

import com.github.zaneway.controller.request.rag.MetaDataRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.springframework.ai.document.Document;
import org.springframework.stereotype.Service;

@Service
public class RagService {

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
