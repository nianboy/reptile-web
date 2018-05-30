package com.itsum.jc.extract.zjtcn.dto;

import jmini.db.annotation.AP_FIELD;
import jmini.db.annotation.AP_MODEL;
import jmini.db.modules.Module;

/**
 * 造价通 参考价 DTO
 * 
 * @author Jason Ma
 *
 */
@AP_MODEL(tableName = "ZJT_CKJ_PRICE")
public class ZJT_CKJ_PriceDTO extends Module {
	
	private static final long serialVersionUID = 1L;


	//材料名称
	@AP_FIELD(fieldName = "NAME")
	private String name = null;
	
	//规格
	@AP_FIELD(fieldName = "SPEC")
	private String spec = null;
	@AP_FIELD(fieldName = "SUBCID_NAMES")
	private String subcidNames = null;
	
	//省
	@AP_FIELD(fieldName = "PROVINCE")
	private String province = null;
	
	//来源
	@AP_FIELD(fieldName = "SOURCE")
	private String source = null;
	
	//计量单位
	@AP_FIELD(fieldName = "UNIT")
	private String unit = null;
	
	//发布时间
	@AP_FIELD(fieldName = "PUBLISH_TIME")
	private String publishTime = null;
	
	//含税面价
	@AP_FIELD(fieldName = "PRICE")
	private String price = null;


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

	public String getSubcidNames() {
		return subcidNames;
	}

	public void setSubcidNames(String subcidNames) {
		this.subcidNames = subcidNames;
	}

	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	public String getPublishTime() {
		return publishTime;
	}

	public void setPublishTime(String publishTime) {
		this.publishTime = publishTime;
	}

	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
	}

	@Override
	public String toString() {
		return "ZJT_CKJ_PriceDTO{" +
				"name='" + name + '\'' +
				", spec='" + spec + '\'' +
				", subcidNames='" + subcidNames + '\'' +
				", province='" + province + '\'' +
				", source='" + source + '\'' +
				", unit='" + unit + '\'' +
				", publishTime='" + publishTime + '\'' +
				", price='" + price + '\'' +
				'}';
	}
}
