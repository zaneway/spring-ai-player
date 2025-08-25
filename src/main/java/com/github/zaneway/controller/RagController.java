package com.github.zaneway.controller;

import com.github.zaneway.controller.request.rag.MetaDataRequest;
import com.github.zaneway.service.ChromaService;
import com.github.zaneway.service.RagService;
import jakarta.annotation.Resource;
import java.util.List;
import org.springframework.ai.document.Document;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("rag")
public class RagController {

  @Resource
  private RagService ragService;

  @Resource
  private ChromaService chromaService;

  @RequestMapping("add")
  public void add(@RequestBody MetaDataRequest request) {
    List<Document> documents = ragService.wrapperDocuments(request);

    chromaService.addFileToDb(documents, request.getCollectionsName(), request.getDatabaseName(),
        request.getTenantName());
    System.out.println(documents);

  }

}
