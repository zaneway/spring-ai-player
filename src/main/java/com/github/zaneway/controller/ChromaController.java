package com.github.zaneway.controller;

import jakarta.annotation.Resource;
import java.util.List;
import org.springframework.ai.chroma.vectorstore.ChromaApi;
import org.springframework.ai.chroma.vectorstore.ChromaApi.Collection;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/chroma")
public class ChromaController {

  @Resource
  private ChromaApi chromaApi;

  @RequestMapping("Collections")
  public void getCollects(String data) {
    List<Collection> test = chromaApi.listCollections("", "test");
    System.out.println(test);
  }

}
