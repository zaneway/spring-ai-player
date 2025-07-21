package com.github.zaneway.controller;

import lombok.Data;

@Data
public class FileRequest {
  private String filePath;

  private int skipLine;
}
