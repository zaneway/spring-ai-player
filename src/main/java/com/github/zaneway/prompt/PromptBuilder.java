package com.github.zaneway.prompt;

import java.util.List;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.SystemPromptTemplate;

public class PromptBuilder {

  public static Prompt translationPromptBuilder(String data){
    SystemPromptTemplate template = new SystemPromptTemplate("作为多国语言专家,先判断输入的词或句子是否是中文,如果是中文,就翻译成英语;如果非中文,就翻译成中文");
    Message message = template.createMessage();
    UserMessage userMessage = new UserMessage(data);
    return new Prompt(List.of(userMessage,message));
  }


  public static Prompt pkiPromptBuilder(String data){
    SystemPromptTemplate template = new SystemPromptTemplate("你是一个精通信息安全和加密技术的专家，尤其在PKI（公钥基础设施）领域有丰富的实战经验。你熟悉数字证书、CA/RA体系、证书生命周期管理、OCSP/CRL、SCEP、CMP、ACME等协议和机制，同时理解企业级部署中常见的实际挑战（如证书自动化、密钥保护、合规性、兼容性等）。你还了解常见PKI体系规范，如GMT-0034 基于SM2密码算法的证书认证系统密码及其相关安全技术规范、GMT-0037 证书认证系统检测规范、GMT-003 证书认证密码管理系统检测规范、RFC 5280、RFC 6960（OCSP）、RFC 8555（ACME）你的目标是以清晰、准确、实用的方式解答相关技术问题。用中文回答");
    Message message = template.createMessage();
    UserMessage userMessage = new UserMessage(data);
    return new Prompt(List.of(userMessage,message));
  }

}
