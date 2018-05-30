package com.itsum.jc.extract.gcw.dto;

import jmini.db.annotation.AP_FIELD;
import jmini.db.annotation.AP_MODEL;
import jmini.db.modules.Module;
import jmini.dto.AbstractDTO;


/**
 * 人工询价DTO
 * 
 * @author Jason Ma
 *
 */
@AP_MODEL(tableName = "T_RGXJ_PRICE")
public class RGXJ_PriceDTO extends Module {
	
	private static final long serialVersionUID = 1L;
	//材料名称
	@AP_FIELD(fieldName = "NAME")
	private String name = null;
	
	//规格
	@AP_FIELD(fieldName = "SPEC")
	private String spec = null;
	
	//计量单位
	@AP_FIELD(fieldName = "UNIT")
	private String unit = null;
	
	//价格
	@AP_FIELD(fieldName = "PRICE")
	private String price = null;
	
	//时间
	@AP_FIELD(fieldName = "PUBLISH_TIME")
	private String publishTime = null;
	
	//地区
	@AP_FIELD(fieldName = "AREA")
	private String area = null;
	
	//备注
	@AP_FIELD(fieldName = "REMARK")
	private String remark = null;
	
	//供应商
	@AP_FIELD(fieldName = "SUPPLIER_NAME")
	private String supplierName = null;
	
	//供应商联系人
	@AP_FIELD(fieldName = "SUPPLIER_CONTACT_NAME")
	private String supplierContactName = null;
	
	//供应商联系方式
	@AP_FIELD(fieldName = "SUPPLIER_CONTACT")
	private String supplierContact = null;

	private boolean pageOver;

	public boolean isPageOver() {
		return pageOver;
	}

	public void setPageOver(boolean pageOver) {
		this.pageOver = pageOver;
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

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
	}

	public String getPublishTime() {
		return publishTime;
	}

	public void setPublishTime(String publishTime) {
		this.publishTime = publishTime;
	}

	public String getArea() {
		return area;
	}

	public void setArea(String area) {
		this.area = area;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getSupplierName() {
		return supplierName;
	}

	public void setSupplierName(String supplierName) {
		this.supplierName = supplierName;
	}

	public String getSupplierContactName() {
		return supplierContactName;
	}

	public void setSupplierContactName(String supplierContactName) {
		this.supplierContactName = supplierContactName;
	}

	public String getSupplierContact() {
		return supplierContact;
	}

	public void setSupplierContact(String supplierContact) {
		this.supplierContact = supplierContact;
	}

	@Override
	public String toString() {
		return "RGXJ_PriceDTO [name=" + name + ", spec=" + spec + ", unit=" + unit + ", price=" + price
				+ ", publishTime=" + publishTime + ", area=" + area + ", remark=" + remark + ", supplierName="
				+ supplierName + ", supplierContactName=" + supplierContactName + ", supplierContact=" + supplierContact
				+ "]";
	}
	
}
