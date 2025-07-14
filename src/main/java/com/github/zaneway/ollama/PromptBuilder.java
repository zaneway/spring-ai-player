package com.github.zaneway.ollama;

import java.util.Map;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;

public class PromptBuilder {

  public static Prompt pkiBuilder(String data){
    PromptTemplate promptTemplate = new PromptTemplate("作为PKI体系专家,解答以下问题:{request}.如果问题不是PKI领域的");
    return promptTemplate.create(Map.of("request", data));
  }

}
