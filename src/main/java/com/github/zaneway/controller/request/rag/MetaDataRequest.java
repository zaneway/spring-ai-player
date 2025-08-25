package com.github.zaneway.controller.request.rag;

import com.github.zaneway.controller.request.ChromaRequest;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author zhangzhenwei
 * @date 2025/8/22 16:09
 * @desc
 *  入参格式
 * {
 * 	"id":"uuid",
 *   "title": "GMT-0034-2014 基于SM2密码算法的证书认证系统密码及其相关安全技术规范",
 *   "date": "2014",
 *   "tags": ["SM2", "PKI", "国密标准"],
 * 	"metadata":{
 * 			"text":"文本内容",
 * 			"chapter": "第1章 总则"
 *            }
 *  }
 */
@Getter
@Setter
@AllArgsConstructor
@ToString
public class MetaDataRequest extends ChromaRequest {

  private String title;

  private String date;

  private String tags;

  private List<Data> datas;


}
