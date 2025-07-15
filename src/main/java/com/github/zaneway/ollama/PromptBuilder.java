package com.github.zaneway.ollama;

import java.util.List;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.SystemPromptTemplate;

public class PromptBuilder {

  public static Prompt translationPromptBuilder(String data){
    SystemPromptTemplate template = new SystemPromptTemplate("作为多国语言专家,先判断输入的词或句子是否是中文,如果是中文,就翻译成英语;如果非中文,就翻译成中文");
    Message message = template.createMessage();
    UserMessage userMessage = new UserMessage(data);
    return new Prompt(List.of(userMessage,message));
  }

}
