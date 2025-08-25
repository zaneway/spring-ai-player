package com.github.zaneway.controller;

import com.github.zaneway.controller.request.ChatRequest;
import com.github.zaneway.controller.request.ChromaRequest;
import com.github.zaneway.controller.request.FileRequest;
import com.github.zaneway.file.FileTypeAdapter;
import com.github.zaneway.file.ParseFileHandler;
import com.github.zaneway.service.ChromaService;
import jakarta.annotation.Resource;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;
import org.springframework.ai.chroma.vectorstore.ChromaApi;
import org.springframework.ai.chroma.vectorstore.ChromaApi.Collection;
import org.springframework.ai.chroma.vectorstore.ChromaApi.CreateCollectionRequest;
import org.springframework.ai.chroma.vectorstore.common.ChromaApiConstants;
import org.springframework.ai.document.Document;
import org.springframework.core.io.FileSystemResource;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/chroma")
public class ChromaController {

  @Resource
  private ChromaApi chromaApi;
  @Resource
  private ChromaService chromaService;

  @Resource
  private FileTypeAdapter adapter;
  @RequestMapping("collections")
  public List<Collection> getCollects(@RequestBody ChromaRequest request) {
    return chromaApi.listCollections(request.getTenantName(), request.getDatabaseName());
  }

  @RequestMapping("/databases/create")
  public String createDatabases(@RequestBody ChromaRequest request) {
    if (StringUtils.isEmpty(request.getTenantName())) {
      request.setTenantName(ChromaApiConstants.DEFAULT_TENANT_NAME);
    }
    if (StringUtils.isEmpty(request.getDatabaseName())) {
      request.setDatabaseName(ChromaApiConstants.DEFAULT_DATABASE_NAME);
    }
    try {
      chromaApi.createDatabase(request.getTenantName(), request.getDatabaseName());
    } catch (Exception e) {
      return e.getMessage();
    }
    return "success";
  }


  @RequestMapping("collections/create")
  public String createCollects(@RequestBody ChromaRequest request) {
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

  //可以多几个参数，设置 MetaData
  @RequestMapping("message/add")
  public String chromaAdd(@RequestBody ChatRequest msg) {
    chromaService.add(msg.getMsg());
    return "success";
  }

  @RequestMapping("message/get")
  public List<Document> chromaGet(@RequestBody ChatRequest msg) {
    return chromaService.query(msg.getMsg());
  }


  @RequestMapping("file/add")
  public String addFile(@RequestBody FileRequest request) {
    String[] split = request.getFilePath().split(".");
    FileSystemResource resource = new FileSystemResource(request.getFilePath());
    //根据文件后缀获取实现类
    ParseFileHandler handler = adapter.getBean(split[split.length - 1]);
    List<Document> documents = handler.parseFile(resource, null);
    chromaService.addFileToDb(documents, request.getCollectionsName(), request.getDatabaseName(),
        request.getTenantName());
    return "success";
  }


}
