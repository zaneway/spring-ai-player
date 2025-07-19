package com.github.zaneway.ollama;

import com.github.zaneway.prompt.PromptBuilder;
import jakarta.annotation.Resource;
import java.util.List;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.Generation;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.converter.ListOutputConverter;
import org.springframework.ai.embedding.EmbeddingResponse;
import org.springframework.ai.ollama.OllamaChatModel;
import org.springframework.ai.ollama.OllamaEmbeddingModel;
import org.springframework.core.convert.support.DefaultConversionService;
import org.springframework.stereotype.Component;

@Component
public class PkiOllama {

  @Resource
  private OllamaChatModel ollamaChatModel;
  @Resource
  private OllamaEmbeddingModel ollamaEmbeddingModel;

  public String pkiChat(String msg) {
    Prompt prompt = PromptBuilder.pkiPromptBuilder(msg);
    ollamaChatModel.call(prompt);

    //todo 可以自定义实现 Converter
    List<String> result = ChatClient.create(ollamaChatModel).prompt(prompt).call()
        .entity(new ListOutputConverter(new DefaultConversionService()));
    return result.get(0);

  }


  public String pkiChat1(String msg) {
    Prompt prompt = PromptBuilder.pkiPromptBuilder(msg);
    Generation result = ollamaChatModel.call(prompt).getResult();

    ListOutputConverter listOutputConverter = new ListOutputConverter();
    String text = result.getOutput().getText();
    List<String> convert = listOutputConverter.convert(text);
    EmbeddingResponse embeddingResponse = ollamaEmbeddingModel.embedForResponse(convert);
    embeddingResponse.toString();
    return convert.get(0);

  }
}
