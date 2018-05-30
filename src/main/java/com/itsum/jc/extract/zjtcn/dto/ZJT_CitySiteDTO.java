package com.itsum.jc.extract.zjtcn.dto;

import jmini.db.annotation.AP_FIELD;
import jmini.db.annotation.AP_MODEL;
import jmini.db.modules.Module;
import jmini.dto.AbstractDTO;

/**
 * 造价通 获取所有站点
 * 
 * @author Jason Ma
 *
 */
@AP_MODEL(tableName = "ZJT_CITY_SITE")
public class ZJT_CitySiteDTO extends Module {
	
	private static final long serialVersionUID = 1L;
	
	//站点名称
	@AP_FIELD(fieldName = "NAME")
	private String name = null;
	
	//站点代码
	@AP_FIELD(fieldName = "CODE")
	private String code = null;
	
	//站点链接
	@AP_FIELD(fieldName = "LINK")
	private String link = null;

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

	public String getLink() {
		return link;
	}

	public void setLink(String link) {
		this.link = link;
	}

	@Override
	public String toString() {
		return "ZJT_CitySiteDTO [name=" + name + ", code=" + code + ", link=" + link + "]";
	}
	
}
