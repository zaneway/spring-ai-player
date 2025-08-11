package com.github.zaneway.file;

import com.github.zaneway.file.enums.FileTypeEnum;
import com.github.zaneway.file.enums.FileTypeChoose;
import java.util.Map;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

@Component
public class FileTypeAdapter implements ApplicationContextAware {

  private ApplicationContext context;

  public ParseFileHandler getBean(String suffix) {
    FileTypeEnum typeEnum = FileTypeEnum.containsSuffix(suffix);
    Map<String, ParseFileHandler> beans = context.getBeansOfType(ParseFileHandler.class);
    for (ParseFileHandler value : beans.values()) {
      FileTypeEnum fileTypeEnum = value.getClass().getAnnotation(FileTypeChoose.class).value();
      if (fileTypeEnum == typeEnum) {
        return value;
      }
    }
    return null;
  }

  @Override
  public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
    this.context = applicationContext;
  }
}
