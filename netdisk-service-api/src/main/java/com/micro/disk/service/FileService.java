package com.micro.disk.service;

import com.micro.disk.bean.*;

import java.util.List;

public interface FileService {
    /**
     * 上传切块
     * @param chunk
     */
    public void uploadChunk(Chunk chunk);

    /**
     * 合并切块
     * @param bean
     */
    public void mergeChunk(MergeFileBean bean);

    public FileBean findOne(String fileid);

    public PageInfo<FileListBean> findFileList(Integer page, Integer limit, String userid, String pid, String typecode, String orderfield, String ordertype);
    FileBean addFile(String pid,String filename,byte[] bytes,String username,String userid);

    public void delete(String createuserid,String createusername,List<String> ids);
    public void rename(String fileid,String filename,String userid);

    public FolderPropertyBean findFolderProperty(String pid,String userid);
    void saveFromShare(String userid,String username, String folderid,String shareid, List<String> ids);
    void addFolder(String filename,String userid,String username,String pid);
    Integer checkFile(String filemd5);
}
