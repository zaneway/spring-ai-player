package com.github.zaneway.controller;

import jakarta.annotation.Resource;
import java.util.List;
import java.util.Map;
import org.springframework.ai.chroma.vectorstore.ChromaApi;
import org.springframework.ai.chroma.vectorstore.ChromaApi.Collection;
import org.springframework.ai.chroma.vectorstore.ChromaApi.CreateCollectionRequest;
import org.springframework.ai.chroma.vectorstore.common.ChromaApiConstants;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/chroma")
public class ChromaController {

  @Resource
  private ChromaApi chromaApi;

  @RequestMapping("collections")
  public void getCollects(String data) {
    List<Collection> test = chromaApi.listCollections("zaneway", "test");
    System.out.println(test);
  }

  @RequestMapping("collections/create")
  public void createCollects() {
    List<Collection> test = chromaApi.listCollections("zaneway", "test");
    String defaultTenantName = ChromaApiConstants.DEFAULT_TENANT_NAME;
    String defaultDatabaseName = ChromaApiConstants.DEFAULT_DATABASE_NAME;

    Map<String, Object> metadata = Map.of("hnsw:space", "cosine", "hnsw:M", 20);
    CreateCollectionRequest createCollectionRequest = new CreateCollectionRequest("TestCreateCollections", metadata);
    Collection collection = chromaApi.createCollection(defaultTenantName, defaultDatabaseName,
        createCollectionRequest);
    String name1 = collection.name();
    System.out.println(name1);
  }


}
