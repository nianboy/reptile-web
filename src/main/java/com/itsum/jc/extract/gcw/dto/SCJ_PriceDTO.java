package com.itsum.jc.extract.gcw.dto;

import jmini.db.annotation.AP_FIELD;
import jmini.db.annotation.AP_MODEL;
import jmini.db.modules.Module;
import jmini.dto.AbstractDTO;

/**
 * 
 *  市场价 DTO
 * 
 * @author Jason Ma
 *
 */
@AP_MODEL(tableName = "T_SCJ_PRICE")
public class SCJ_PriceDTO extends Module {
	
	private static final long serialVersionUID = 1L;
	//材料名称
	@AP_FIELD(fieldName = "NAME")
	private String name = null;
	//材质
	@AP_FIELD(fieldName = "TEXTURE")
	private String texture = null;
	//规格
	@AP_FIELD(fieldName = "SPEC")
	private String spec = null;
	//属性（包括多项）
	@AP_FIELD(fieldName = "PROPERTIES")
	private String properties = null;
	//省
	@AP_FIELD(fieldName = "PROVINCE")
	private String province = null;
	//城市
	@AP_FIELD(fieldName = "CITY_NAME")
	private String cityName = null;
	//公司名称
	@AP_FIELD(fieldName = "COMPANY_NAME")
	private String companyName = null;
	//公司地址
	@AP_FIELD(fieldName = "COMPANY_ADDRESS")
	private String companyAddress = null;
	//联系人
	@AP_FIELD(fieldName = "LINK_MAN")
	private String linkMan = null;
	//联系电话
	@AP_FIELD(fieldName = "LINK_PHONE")
	private String linkPhone = null;
	//纳税人
	@AP_FIELD(fieldName = "TAXPAYER")
	private String taxpayer = null;
	//品牌
	@AP_FIELD(fieldName = "BRAND")
	private String brand = null;
	//计量单位
	@AP_FIELD(fieldName = "UNIT")
	private String unit = null;
	//发布时间
	@AP_FIELD(fieldName = "PUBLISH_TIME")
	private String publishTime = null;
	//含税市场价
	@AP_FIELD(fieldName = "TAX_PRICE_MARKET")
	private String taxPriceMarket = null;
	//含税面价
	@AP_FIELD(fieldName = "TAX_PRICE")
	private String taxPrice = null;
	//税率
	@AP_FIELD(fieldName = "TAX_RATE")
	private String taxRate = null;

	//材料分类1id
	@AP_FIELD(fieldName = "CATEGORY1_ID")
	private String category1Id = null;

	//材料分类2id
	@AP_FIELD(fieldName = "CATEGORY2_ID")
	private String category2Id = null;

	//材料分类1id
	@AP_FIELD(fieldName = "CATEGORY_NAME")
	private String categoryName = null;

	public String getCategory1Id() {
		return category1Id;
	}

	public void setCategory1Id(String category1Id) {
		this.category1Id = category1Id;
	}

	public String getCategory2Id() {
		return category2Id;
	}

	public void setCategory2Id(String category2Id) {
		this.category2Id = category2Id;
	}

	public String getCategoryName() {
		return categoryName;
	}

	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getTexture() {
		return texture;
	}
	public void setTexture(String texture) {
		this.texture = texture;
	}
	public String getSpec() {
		return spec;
	}
	public void setSpec(String spec) {
		this.spec = spec;
	}
	public String getProperties() {
		return properties;
	}
	public void setProperties(String properties) {
		this.properties = properties;
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
	public String getTaxPriceMarket() {
		return taxPriceMarket;
	}
	public void setTaxPriceMarket(String taxPriceMarket) {
		this.taxPriceMarket = taxPriceMarket;
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

	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	public String getCompanyAddress() {
		return companyAddress;
	}

	public void setCompanyAddress(String companyAddress) {
		this.companyAddress = companyAddress;
	}

	public String getLinkMan() {
		return linkMan;
	}

	public void setLinkMan(String linkMan) {
		this.linkMan = linkMan;
	}

	public String getLinkPhone() {
		return linkPhone;
	}

	public void setLinkPhone(String linkPhone) {
		this.linkPhone = linkPhone;
	}

	public String getTaxpayer() {
		return taxpayer;
	}

	public void setTaxpayer(String taxpayer) {
		this.taxpayer = taxpayer;
	}
}
