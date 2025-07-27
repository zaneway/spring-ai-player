package com.github.zaneway.controller;

import com.github.zaneway.controller.request.ChromaRequest;
import jakarta.annotation.Resource;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;
import org.springframework.ai.chroma.vectorstore.ChromaApi;
import org.springframework.ai.chroma.vectorstore.ChromaApi.Collection;
import org.springframework.ai.chroma.vectorstore.ChromaApi.CreateCollectionRequest;
import org.springframework.ai.chroma.vectorstore.common.ChromaApiConstants;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/chroma")
public class ChromaController {

  @Resource
  private ChromaApi chromaApi;

  @RequestMapping("collections")
  public void getCollects(String data) {
    List<Collection> test = chromaApi.listCollections("zaneway", "test");
    System.out.println(test);
  }

  @RequestMapping("/databases/create")
  public String  createDatabases(@RequestBody ChromaRequest request) {
    if (StringUtils.isEmpty(request.getTenantName())) {
      request.setTenantName(ChromaApiConstants.DEFAULT_TENANT_NAME);
    }
    if (StringUtils.isEmpty(request.getDatabaseName())) {
      request.setDatabaseName(ChromaApiConstants.DEFAULT_DATABASE_NAME);
    }
    try {
      chromaApi.createDatabase(request.getTenantName(),request.getDatabaseName());
    } catch (Exception e) {
      return e.getMessage();
    }
    return "success";
  }


  @RequestMapping("collections/create")
  public String  createCollects(@RequestBody ChromaRequest request) {
    if (StringUtils.isEmpty(request.getTenantName())) {
      request.setTenantName(ChromaApiConstants.DEFAULT_TENANT_NAME);
    }
    if (StringUtils.isEmpty(request.getDatabaseName())) {
      request.setDatabaseName(ChromaApiConstants.DEFAULT_DATABASE_NAME);
    }
    Map<String, Object> metadata = Map.of("hnsw:space", "cosine", "hnsw:M", 20);
    CreateCollectionRequest createCollectionRequest = new CreateCollectionRequest(
        request.getCollectionsName(), metadata);
    Collection collection = chromaApi.createCollection(request.getTenantName(),
        request.getDatabaseName(), createCollectionRequest);
    return collection.name();
  }


}
