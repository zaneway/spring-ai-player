package com.github.zaneway.ollama;

import jakarta.annotation.Resource;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.ollama.OllamaChatModel;
import org.springframework.stereotype.Component;

@Component
public class ChatOllama {

  @Resource
  private OllamaChatModel ollamaChatModel;

  public String chat(String msg){
    ChatClient client = ChatClient.builder(ollamaChatModel).build();
    String content = client.prompt(msg).call().content();
    System.out.println(content);
    return content;
  }


}
