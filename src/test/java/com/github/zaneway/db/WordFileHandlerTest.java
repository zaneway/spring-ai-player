package com.github.zaneway.db;

import com.github.zaneway.file.WordFileHandler;
import java.io.File;
import java.util.List;
import org.springframework.ai.document.Document;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;

public class WordFileHandlerTest {

  public static void main(String[] args) {
    try {
      // 创建WordFileHandler实例
      WordFileHandler handler = new WordFileHandler();
      
      // 准备测试文件（请确保此文件存在）
      File testFile = new File("/Users/zhangzhenwei/Downloads/0037.docx");
      if (!testFile.exists()) {
        System.out.println("测试文件不存在: " + testFile.getAbsolutePath());
        return;
      }
      
      Resource resource = new FileSystemResource(testFile);
      
      // 调用解析方法
      List<Document> documents = handler.parseFile(resource, null);
      
      // 输出结果
      System.out.println("解析出的文档数量: " + documents.size());
      for (int i = 0; i < documents.size(); i++) {
        Document doc = documents.get(i);
        System.out.println("=== 文档 " + (i + 1) + " ===");
        System.out.println("内容预览: " + doc.getText().substring(0, Math.min(200, doc.getText().length())) + "...");
        System.out.println("元数据: " + doc.getMetadata());
        System.out.println();
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}