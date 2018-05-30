package com.itsum.jc.service;

import com.itsum.jc.ENV;
import com.itsum.jc.moudles.PageConstant;
import com.itsum.jc.moudles.TPageRecordDTO;
import jmini.utils.Str;
import org.jsoup.helper.StringUtil;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class BaseService {

    public static String formatStr(String str){
        return StringUtil.isBlank(str) ? "" : str;
    }

    //获取最年3年
    public static String[] getDateArr(){
        String date[] = new String[3];
        LocalDate localDate = LocalDate.now();
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy");
        String newtime = localDate.format(dateTimeFormatter);
        date[0] = newtime;
        date[1] = String.valueOf(Integer.parseInt(newtime) - 1);
        date[2] = String.valueOf(Integer.parseInt(newtime) - 2);
        return date;
    }

    public static String[] getIndustry(){
        String[] industryArr = new String[5];
        industryArr[0] = "";
        industryArr[1] = "jtgc";
        industryArr[2] = "slgc";
        industryArr[3] = "tlgd";
        industryArr[4] = "rfgc";

        return industryArr;
    }

    public static String[] getIndustryComplate(){
        String[] industryArr = new String[5];
        industryArr[0] = "/gov/c_cs_d_t_p1.html";
        industryArr[1] = "/gov/jtgc_c_cs_d_t_p1.html";
        industryArr[2] = "/gov/slgc_c_cs_d_t_p1.html";
        industryArr[3] = "/gov/tlgd_c_cs_d_t_p1.html";
        industryArr[4] = "/gov/rfgc_c_cs_d_t_p1.html";
        return industryArr;
    }

    public static String[] getIndustryName(){
        String[] industryArr = new String[5];
        industryArr[0] = "建筑工程";
        industryArr[1] = "交通工程";
        industryArr[2] = "水利工程";
        industryArr[3] = "铁路轨道";
        industryArr[4] = "人防工程";
        return industryArr;
    }

    //验证爬过的页数
    public static int checkPageRecord(int num,String name,String type){
        int pageAction = 1;
        TPageRecordDTO tPageRecordDTO = ENV.db().queryOne("NAME=? AND TYPE=?",null,TPageRecordDTO.class,name,type);
        if(tPageRecordDTO != null){
            if(!Str.isEmpty(tPageRecordDTO.getPage())){
                int pageNo = Integer.parseInt(tPageRecordDTO.getPage());
                if(num <= pageNo){
                    pageAction = 0;
                }else{
                    pageAction = pageNo+1;
                }
            }
        }
        return pageAction;
    }

    //查询爬过的页数
    public static int getPageRecord(String name,String type){
        int pageNo = 0;
        TPageRecordDTO tPageRecordDTO = ENV.db().queryOne("NAME=? AND TYPE=?",null,TPageRecordDTO.class,name,type);
        if(tPageRecordDTO != null){
            pageNo = Integer.parseInt(tPageRecordDTO.getPage());
        }
        return pageNo;
    }

    //记录爬过的页数
    public static void savePageRecord(int count,String name){
        TPageRecordDTO tPageRecordDTO = ENV.db().queryOne("NAME=?",null,TPageRecordDTO.class,name);
        if(tPageRecordDTO == null){
            tPageRecordDTO = new TPageRecordDTO();
            tPageRecordDTO.setName(name);
            tPageRecordDTO.setPage("0");
            tPageRecordDTO.setType(PageConstant.GCW);
            ENV.db().save(tPageRecordDTO);
        }else{
            tPageRecordDTO.setPage(String.valueOf(count));
            ENV.db().update(tPageRecordDTO,"PAGE");
        }
    }

    //查询区域记录
    public static boolean getAreaCodeRecord(String areaCode,String type,String name){
        TPageRecordDTO tPageRecordDTO = ENV.db().queryOne("TYPE=? AND NAME=? AND AREA_CODE=?","",TPageRecordDTO.class,type,name,areaCode);
        if(tPageRecordDTO == null){
            return true;
        }else {
            return false;
        }
    }

    //保存区域记录
    public static void saveAreaCodeRecord(String areaCode,String type,String name){
        TPageRecordDTO tPageRecordDTO = new TPageRecordDTO();
        tPageRecordDTO.setName(name);
        tPageRecordDTO.setType(type);
        tPageRecordDTO.setAreaCode(areaCode);
        ENV.db().save(tPageRecordDTO);
    }

    //查询城市记录
    public static boolean getCityCodeRecord(String cityCode,String type,String name){
        TPageRecordDTO tPageRecordDTO = ENV.db().queryOne("TYPE=? AND NAME=? AND CITY_CODE=?","",TPageRecordDTO.class,type,name,cityCode);
        if(tPageRecordDTO == null){
            return true;
        }else {
            return false;
        }
    }

    //保存城市记录
    public static void saveCityCodeRecord(String cityCode,String type,String name){
        TPageRecordDTO tPageRecordDTO = new TPageRecordDTO();
        tPageRecordDTO.setName(name);
        tPageRecordDTO.setType(type);
        tPageRecordDTO.setCityCode(cityCode);
        ENV.db().save(tPageRecordDTO);
    }

    //查询链接记录
    public static boolean getLinkRecord(String flagId,String type,String name){
        TPageRecordDTO tPageRecordDTO = ENV.db().queryOne("TYPE=? AND NAME=? AND LINK=?","",TPageRecordDTO.class,type,name,flagId);
        if(tPageRecordDTO == null){
            return true;
        }else {
            return false;
        }
    }

    //保存链接记录
    public static void saveLinkRecord(String flagId,String type,String name){
        TPageRecordDTO tPageRecordDTO = new TPageRecordDTO();
        tPageRecordDTO.setName(name);
        tPageRecordDTO.setType(type);
        tPageRecordDTO.setLink(flagId);
        ENV.db().save(tPageRecordDTO);
    }


}
