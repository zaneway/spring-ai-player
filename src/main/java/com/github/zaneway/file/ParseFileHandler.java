package com.github.zaneway.file;

import java.util.List;
import java.util.Map;
import org.springframework.ai.document.Document;
import org.springframework.core.io.Resource;

public interface ParseFileHandler {

  /**
   * 根据不同文件类型解析文件
   * @param resource
   * @param additionalMetadata
   * @return
   */
  List<Document> parseFile(Resource resource, Map<String, Object> additionalMetadata);

}
