package com.github.zaneway.file;

import java.util.List;
import java.util.Map;
import org.springframework.ai.document.Document;
import org.springframework.core.io.Resource;

public interface ParseFileHandler {


  List<Document> parseFile(Resource resource, Map<String, Object> additionalMetadata);

}
