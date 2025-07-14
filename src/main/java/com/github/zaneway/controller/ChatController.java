package com.github.zaneway.controller;

import com.github.zaneway.ollama.ChatOllama;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("ollama")
public class ChatController {

  @Resource
  private ChatOllama chatOllama;

  @RequestMapping("chat")
  public String chat(@RequestBody ChatRequest msg){
    return chatOllama.chat(msg.getMsg());
  }



}
