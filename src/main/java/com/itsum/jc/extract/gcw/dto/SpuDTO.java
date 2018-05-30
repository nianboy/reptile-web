package com.itsum.jc.extract.gcw.dto;

import jmini.db.annotation.AP_FIELD;
import jmini.db.annotation.AP_MODEL;
import jmini.db.modules.Module;
import jmini.dto.AbstractDTO;

/**
 * 
 * 市场价 Spu链接 DTO
 * 
 * @author Jason Ma
 *
 */
@AP_MODEL(tableName = "T_SPU")
public class SpuDTO extends Module {
	
	private static final long serialVersionUID = 1L;
	@AP_FIELD(fieldName = "NAME")
	private String name = null;
	@AP_FIELD(fieldName = "LINK")
	private String link = null;

	private Integer pageNo;

	public Integer getPageNo() {
		return pageNo;
	}

	public void setPageNo(Integer pageNo) {
		this.pageNo = pageNo;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getLink() {
		return link;
	}

	public void setLink(String link) {
		this.link = link;
	}
	
}
