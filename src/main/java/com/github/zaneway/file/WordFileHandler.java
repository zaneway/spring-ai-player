package com.github.zaneway.file;

import com.github.zaneway.file.enums.FileTypeEnum;
import com.github.zaneway.file.enums.FileTypeChoose;
import java.io.IOException;
import java.util.ArrayList;
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
public class WordFileHandler implements ParseFileHandler{

  @Override
  public List<Document> parseFile(Resource resource, Map<String, Object> additionalMetadata) {

//    Pattern chapterPattern = Pattern.compile("^第\\d+章.*"); // 你可以改成 "Chapter \\d+" 或别的格式
    //数字标题
    try {
      Pattern chapterPattern = Pattern.compile("^\\d+(\\.\\d+)*\\s+.*$");

      //读取文件,跳过文件前xxx页
      TikaDocumentReader reader = new TikaDocumentReader(resource,
          ExtractedTextFormatter.builder().withNumberOfTopTextLinesToDelete(90).build());

      //过滤出目录名称
      XWPFDocument document = new XWPFDocument(resource.getInputStream());
      List<XWPFParagraph> documents = document.getParagraphs();
      ArrayList<String> titles = new ArrayList<>();
      for (XWPFParagraph para : documents) {
        String trim = para.getText().trim();
        if (chapterPattern.matcher(trim).matches()) {
          String[] s = trim.split(" ");
          if (s.length > 1) {
            titles.add(s[1].split("\t")[0]);
          } else {
            titles.add(trim.split("\t")[1]);
          }
        }
      }
      //按照目录截取文件
      ArrayList<Document> result = new ArrayList<>();
      Document doc = reader.get().get(0);

      String text = doc.getText();
      int end = 0;
      for (int i = 4; i < titles.size(); i++) {
        String regex = titles.get(i);
        end = text.indexOf(regex);
        String substring = text.substring(0, end);
        result.add(new Document(substring, doc.getMetadata()));
        text = text.substring(end, text.length());
      }
      return result;
    } catch (IOException e) {
      throw new RuntimeException(e);
    }

  }
}
