package com.itsum.jc.extract.gcw.dto;

import jmini.db.annotation.AP_FIELD;
import jmini.db.annotation.AP_MODEL;
import jmini.db.modules.Module;

/**
 * 
 * 刚才行情 发布日期价格
 * 
 * @author Jason Ma
 *
 */
@AP_MODEL(tableName = "T_GCHQ_PRICE")
public class Gchq_PriceDTO extends Module {
	
	private static final long serialVersionUID = 1L;
	
	//地区代码
	@AP_FIELD(fieldName = "AREA_CODE")
	private String areaCode = null;
	
	//地区名称
	@AP_FIELD(fieldName = "AREA_NAME")
	private String areaName = null;
	
	//日期
	@AP_FIELD(fieldName = "DATE")
	private String date = null;
	
	//名称
	@AP_FIELD(fieldName = "NAME")
	private String name = null;
	
	//规格
	@AP_FIELD(fieldName = "SPEC")
	private String spec = null;
	
	//材质
	@AP_FIELD(fieldName = "TEXTURE")
	private String texture = null;
	
	//产地/厂家
	@AP_FIELD(fieldName = "MAUFACTURER")
	private String manufacturer = null;
	
	//价格
	@AP_FIELD(fieldName = "PRICE")
	private String price = null;
	
	//备注
	@AP_FIELD(fieldName = "REMARK")
	private String remark = null;

	public String getAreaCode() {
		return areaCode;
	}

	public void setAreaCode(String areaCode) {
		this.areaCode = areaCode;
	}

	public String getAreaName() {
		return areaName;
	}

	public void setAreaName(String areaName) {
		this.areaName = areaName;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSpec() {
		return spec;
	}

	public void setSpec(String spec) {
		this.spec = spec;
	}

	public String getTexture() {
		return texture;
	}

	public void setTexture(String texture) {
		this.texture = texture;
	}

	public String getManufacturer() {
		return manufacturer;
	}

	public void setManufacturer(String manufacturer) {
		this.manufacturer = manufacturer;
	}

	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	@Override
	public String toString() {
		return "Gchq_PriceDTO [areaCode=" + areaCode + ", areaName=" + areaName + ", date=" + date + ", name=" + name
				+ ", spec=" + spec + ", texture=" + texture + ", manufacturer=" + manufacturer + ", price=" + price
				+ ", remark=" + remark + "]";
	}
	
}
