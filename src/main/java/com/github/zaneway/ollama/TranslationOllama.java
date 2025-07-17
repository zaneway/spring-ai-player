package com.github.zaneway.ollama;

import com.github.zaneway.prompt.PromptBuilder;
import jakarta.annotation.Resource;
import java.util.List;
import org.springframework.ai.chat.model.Generation;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.ollama.OllamaChatModel;
import org.springframework.stereotype.Component;

@Component
public class TranslationOllama {

  @Resource
  private OllamaChatModel ollamaChatModel;


  public String translationChat(String msg){
    Prompt prompt = PromptBuilder.translationPromptBuilder(msg);
    List<Generation> results = ollamaChatModel.call(prompt).getResults();
    return results.get(0).getOutput().getText();

  }


}
