package com.github.zaneway.vector.chroma;

import jakarta.annotation.Resource;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.springframework.ai.chroma.vectorstore.ChromaApi;
import org.springframework.ai.document.Document;
import org.springframework.ai.ollama.OllamaEmbeddingModel;
import org.springframework.ai.reader.ExtractedTextFormatter;
import org.springframework.ai.reader.tika.TikaDocumentReader;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.core.io.FileSystemResource;
import org.springframework.stereotype.Component;

@Component
public class ChromaDB {

  @Resource
  private OllamaEmbeddingModel ollamaEmbeddingModel;
  @Resource
  private ChromaApi chromaApi;
  @Resource
  private VectorStore vectorStore;

  public void add(String data) {
    vectorStore.add(List.of(new Document(data)));
  }

  public void addFile(String filePath, int skipLine) {

//    Pattern chapterPattern = Pattern.compile("^第\\d+章.*"); // 你可以改成 "Chapter \\d+" 或别的格式
    //数字标题
    try {
      Pattern chapterPattern = Pattern.compile("^\\d+(\\.\\d+)*\\s+.*$");

      FileSystemResource from = new FileSystemResource(filePath);
      //读取文件,跳过文件前xxx页
      TikaDocumentReader reader = new TikaDocumentReader(from,
          ExtractedTextFormatter.builder().withNumberOfTopTextLinesToDelete(skipLine).build());

      //过滤出目录名称
      XWPFDocument document = new XWPFDocument(new FileInputStream(filePath));
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
      vectorStore.add(result);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }


  }


  public List<Document> query(String data) {
    List<Document> documents = vectorStore.similaritySearch(data);
    return documents;
  }


}
