package com.anapp.fw.json;

/**
 * 当要进行数据库查询的对象不是页面Modele.class的子类时，抛出该异常
 * @author Jason.ma
 */
public class JsonConvertException extends RuntimeException{

	private static final long serialVersionUID = 1L;
	
	public JsonConvertException(String msg,Throwable e){
		super(msg,e);
	}
	
	public JsonConvertException(String msg){
		super(msg);
	}

}
