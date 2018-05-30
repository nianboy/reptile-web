package com.itsum.jc.extract.zjtcn.dto;

import jmini.db.annotation.AP_FIELD;
import jmini.db.annotation.AP_MODEL;
import jmini.db.modules.Module;
import jmini.dto.AbstractDTO;

/**
 * 
 *  造价通 信息价 DTO
 * 
 * @author Jason Ma
 *
 */
@AP_MODEL(tableName = "ZJT_XXJ_PRICE")
public class ZJT_XXJ_PriceDTO extends Module {
	
	private static final long serialVersionUID = 1L;
	@AP_FIELD(fieldName = "CODE")
	private String code = null;
	
	//材料名称
	@AP_FIELD(fieldName = "NAME")
	private String name = null;
	@AP_FIELD(fieldName = "STD_NAME")
	private String stdName = null;
	
	//规格
	@AP_FIELD(fieldName = "SPEC")
	private String spec = null;
	
	//计量单位
	@AP_FIELD(fieldName = "UNIT")
	private String unit = null;
	
	//省
	@AP_FIELD(fieldName = "PROVINCE")
	private String province = null;
	
	//城市
	@AP_FIELD(fieldName = "CITY")
	private String city = null;
	
	//国家
	@AP_FIELD(fieldName = "COUNTY")
	private String county = null;
	
	//发布时间
	@AP_FIELD(fieldName = "PUBLISH_TIME")
	private String publishTime = null;

	@AP_FIELD(fieldName = "UPDATE_ON")
	private String updateOn = null;
	
	//价格
	@AP_FIELD(fieldName = "PRICE_NTAX")
	private String priceNtax = null;
	
	//备注
	@AP_FIELD(fieldName = "NOTES")
	private String notes = null;


	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getStdName() {
		return stdName;
	}

	public void setStdName(String stdName) {
		this.stdName = stdName;
	}

	public String getSpec() {
		return spec;
	}

	public void setSpec(String spec) {
		this.spec = spec;
	}

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getCounty() {
		return county;
	}

	public void setCounty(String county) {
		this.county = county;
	}

	public String getPublishTime() {
		return publishTime;
	}

	public void setPublishTime(String publishTime) {
		this.publishTime = publishTime;
	}

	public String getUpdateOn() {
		return updateOn;
	}

	public void setUpdateOn(String updateOn) {
		this.updateOn = updateOn;
	}

	public String getPriceNtax() {
		return priceNtax;
	}

	public void setPriceNtax(String priceNtax) {
		this.priceNtax = priceNtax;
	}
	
	public String getNotes() {
		return notes;
	}

	public void setNotes(String notes) {
		this.notes = notes;
	}

	@Override
	public String toString() {
		return "ZJT_XXJ_PriceDTO{" +
				"code='" + code + '\'' +
				", name='" + name + '\'' +
				", stdName='" + stdName + '\'' +
				", spec='" + spec + '\'' +
				", unit='" + unit + '\'' +
				", province='" + province + '\'' +
				", city='" + city + '\'' +
				", county='" + county + '\'' +
				", publishTime='" + publishTime + '\'' +
				", updateOn='" + updateOn + '\'' +
				", priceNtax='" + priceNtax + '\'' +
				", notes='" + notes + '\'' +
				'}';
	}
}
