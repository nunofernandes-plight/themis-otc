package com.oxchains.basicService.files.tfsService;
import com.oxchains.basicService.files.entity.FileInfos;
import com.oxchains.basicService.files.exception.SaveFileExecption;

/**
 * Created by xuqi on 2017/12/1.
 */
public interface TFSService {
    public String saveTfsFile(FileInfos fileInfo) throws SaveFileExecption;
    public String saveTfsLargeFile(FileInfos fileInfo) throws SaveFileExecption;
    public FileInfos getTfsFile(String tfsFileName) throws SaveFileExecption;
    public boolean deleteTfsFile(String tfsFileName) throws Exception;
    public boolean hideTfsFile(String tfsFileName, int isHidden) throws Exception;
}
