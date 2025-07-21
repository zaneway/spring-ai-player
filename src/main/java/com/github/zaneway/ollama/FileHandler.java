package com.github.zaneway.ollama;

import com.github.zaneway.vector.chroma.ChromaDB;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Component;

@Component
public class FileHandler {

  @Resource
  private ChromaDB chromaDB;

  public void addFileToDB(String filePath, int skipLine){
    chromaDB.addFile(filePath, skipLine);
  }



}
