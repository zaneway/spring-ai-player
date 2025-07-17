package com.github.zaneway.controller;

import com.github.zaneway.ollama.ChatOllama;
import com.github.zaneway.ollama.PkiOllama;
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

}
