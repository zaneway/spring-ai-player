package com.github.zaneway.db;

import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.springframework.ai.document.Document;
import org.springframework.ai.reader.ExtractedTextFormatter;
import org.springframework.ai.reader.tika.TikaDocumentReader;
import org.springframework.core.io.FileSystemResource;

public class TikaTest {

  public static void main(String[] args) throws Exception {
    // 文件路径
    FileSystemResource from = new FileSystemResource("/Downloads/20250719.docx");

    TikaDocumentReader reader = new TikaDocumentReader(from, ExtractedTextFormatter.builder().withNumberOfTopTextLinesToDelete(90).build());
    List<Document> documents = reader.get();

//    Pattern chapterPattern = Pattern.compile("^第\\d+章.*"); // 你可以改成 "Chapter \\d+" 或别的格式
    //数字标题
    Pattern chapterPattern = Pattern.compile("^\\d+(\\.\\d+)*\\s+.*$");

    XWPFDocument document = new XWPFDocument(
        new FileInputStream("/Downloads/20250719.docx"));
    List<XWPFParagraph> paragraphs = document.getParagraphs();
    ArrayList<String> objects = new ArrayList<>();
    for (XWPFParagraph para : paragraphs) {
      String trim = para.getText().trim();
      if (chapterPattern.matcher(trim).matches()) {
        String[] s = trim.split(" ");
        if (s.length > 1) {
          objects.add(s[1].split("\t")[0]);
        } else {
          objects.add(trim.split("\t")[1]);
        }
      }
    }
    ArrayList<String> strings = new ArrayList<>();
    String text = documents.get(0).getText();
    int  end = 0;
    for (int i = 4; i < objects.size(); i++) {
      String regex = objects.get(i);
      end = text.indexOf(regex);
      String substring = text.substring(0, end);
      strings.add(substring);
      text = text.substring(end, text.length());
    }
    System.out.println(strings);


  }

}
