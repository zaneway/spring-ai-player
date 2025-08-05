package com.github.zaneway.file;

import com.github.zaneway.file.enums.FileType;
import com.github.zaneway.file.enums.FileTypeChoose;
import java.util.List;
import java.util.Map;
import org.springframework.ai.document.Document;
import org.springframework.ai.reader.markdown.MarkdownDocumentReader;
import org.springframework.ai.reader.markdown.config.MarkdownDocumentReaderConfig;
import org.springframework.ai.reader.markdown.config.MarkdownDocumentReaderConfig.Builder;
import org.springframework.core.io.Resource;

@FileTypeChoose(FileType.MD)
public class MarkdownFileHandler implements ParseFileHandler {

  @Override
  public List<Document> parseFile(Resource resource, Map<String, Object> additionalMetadata) {

    Builder builder = MarkdownDocumentReaderConfig.builder()
        .withHorizontalRuleCreateDocument(true)
        .withIncludeCodeBlock(false)
        .withIncludeBlockquote(false);
    if (additionalMetadata != null) {
      //withAdditionalMetadata方法不要调整顺序,注意内部实现
      builder.withAdditionalMetadata(additionalMetadata);
    }
    builder.withAdditionalMetadata("filename", resource.getFilename());
    MarkdownDocumentReader reader = new MarkdownDocumentReader(resource, builder.build());
    return reader.get();
  }
}
