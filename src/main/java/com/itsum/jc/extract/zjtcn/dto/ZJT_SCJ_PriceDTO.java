package com.itsum.jc.extract.zjtcn.dto;

import jmini.db.annotation.AP_FIELD;
import jmini.db.annotation.AP_MODEL;
import jmini.db.modules.Module;
import jmini.dto.AbstractDTO;

/**
 * 
 *  造价通 市场价 DTO
 * 
 * @author Jason Ma
 *
 */
@AP_MODEL(tableName = "ZJT_SCJ_PRICE")
public class ZJT_SCJ_PriceDTO extends Module {
	
	private static final long serialVersionUID = 1L;
	@AP_FIELD(fieldName = "CODE")
	private String code = null;
	@AP_FIELD(fieldName = "STD_NAME")
	private String stdName = null;
	
	//材料名称
	@AP_FIELD(fieldName = "NAME")
	private String name = null;
	
	//材质
	@AP_FIELD(fieldName = "KEY_FEATURES")
	private String keyFeatures = null;
	
	//规格
	@AP_FIELD(fieldName = "SPEC")
	private String spec = null;
	
	//省
	@AP_FIELD(fieldName = "PROVINCE")
	private String province = null;
	
	//城市
	@AP_FIELD(fieldName = "CITY_NAME")
	private String cityName = null;
	
	//公司名称
	@AP_FIELD(fieldName = "COMPANY_NAME")
	private String companyName = null;

	//纳税人
	@AP_FIELD(fieldName = "TAXPAYER")
	private String taxpayer = null;

	//联系方式
	@AP_FIELD(fieldName = "CONTACT")
	private String contact = null;
	
	//品牌
	@AP_FIELD(fieldName = "BRAND")
	private String brand = null;
	
	//计量单位
	@AP_FIELD(fieldName = "UNIT")
	private String unit = null;
	
	//发布时间
	@AP_FIELD(fieldName = "PUBLISH_TIME")
	private String publishTime = null;
	
	//含税面价
	@AP_FIELD(fieldName = "TAX_PRICE")
	private String taxPrice = null;
	
	//税率
	@AP_FIELD(fieldName = "TAX_RATE")
	private String taxRate = null;

	private String link;

	public String getLink() {
		return link;
	}

	public void setLink(String link) {
		this.link = link;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getStdName() {
		return stdName;
	}

	public void setStdName(String stdName) {
		this.stdName = stdName;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getKeyFeatures() {
		return keyFeatures;
	}

	public void setKeyFeatures(String keyFeatures) {
		this.keyFeatures = keyFeatures;
	}

	public String getSpec() {
		return spec;
	}

	public void setSpec(String spec) {
		this.spec = spec;
	}

	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	public String getCityName() {
		return cityName;
	}

	public void setCityName(String cityName) {
		this.cityName = cityName;
	}

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public String getBrand() {
		return brand;
	}

	public void setBrand(String brand) {
		this.brand = brand;
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

	public String getTaxPrice() {
		return taxPrice;
	}

	public void setTaxPrice(String taxPrice) {
		this.taxPrice = taxPrice;
	}

	public String getTaxRate() {
		return taxRate;
	}

	public void setTaxRate(String taxRate) {
		this.taxRate = taxRate;
	}

	public String getTaxpayer() {
		return taxpayer;
	}

	public void setTaxpayer(String taxpayer) {
		this.taxpayer = taxpayer;
	}

	public String getContact() {
		return contact;
	}

	public void setContact(String contact) {
		this.contact = contact;
	}

	@Override
	public String toString() {
		return "ZJT_SCJ_PriceDTO{" +
				"code='" + code + '\'' +
				", stdName='" + stdName + '\'' +
				", name='" + name + '\'' +
				", keyFeatures='" + keyFeatures + '\'' +
				", spec='" + spec + '\'' +
				", province='" + province + '\'' +
				", cityName='" + cityName + '\'' +
				", companyName='" + companyName + '\'' +
				", brand='" + brand + '\'' +
				", unit='" + unit + '\'' +
				", publishTime='" + publishTime + '\'' +
				", taxPrice='" + taxPrice + '\'' +
				", taxRate='" + taxRate + '\'' +
				'}';
	}
}
