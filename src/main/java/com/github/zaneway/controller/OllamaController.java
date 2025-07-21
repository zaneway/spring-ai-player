package com.github.zaneway.controller;

import com.github.zaneway.ollama.ChatOllama;
import com.github.zaneway.ollama.FileHandler;
import com.github.zaneway.ollama.PkiOllama;
import com.github.zaneway.ollama.RagOllama;
import com.github.zaneway.ollama.TranslationOllama;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("ollama")
public class OllamaController {

  @Resource
  private ChatOllama chatOllama;

  @Resource
  private TranslationOllama translationOllama;

  @Resource
  private PkiOllama pkiOllama;

  @Resource
  private RagOllama ragOllama;
  @Resource
  private FileHandler fileHandler;

  @RequestMapping("chat")
  public String chat(@RequestBody ChatRequest msg){
    return chatOllama.chat(msg.getMsg());
  }


  @RequestMapping("translation")
  public String translation(@RequestBody ChatRequest msg){
    return translationOllama.translationChat(msg.getMsg());
  }

  @RequestMapping("pki")
  public String pki(@RequestBody ChatRequest msg){
    return pkiOllama.pkiChat(msg.getMsg());
  }


  @RequestMapping("chroma/add")
  public String chromaAdd(@RequestBody ChatRequest msg){
    ragOllama.add(msg.getMsg());
    return "success";
  }

  @RequestMapping("chroma/get")
  public String chromaGet(@RequestBody ChatRequest msg){
    return ragOllama.query(msg.getMsg());
  }


  @RequestMapping("chroma/addFile")
  public String addFile(@RequestBody FileRequest request){
    fileHandler.addFileToDB(request.getFilePath(), request.getSkipLine());
    return "success";
  }





}
