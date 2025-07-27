package com.github.zaneway.controller.request;

import lombok.Data;

@Data
public class FileRequest extends ChromaRequest {

  private String filePath;

  private int skipLine;


}
