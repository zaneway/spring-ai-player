package com.github.zaneway.ollama;

import com.github.zaneway.vector.chroma.ChromaDB;
import jakarta.annotation.Resource;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.ChatClient.Builder;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.memory.MessageWindowChatMemory;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.ollama.OllamaChatModel;
import org.springframework.ai.rag.advisor.RetrievalAugmentationAdvisor;
import org.springframework.ai.rag.generation.augmentation.ContextualQueryAugmenter;
import org.springframework.ai.rag.retrieval.search.VectorStoreDocumentRetriever;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.stereotype.Component;

@Component
public class ChatOllama {

  @Resource
  private OllamaChatModel ollamaChatModel;

  @Resource
  private ChromaDB chromaDB;

  public String chat(String msg) {

    String prompt  ="以下提供了与用户问题相关的上下文信息，请严格遵循以下规则进行回答：\n"
        + "---------------------\n"
        + "{context}\n"
        + "---------------------\n"
        + "根据以上信息回答用户问题:\n"
        + "{query}\n"
        +"要求: \n"
        +"1. 如果上下文中包含答案，请**优先基于上下文**进行回答，并尽量引用上下文中的表述。  \n"
        + "2. 如果上下文中没有相关答案，可以基于你已有的知识回答，但请保持**简洁、准确**，避免无根据的推测。  ";
//        + "3. 如果无法确定答案，请明确告诉用户“根据现有信息无法确定”。  ";

    Builder builder = ChatClient.builder(ollamaChatModel);
    MessageWindowChatMemory chatMemory = MessageWindowChatMemory.builder().maxMessages(1000)
        .build();

    VectorStore vector = chromaDB.getDefaultVector();

    //实现rag
    VectorStoreDocumentRetriever.Builder retriever = VectorStoreDocumentRetriever.builder()
        .vectorStore(vector).similarityThreshold(0.8).topK(5);

    //上下文处理
    ContextualQueryAugmenter contextualQueryAugmenter = ContextualQueryAugmenter.builder()
        //允许查询为空
        .allowEmptyContext(true)
        .promptTemplate(new PromptTemplate(prompt))
        .build();
    //构造advisor
    RetrievalAugmentationAdvisor retrievalAugmentationAdvisor = RetrievalAugmentationAdvisor.builder()
        .documentRetriever(retriever.build())
        .queryAugmenter(contextualQueryAugmenter)
        //需要添加PostProcessor进行查询,将查询结果添加到prompt的query中
        .documentPostProcessors(new RagDocumentPostProcessor(vector))
        .build();

    ChatClient client = builder.build();
    MessageChatMemoryAdvisor messageChatMemoryAdvisor = MessageChatMemoryAdvisor.builder(chatMemory).build();
    return client.prompt().user(msg).advisors(messageChatMemoryAdvisor,
        retrievalAugmentationAdvisor).call().chatResponse().toString();
  }


}
