package com.micro.handler;

import com.micro.core.ContextRequest;
import com.micro.core.ContextResponse;
import com.micro.core.Handler;
import com.micro.db.dao.DiskFileDao;
import com.micro.model.DiskFile;
import com.micro.param.MergeRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
@Component
public class MergeCreateFolderHandler extends Handler {
    @Autowired
    private DiskFileDao diskFileDao;

    @Override
    public void doHandler(ContextRequest request, ContextResponse response) {
        if (request instanceof MergeRequest) {
            MergeRequest bean = (MergeRequest) request;
            String relativepath = bean.getRelativepath();
            if (!StringUtils.isEmpty(relativepath)) {
                String[] names = relativepath.split("/");
                String userid = bean.getUserid();
                String username = bean.getUsername();
                String pid = bean.getPid();
                List<DiskFile> folders = new ArrayList<>();

                try {
                    for (int i = 0; i < names.length - 1; i++) {
                        String name=names[i];
                        DiskFile df = diskFileDao.findFolder(userid, pid, name);
                        if (df == null) {
                            df = new DiskFile();
                            df.setFilename(name);
                            df.setPid(pid);
                            df.setFiletype(0);
                            df.setFilesize(0);
                            df.setCreateuserid(userid);
                            df.setCreateusername(username);
                            df.setCreatetime(new Date());
                            diskFileDao.save(df);
                        }
                        pid = df.getId();
                        folders.add(df);
                    }

                    // 写到下一个Handler
                    bean.setPid(pid);
                    bean.setFolders(folders);
                    this.updateRequest(bean);

                } catch (Exception e) {
                    throw new RuntimeException("MergeCreateFolderHandler=="+e.getMessage());
                }
            }
        } else {
            throw new RuntimeException("MergeCreateFolderHandler==参数不对");
        }
    }
}
