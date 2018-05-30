package com.itsum.jc.extract.zjtcn.dto;

import jmini.db.annotation.AP_FIELD;
import jmini.db.annotation.AP_MODEL;
import jmini.db.modules.Module;
import jmini.dto.AbstractDTO;

/**
 * 造价通 获取所有站点下的区域
 * 
 * @author Jason Ma
 *
 */
@AP_MODEL(tableName = "ZJT_CITY_SITE_AREA")
public class ZJT_CitySite_AreaDTO extends Module {
	
	private static final long serialVersionUID = 1L;
	
	//城市名称
	@AP_FIELD(fieldName = "NAME")
	private String name = null;
	
	//城市代码
	@AP_FIELD(fieldName = "CODE")
	private String code = null;
	

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	@Override
	public String toString() {
		return "ZJT_CitySite_AreaDTO [name=" + name + ", code=" + code + "]";
	}
	
}
