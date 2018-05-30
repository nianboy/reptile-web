package com.anapp.fw.json;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import jmini.utils.Str;

/**
 * 首页Json数据转Java对象
 * @author Jason Ma
 *
 */
public class JsonDataConvertor{
	
	/**
	 * 读取Java对象列表
	 * @param jsonstr
	 * @param requiredType
	 * @return
	 */
	public static <T> List<T> toJavaList(String jsonstr, Class<T> requiredType){
		if(!Str.isEmpty(jsonstr)){
			ObjectMapper mapper = new ObjectMapper();
			JavaType javaType = mapper.getTypeFactory().constructParametricType(ArrayList.class, requiredType);
			List<T> r = null;
			try {
				r = mapper.readValue(jsonstr, javaType);
			} catch (IOException e) {
				throw new JsonConvertException("Json data convert fail", e);
			}
			return r;			
		}else{
			return null;
		}
	}

	/**
	 * 读取单个Java对象
	 * @param jsonstr
	 * @param requiredType
	 * @return
	 * @throws JsonConvertException
	 * @throws JsonParseException
	 * @throws JsonMappingException
	 * @throws IOException
	 */
	public static <T> T toJavaObject(String jsonstr, Class<T> requiredType) throws JsonConvertException, JsonParseException, JsonMappingException, IOException {
		if(!Str.isEmpty(jsonstr)){
			ObjectMapper mapper = new ObjectMapper();
			return mapper.readValue(jsonstr, requiredType);
		}else{
			return null;
		}
	}
	
	/**
	 * 获取Map中的值List
	 * @param jsonstr
	 * @param requiredType
	 * @return
	 * @throws JsonConvertException
	 * @throws JsonParseException
	 * @throws JsonMappingException
	 * @throws IOException
	 */
	public static <T> List<T> mapValues(String jsonstr, Class<T> requiredType) throws JsonConvertException, JsonParseException, JsonMappingException, IOException {
		if(!Str.isEmpty(jsonstr)){
			ObjectMapper mapper = new ObjectMapper();
			//@SuppressWarnings("unchecked")
			//List类型 JavaType javaType = mapper.getTypeFactory().constructParametricType(List.class, Bean.class);
			JavaType javaType = mapper.getTypeFactory().constructParametricType(HashMap.class, String.class, requiredType); 
			Map<String, T> map = mapper.readValue(jsonstr, javaType);
			if(map.isEmpty()){
				return null;
			}
			List<T> valueList = new ArrayList<T>(map.values());
			return valueList;
		}else{
			return null;
		}
	}
	
	/**
	 * Java对象转json字符串方法
	 * @param javaObject
	 * @return
	 * @throws JsonProcessingException
	 */
	public static String toJson(Object javaObject) throws JsonProcessingException{
		if(javaObject == null){
			return null;
		}
		ObjectMapper mapper = new ObjectMapper();
		String json = mapper.writeValueAsString(javaObject);
		return json;
	}

}
