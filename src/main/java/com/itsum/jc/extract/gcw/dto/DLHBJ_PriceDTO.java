package com.itsum.jc.extract.gcw.dto;

import jmini.db.annotation.AP_FIELD;
import jmini.db.annotation.AP_MODEL;
import jmini.db.modules.Module;

/**
 * 电缆红本价DTO
 * 
 * @author Jason Ma
 *
 */
@AP_MODEL(tableName = "T_DLHBJ_PRICE")
public class DLHBJ_PriceDTO extends Module {
	
	private static final long serialVersionUID = 1L;
	
	//规格型号
	@AP_FIELD(fieldName = "SPEC")
	private String spec = null;
	
	//计量单位
	@AP_FIELD(fieldName = "UNIT")
	private String unit = null;
	
	//电压等级
	@AP_FIELD(fieldName = "VOLTAGE")
	private String voltage = null;
	
	//含税价
	@AP_FIELD(fieldName = "TAX_PRICE")
	private String taxPrice = null;
	
	//除税价
	@AP_FIELD(fieldName = "PRICE")
	private String price = null;
	
	//含税成本价
	@AP_FIELD(fieldName = "TAX_COST_PRICE")
	private String taxCostPrice = null;
	
	//增值税税率
	@AP_FIELD(fieldName = "TAX_RATE")
	private String taxRate = null;
	
	//时间
	@AP_FIELD(fieldName = "PUBLISH_TIME")
	private String publishTime = null;
	
	//产品ID
	@AP_FIELD(fieldName = "CABLE_PRODUCT_ID")
	private String cableProductId = null;
	
	//产品价格ID
	@AP_FIELD(fieldName = "CABLE_PRODUCT_PRICE_ID")
	private String cableProductPriceId = null;

	private Integer pageNo;

	public Integer getPageNo() {
		return pageNo;
	}

	public void setPageNo(Integer pageNo) {
		this.pageNo = pageNo;
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

	public String getVoltage() {
		return voltage;
	}

	public void setVoltage(String voltage) {
		this.voltage = voltage;
	}

	public String getTaxPrice() {
		return taxPrice;
	}

	public void setTaxPrice(String taxPrice) {
		this.taxPrice = taxPrice;
	}

	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
	}

	public String getTaxCostPrice() {
		return taxCostPrice;
	}

	public void setTaxCostPrice(String taxCostPrice) {
		this.taxCostPrice = taxCostPrice;
	}

	public String getTaxRate() {
		return taxRate;
	}

	public void setTaxRate(String taxRate) {
		this.taxRate = taxRate;
	}

	public String getPublishTime() {
		return publishTime;
	}

	public void setPublishTime(String publishTime) {
		this.publishTime = publishTime;
	}

	public String getCableProductId() {
		return cableProductId;
	}

	public void setCableProductId(String cableProductId) {
		this.cableProductId = cableProductId;
	}

	public String getCableProductPriceId() {
		return cableProductPriceId;
	}

	public void setCableProductPriceId(String cableProductPriceId) {
		this.cableProductPriceId = cableProductPriceId;
	}

	@Override
	public String toString() {
		return "DLHBJ_PriceDTO [spec=" + spec + ", unit=" + unit + ", voltage=" + voltage + ", taxPrice=" + taxPrice
				+ ", price=" + price + ", taxCostPrice=" + taxCostPrice + ", taxRate=" + taxRate + ", publishTime="
				+ publishTime + ", cableProductId=" + cableProductId + ", cableProductPriceId=" + cableProductPriceId
				+ "]";
	}
	
}
