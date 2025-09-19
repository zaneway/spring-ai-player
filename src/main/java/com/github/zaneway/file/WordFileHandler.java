package com.github.zaneway.file;

import com.github.zaneway.file.enums.FileTypeEnum;
import com.github.zaneway.file.enums.FileTypeChoose;
import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.springframework.ai.document.Document;
import org.springframework.ai.reader.ExtractedTextFormatter;
import org.springframework.ai.reader.tika.TikaDocumentReader;
import org.springframework.core.io.Resource;

@FileTypeChoose(FileTypeEnum.Word)
public class WordFileHandler implements ParseFileHandler {

  @Override
  public List<Document> parseFile(Resource resource, Map<String, Object> additionalMetadata) {
    try {
      // 使用Tika读取整个文档内容
      TikaDocumentReader reader = new TikaDocumentReader(resource,
          ExtractedTextFormatter.builder().withNumberOfTopTextLinesToDelete(0).build());
      
      List<Document> tikaDocuments = reader.get();
      if (tikaDocuments.isEmpty()) {
        return new ArrayList<>();
      }
      
      Document fullDocument = tikaDocuments.get(0);
      String fullText = fullDocument.getText();
      
      // 读取Word文档结构
      XWPFDocument document = new XWPFDocument(resource.getInputStream());
      List<XWPFParagraph> paragraphs = document.getParagraphs();
      
      // 提取标题信息
      List<TitleInfo> titles = extractTitles(paragraphs);
      
      // 根据标题分割文档
      List<Document> result = splitDocumentByTitles(fullText, titles, fullDocument.getMetadata());
      
      return result;
    } catch (IOException e) {
      throw new RuntimeException("Failed to parse Word document", e);
    }
  }
  
  /**
   * 提取文档中的标题信息
   */
  private List<TitleInfo> extractTitles(List<XWPFParagraph> paragraphs) {
    List<TitleInfo> titles = new ArrayList<>();
    
    for (int i = 0; i < paragraphs.size(); i++) {
      XWPFParagraph paragraph = paragraphs.get(i);
      String text = paragraph.getText();
      
      if (text != null && !text.trim().isEmpty()) {
        // 检查是否为标题（通过样式或编号）
        String style = paragraph.getStyle();
        BigInteger numID = paragraph.getNumID();
        
        int level = determineHeadingLevel(style, numID, text);
        if (level > 0) {
          titles.add(new TitleInfo(text.trim(), level, i));
        }
      }
    }
    
    return titles;
  }
  
  /**
   * 确定标题级别
   */
  private int determineHeadingLevel(String style, BigInteger numID, String text) {
    // 方法1: 通过样式判断 (Heading 1, Heading 2, etc.)
    if (style != null && style.toLowerCase().startsWith("heading")) {
      try {
        return Integer.parseInt(style.substring(7).trim());
      } catch (NumberFormatException e) {
        return 1; // 默认为1级标题
      }
    }
    
    // 方法2: 通过编号判断
    if (numID != null) {
      BigInteger ilvl = getNumberingLevel(numID);
      return ilvl != null ? ilvl.intValue() + 1 : 1;
    }
    
    // 方法3: 通过正则表达式匹配
    Pattern headingPattern = Pattern.compile("^(第?[1-9][0-9]*章|[1-9][0-9]*\\.([1-9][0-9]*\\.?)*).*");
    if (headingPattern.matcher(text.trim()).matches()) {
      return 1; // 简化处理，都视为1级标题
    }
    
    return 0; // 不是标题
  }
  
  /**
   * 获取编号级别
   */
  private BigInteger getNumberingLevel(BigInteger numID) {
    // 这里可以实现更复杂的编号级别解析逻辑
    // 简化处理，直接返回默认值
    return BigInteger.ONE;
  }
  
  /**
   * 根据标题分割文档
   */
  private List<Document> splitDocumentByTitles(String fullText, List<TitleInfo> titles, Map<String, Object> baseMetadata) {
    List<Document> result = new ArrayList<>();
    
    if (titles.isEmpty()) {
      // 如果没有找到标题，返回整个文档
      result.add(new Document(fullText, baseMetadata));
      return result;
    }
    
    // 处理第一个标题之前的内容
    TitleInfo firstTitle = titles.get(0);
    int firstTitleIndex = fullText.indexOf(firstTitle.title);
    if (firstTitleIndex > 0) {
      String introText = fullText.substring(0, firstTitleIndex).trim();
      if (!introText.isEmpty()) {
        Map<String, Object> metadata = new HashMap<>(baseMetadata);
        metadata.put("section", "introduction");
        metadata.put("level", 0);
        result.add(new Document(introText, metadata));
      }
    }
    
    // 处理各标题之间的内容
    for (int i = 0; i < titles.size(); i++) {
      TitleInfo currentTitle = titles.get(i);
      String currentTitleText = currentTitle.title;
      
      int startIndex = fullText.indexOf(currentTitleText);
      if (startIndex == -1) continue;
      
      int endIndex;
      if (i < titles.size() - 1) {
        TitleInfo nextTitle = titles.get(i + 1);
        endIndex = fullText.indexOf(nextTitle.title);
        if (endIndex == -1) {
          endIndex = fullText.length();
        }
      } else {
        endIndex = fullText.length();
      }
      
      String sectionText = fullText.substring(startIndex, endIndex).trim();
      if (!sectionText.isEmpty()) {
        Map<String, Object> metadata = new HashMap<>(baseMetadata);
        metadata.put("section", currentTitleText);
        metadata.put("level", currentTitle.level);
        result.add(new Document(sectionText, metadata));
      }
    }
    
    return result;
  }
  
  /**
   * 标题信息类
   */
  private static class TitleInfo {
    final String title;
    final int level;
    final int paragraphIndex;
    
    TitleInfo(String title, int level, int paragraphIndex) {
      this.title = title;
      this.level = level;
      this.paragraphIndex = paragraphIndex;
    }
  }
}