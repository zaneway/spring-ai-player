package com.github.zaneway.db;

import java.io.File;
import org.apache.tika.Tika;

public class TikaTest {

  public static void main(String[] args) throws Exception {
    Tika tika = new Tika();
    File file = new File("/Users/zhangzhenwei/Downloads/20250719.docx");
    String detect = tika.detect(file);
    //解析成字符串
    String s = tika.parseToString(file);
    System.out.println(detect);
  }

}
