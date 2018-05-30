package com.itsum.jc.extract.gcw.dto;

import jmini.db.annotation.AP_FIELD;
import jmini.db.annotation.AP_MODEL;
import jmini.db.modules.Module;
import jmini.dto.AbstractDTO;

/**
 * 
 * 刚才行情 获取所有地区 DTO
 * 
 * @author Jason Ma
 *
 */
@AP_MODEL(tableName = "T_GCHQ_AREA")
public class Gchq_AreaCodeDTO extends Module {
	
	private static final long serialVersionUID = 1L;
	//名称
	@AP_FIELD(fieldName = "NAME")
	private String name = null;
	//代码
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
		return "Gchq_AreaCodeDTO [name=" + name + ", code=" + code + "]";
	}
	
}
