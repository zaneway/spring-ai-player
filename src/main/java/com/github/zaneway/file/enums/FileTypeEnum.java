package com.github.zaneway.file.enums;

import java.util.Arrays;

public enum FileTypeEnum {

  PDF("pdf"),
  MD("md"),
  Word("docx","doc"),
  Excel("xlsx","xls","csv"),
  PPT("ppt","pptx","potx"),

  ;

  private String[] suffix;

  FileTypeEnum(String... suffix) {
    this.suffix = suffix;
  }

  public String[] getSuffix() {
    return suffix;
  }

  public static FileTypeEnum containsSuffix(String suffix) {

    return Arrays.stream(values())
        .filter(type -> Arrays.stream(type.getSuffix())
            .anyMatch(s -> s.equalsIgnoreCase(suffix)))
        .findFirst().orElseThrow(() -> new RuntimeException("no match of FileType" + suffix));
  }

}
