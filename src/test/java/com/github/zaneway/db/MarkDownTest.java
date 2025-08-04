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
        .withIncludeCodeBlock(false)
        .withIncludeBlockquote(false)
        .withAdditionalMetadata("filename",
            "GMT 0034 基于SM2密码算法的证书认证系统密码及其相关安全技术规范")
        .build();
    //user.dir是当前目录
    //user.home 是当前用户的基目录
    String baseDir = System.getProperty("user.home");
    FileSystemResource fileSystemResource = new FileSystemResource(
        baseDir + "/Library/Mobile Documents/iCloud~md~obsidian/Documents/wegoo-node/学习/CA/GMT 0034-2014 基于SM2密码算法的证书认证系统密码及其相关安全技术规范.md");

    String filename = fileSystemResource.getFilename();
    System.out.println(filename);
    MarkdownDocumentReader reader = new MarkdownDocumentReader(fileSystemResource, config);
    List<Document> documents = reader.get();
    System.out.println(documents);

  }

}
