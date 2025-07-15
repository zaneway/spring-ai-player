package com.github.zaneway.controller;

import com.github.zaneway.ollama.ChatOllama;
import com.github.zaneway.ollama.TranslationOllama;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("ollama")
public class ChatController {

  @Resource
  private ChatOllama chatOllama;

  @Resource
  private TranslationOllama translationOllama;

  @RequestMapping("chat")
  public String chat(@RequestBody ChatRequest msg){
    return chatOllama.chat(msg.getMsg());
  }


  @RequestMapping("translation")
  public String translation(@RequestBody ChatRequest msg){
    return translationOllama.pkiChat(msg.getMsg());
  }
}
