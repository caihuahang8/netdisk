package com.micro.db.dao;

import com.micro.model.DiskFile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface DiskFileDao  extends JpaRepository<DiskFile, String>, JpaSpecificationExecutor<DiskFile> {
    /**
     * 文件夹-不能有重复【文件夹上传】
     * @param userid
     * @param pid
     * @param filename
     * @param filetype
     * @return
     */
    @Query("select t from DiskFile t where createuserid=?1 and pid=?2 and filename=?3 and filetype=0")
    public DiskFile findFolder(String userid,String pid,String filename);


    /**
     * 文件-不能重复【上传】
     * @param userid
     * @param pid
     * @param filename
     * @param filemd5
     * @return
     */
    @Query("select t from DiskFile t where createuserid=?1 and pid=?2 and filename=?3 and filemd5=?4 and filetype=1")
    public DiskFile findFile(String userid,String pid,String filename,String filemd5);

    /**
     * 更新字段
     * @param thumbnailurl
     * @param imgsize
     * @param md5
     */
    @Modifying
    @Query("update DiskFile set thumbnailurl=?1,imgsize=?2 where id=?3")
    public void updateField(String thumbnailurl,String imgsize,String id);

    @Modifying
    @Query("update DiskFile set filemd5=?1,filesize=?2 where id=?3")
    public void updateField(String filemd5,long filesize,String id);

    @Query("select t from DiskFile t where createuserid=?1 and pid=?2 order by createtime asc")
    public List<DiskFile> findListByPid(String userid, String pid);

    /**
     * 文件是否存在
     * @param userid
     * @param pid
     * @param fileid
     * @param filename
     * @param filetype
     * @return
     */
    //userid pid fileid filename filetype
    @Query("select count(1) from DiskFile t where createuserid=?1 and pid=?2 and id!=?3 and filename=?4 and filetype=?5")
    Integer findFilenameIsExist(String userid,String pid,String fileid,String filename,Integer filetype);

    //userid fileid
    @Modifying
    @Query("update DiskFile set filename=?1 where createuserid=?2 and id=?3")
    void updateFilenameWithUseridAndFileid(String filename,String userid,String fileid);

    @Query("select t from DiskFile t where createuserid=?1 and pid=?2 and filename=?3 and filetype=0")
    public DiskFile findFolderNameIsExist(String userid,String pid,String name);
    /**
     * 复制、移动、数据还原、分享转存--控制名称不能相同
     * @param userid
     * @param pid
     * @param filemd5
     * @param name
     * @return
     */
    @Query("select t from DiskFile t where createuserid=?1 and pid=?2 and filemd5=?3 and filename=?4 and filetype=1")
    public DiskFile findFileNameIsExist(String userid,String pid,String filemd5,String name);
}
