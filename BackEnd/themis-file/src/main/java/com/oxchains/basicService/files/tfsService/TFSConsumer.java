package com.oxchains.basicService.files.tfsService;
import com.alibaba.dubbo.config.annotation.Reference;
import com.oxchains.basicService.files.entity.FileInfos;
import com.oxchains.basicService.files.exception.SaveFileExecption;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
/**
 * Created by xuqi on 2017/12/1.
 */
@Service
public class TFSConsumer {
     private final Logger LOG = LoggerFactory.getLogger(this.getClass());
     @Reference(version = "1.0.0")
     TFSService tfsService;
     @Value("${oxchians.appKey}")
     private Integer appKey;
    //将文件存入文件服务器 并返回一个新的文件name
    public String saveTfsFile(MultipartFile multipartFile,Long userId){
        String s = null;
        try {
            if(this.checkParam(multipartFile,userId,appKey)){
                FileInfos fileInfos = new FileInfos();
                fileInfos.setFile(multipartFile.getBytes());
                fileInfos.setFilename(multipartFile.getOriginalFilename());
                fileInfos.setAppKey(appKey);
                fileInfos.setUserId(userId);
                fileInfos.setLength(multipartFile.getBytes().length);
                s = tfsService.saveTfsFile(fileInfos);
            }
        } catch (Exception e) {
            LOG.error("save file faild ",e);
            return null;
        }
        return s;
    }
    public String saveTfsFile(byte[] bytes,String filename,Long userId) {
        String s = null;
        try {
            if(this.checkParam(bytes,filename,userId,appKey)){
                FileInfos fileInfos = new FileInfos();
                fileInfos.setFile(bytes);
                fileInfos.setFilename(filename);
                fileInfos.setAppKey(appKey);
                fileInfos.setUserId(userId);
                fileInfos.setLength(bytes.length);
                s = tfsService.saveTfsFile(fileInfos);
            }
        } catch (SaveFileExecption e) {
            LOG.error("save file faild",e);
            return null;
        }
        return s;
    }
    public String saveTfsLargeFile(MultipartFile multipartFile,Long userId) {
        String s = null;
        try {
            if(this.checkParam(multipartFile,userId,appKey)){
                FileInfos fileInfos = new FileInfos();
                fileInfos.setFile(multipartFile.getBytes());
                fileInfos.setFilename(multipartFile.getOriginalFilename());
                fileInfos.setAppKey(appKey);
                fileInfos.setUserId(userId);
                fileInfos.setLength(multipartFile.getBytes().length);
                s = tfsService.saveTfsLargeFile(fileInfos);
            }
        } catch (Exception e) {
            LOG.error("save large file faild ",e);
            return null;
        }
        return s;
    }
    public String saveTfsLargeFile(byte[] bytes,String filename,Long userId) {
        String s = null;
        try {
            if(this.checkParam(bytes,filename,userId,appKey)){
                FileInfos fileInfos = new FileInfos();
                fileInfos.setFile(bytes);
                fileInfos.setFilename(filename);
                fileInfos.setAppKey(appKey);
                fileInfos.setUserId(userId);
                fileInfos.setLength(bytes.length);
                s = tfsService.saveTfsLargeFile(fileInfos);
            }
        } catch (SaveFileExecption e) {
            LOG.error("save large file faild ",e);
            return null;
        }
        return s;
    }

    //从文件服务器读取文件
    public FileInfos getTfsFile(String tfsFileName) {
        FileInfos tfsFile = null;
        try {
            if(this.checkParam(tfsFileName)){
                tfsFile = tfsService.getTfsFile(tfsFileName);
            }
        } catch (Exception e) {
            LOG.error("get file faild ",e);
            return null;
        }
        return tfsFile;
    }
    private boolean checkParam(Object ...objects){
        for (Object obj:objects) {
            if(objects == null){
              return false;
            }
        }
        return true;
    }


}
