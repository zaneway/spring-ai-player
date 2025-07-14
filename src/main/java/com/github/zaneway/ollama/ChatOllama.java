package com.github.zaneway.ollama;

import jakarta.annotation.Resource;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.ChatClient.Builder;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.memory.MessageWindowChatMemory;
import org.springframework.ai.ollama.OllamaChatModel;
import org.springframework.stereotype.Component;

@Component
public class ChatOllama {

  @Resource
  private OllamaChatModel ollamaChatModel;

  public String chat(String msg){
    Builder builder = ChatClient.builder(ollamaChatModel);
    MessageWindowChatMemory chatMemory = MessageWindowChatMemory.builder().maxMessages(1000).build();
    //todo 可以基于Advisor制作 RAG
    builder.defaultAdvisors(MessageChatMemoryAdvisor.builder(chatMemory).build());
    ChatClient client = builder.build();
    return client.prompt(msg).call().content();
  }


}
