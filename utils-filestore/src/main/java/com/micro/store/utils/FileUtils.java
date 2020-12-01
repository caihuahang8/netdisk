package com.micro.store.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

public class FileUtils {
    /**
     * 根据时间来获取文件夹名字
     * @return
     */
    public static String getFolder(){
        SimpleDateFormat formatYear=new SimpleDateFormat("yyyy");
        SimpleDateFormat formatMonth=new SimpleDateFormat("MM");
        SimpleDateFormat formatDay=new SimpleDateFormat("dd");
        String year=formatYear.format(new Date());
        String month=formatMonth.format(new Date());
        String day=formatDay.format(new Date());

        String folder=year+"/"+month+"/"+day;
        return folder;
    }

    /**
     * 获取文件-后缀
     *
     */
    public static String getFileSuffix(String filename){
        String suff=filename.substring(filename.lastIndexOf(".")+1);//后缀名
        suff=suff.toLowerCase();
        return suff;
    }


}
