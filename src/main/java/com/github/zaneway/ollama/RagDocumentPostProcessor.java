package com.github.zaneway.ollama;

import java.util.List;
import org.springframework.ai.chat.client.ChatClientRequest;
import org.springframework.ai.chat.client.advisor.api.AdvisorChain;
import org.springframework.ai.document.Document;
import org.springframework.ai.rag.Query;
import org.springframework.ai.rag.postretrieval.document.DocumentPostProcessor;
import org.springframework.ai.vectorstore.VectorStore;

public class RagDocumentPostProcessor implements DocumentPostProcessor {

  private VectorStore vectorStore;
  public RagDocumentPostProcessor(VectorStore vectorStore) {
    this.vectorStore = vectorStore;
  }

   /**
    * @author zhangzhenwei
    * @date 2025/8/19 18:24
    * @desc
    *
    * {@link org.springframework.ai.rag.advisor.RetrievalAugmentationAdvisor#before(ChatClientRequest, AdvisorChain)}  }
    *  140行中进行执行.
    */
  @Override
  public List<Document> process(Query query, List<Document> documents) {
    documents = vectorStore.similaritySearch(query.text());
    return documents;
  }

}
