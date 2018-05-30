package com.itsum.jc.moudles;

import jmini.db.annotation.AP_FIELD;
import jmini.db.annotation.AP_MODEL;
import jmini.db.modules.Module;

@AP_MODEL(tableName = "T_PAGE_RECORD")
public class TPageRecordDTO extends Module {

    private static final long serialVersionUID = 1L;

    //名称
    @AP_FIELD(fieldName = "NAME")
    private String name = null;

    //页数
    @AP_FIELD(fieldName = "PAGE")
    private String page = null;

    //网站类型
    @AP_FIELD(fieldName = "TYPE")
    private String type = null;

    //区域代码
    @AP_FIELD(fieldName = "AREA_CODE")
    private String areaCode = null;

    //城市代码
    @AP_FIELD(fieldName = "CITY_CODE")
    private String cityCode = null;

    //链接名
    @AP_FIELD(fieldName = "LINK")
    private String link = null;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPage() {
        return page;
    }

    public void setPage(String page) {
        this.page = page;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getAreaCode() {
        return areaCode;
    }

    public void setAreaCode(String areaCode) {
        this.areaCode = areaCode;
    }

    public String getCityCode() {
        return cityCode;
    }

    public void setCityCode(String cityCode) {
        this.cityCode = cityCode;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }
}
