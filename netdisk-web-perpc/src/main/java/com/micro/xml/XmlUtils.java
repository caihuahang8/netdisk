package com.micro.xml;
import com.thoughtworks.xstream.XStream;
import org.springframework.core.io.ClassPathResource;

import java.io.InputStream;
import java.util.List;

public class XmlUtils {
    public static List<MenuBean> parseMenusxml(){
        try {
            XStream stream = new XStream();
            stream.alias("menu",MenuBean.class);
            stream.alias("menus",List.class);
            ClassPathResource classPathResource = new ClassPathResource("menu.xml");
            InputStream input =classPathResource.getInputStream();
            List<MenuBean> lists=(List<MenuBean>)stream.fromXML(input);
            return lists;
        }catch (Exception e){
            throw new RuntimeException(e.getMessage());
        }
    }
}
