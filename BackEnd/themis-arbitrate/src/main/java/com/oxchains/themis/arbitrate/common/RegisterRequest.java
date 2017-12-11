package com.oxchains.themis.arbitrate.common;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.Size;

@Slf4j
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class RegisterRequest {
  private MultipartFile multipartFile;
  private String id;
  private Long userId;
  @Size(max = 200,message = "上限200字")
  private String content;
  private String fileName;
  private String image;
  private MultipartFile[] files;

  public MultipartFile[] getFiles() {
    return files;
  }

  public void setFiles(MultipartFile[] files) {
    this.files = files;
  }

  public String getFileName() {
    return fileName;
  }

  public void setFileName(String fileName) {
    this.fileName = fileName;
  }

  public Long getUserId() {
    return userId;
  }

  public void setUserId(Long userId) {
    this.userId = userId;
  }

  public String getContent() {
    return content;
  }

  public void setContent(String content) {
    this.content = content;
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public MultipartFile getMultipartFile() {
    return multipartFile;
  }

  public void setMultipartFile(MultipartFile multipartFile) {
    this.multipartFile = multipartFile;
  }

}
