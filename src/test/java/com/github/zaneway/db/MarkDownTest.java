package com.github.zaneway.db;

import java.util.List;
import org.springframework.ai.document.Document;
import org.springframework.ai.reader.markdown.MarkdownDocumentReader;
import org.springframework.ai.reader.markdown.config.MarkdownDocumentReaderConfig;
import org.springframework.core.io.FileSystemResource;

public class MarkDownTest {


  public static void main(String[] args) {

    MarkdownDocumentReaderConfig config = MarkdownDocumentReaderConfig.builder()
        .withHorizontalRuleCreateDocument(true)
        .withIncludeCodeBlock(true )
        .build();
    FileSystemResource fileSystemResource = new FileSystemResource("/Users/zhangzhenwei/Downloads/Mermaid语法.md");
    MarkdownDocumentReader reader = new MarkdownDocumentReader(fileSystemResource,config);
    List<Document> documents = reader.get();
    System.out.println(documents);

  }

}
