package com.github.zaneway.controller;

import com.github.zaneway.ollama.ChatOllama;
import com.github.zaneway.ollama.PkiOllama;
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
  private PkiOllama pkiOllama;

  @RequestMapping("chat")
  public String chat(@RequestBody ChatRequest msg){
    return chatOllama.chat(msg.getMsg());
  }


  @RequestMapping("pki")
  public String pki(@RequestBody ChatRequest msg){
    return pkiOllama.pkiChat(msg.getMsg());
  }
}
