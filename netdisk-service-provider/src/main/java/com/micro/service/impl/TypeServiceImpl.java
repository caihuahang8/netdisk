package com.micro.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.micro.db.dao.DiskTypeDao;
import com.micro.db.dao.DiskTypeSuffixDao;
import com.micro.disk.service.TypeService;
import com.micro.model.DiskType;
import com.micro.model.DiskTypeSuffix;
import com.micro.xml.TypeSuffixXml;
import com.micro.xml.TypeXml;
import com.micro.xml.XstreamUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service(interfaceClass=TypeService.class)
@Transactional
@Component
public class TypeServiceImpl implements TypeService {

    @Autowired
    private DiskTypeDao diskTypeDao;
    @Autowired
    private DiskTypeSuffixDao diskTypeSuffixDao;
    @Override
    public void init() {
        //类型
        List<TypeXml> types= XstreamUtils.parseTypexml();
        for(TypeXml type:types){
            DiskType diskType = new DiskType();
            diskType.setCode(type.getCode());
            diskType.setName(type.getName());
            Integer count =diskTypeDao.findCount(type.getCode());
            if (count==0){
                diskTypeDao.save(diskType);
            }
        }
        //类型明细
        List<TypeSuffixXml> details=XstreamUtils.parseTypeDetailxml();
        for (TypeSuffixXml detail:details){
            DiskTypeSuffix typeSuffix = new DiskTypeSuffix();
            typeSuffix.setTypecode(detail.getTypecode());
            typeSuffix.setName(detail.getName());
            typeSuffix.setSuffix(detail.getSuffix());
            typeSuffix.setIcon(detail.getIcon());

            //查看是否已经存在
            Integer count = diskTypeSuffixDao.findCountAdd(typeSuffix.getSuffix());
            if (count==0){
                diskTypeSuffixDao.save(typeSuffix);
            }
        }
    }
}
