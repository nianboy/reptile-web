package com.itsum.jc.extract.gcw.dto;

import jmini.db.annotation.AP_FIELD;
import jmini.db.annotation.AP_MODEL;
import jmini.db.modules.Module;
import jmini.dto.AbstractDTO;

/**
 * 
 * 信息价 DTO
 * 
 * @author Jason Ma
 *
 */
@AP_MODEL(tableName = "T_XXJ_PRICE")
public class XXJ_PriceDTO extends Module {
	
	private static final long serialVersionUID = 1L;
	//材料名称
	@AP_FIELD(fieldName = "NAME")
	private String name = null;
	
	//规格
	@AP_FIELD(fieldName = "SPEC")
	private String spec = null;

	//材料类别
	@AP_FIELD(fieldName = "CATEGORY_NAME")
	private String categoryName = null;
	
	//计量单位
	@AP_FIELD(fieldName = "UNIT")
	private String unit = null;
	
	//含税市场价
	@AP_FIELD(fieldName = "TAX_PRICE_MARKET")
	private String taxPriceMarket = null;
	
	//含税面价
	@AP_FIELD(fieldName = "TAX_PRICE")
	private String taxPrice = null;
	
	//税率
	@AP_FIELD(fieldName = "TAX_RATE")
	private String taxRate = null;
	
	//备注
	@AP_FIELD(fieldName = "RAMARK")
	private String remark = null;
	
	//专业
	@AP_FIELD(fieldName = "MAJOR")
	private String major = null;
	
	//城市期间
	@AP_FIELD(fieldName = "CITY_PERIOD")
	private String cityPeriod = null;
	
	//城市名称
	@AP_FIELD(fieldName = "CITY_NAME")
	private String cityName = null;

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

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
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

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getMajor() {
		return major;
	}

	public void setMajor(String major) {
		this.major = major;
	}

	public String getCityPeriod() {
		return cityPeriod;
	}

	public void setCityPeriod(String cityPeriod) {
		this.cityPeriod = cityPeriod;
	}

	public String getCityName() {
		return cityName;
	}

	public void setCityName(String cityName) {
		this.cityName = cityName;
	}

	public String getCategoryName() {
		return categoryName;
	}

	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}

	@Override
	public String toString() {
		return "XXJ_PriceDTO [name=" + name + ", spec=" + spec + ", unit=" + unit + ", taxPriceMarket=" + taxPriceMarket
				+ ", taxPrice=" + taxPrice + ", taxRate=" + taxRate + ", remark=" + remark + ", major=" + major
				+ ", cityPeriod=" + cityPeriod + ", cityName=" + cityName + "]";
	}
	
}
