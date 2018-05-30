package com.anapp.fw.cache;

import java.util.List;

import jmini.dto.IDataDTO;

/**
 * 缓存对象接口
 * @author Jason Ma
 *
 * @param <T>
 */
public interface IMyCache<T extends IDataDTO> {
	
	/**
	 * 以列表方式缓存对象调用该方法返回
	 * @return
	 */
	public List<T> getList();
	/**
	 * 以对象方式缓存对象调用该方法返回
	 * @return
	 */
	public T getData();
	/**
	 * 通知缓存重新装载数据
	 */
	public void reload();

}
