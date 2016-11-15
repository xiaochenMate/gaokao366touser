package com.gaokao366.gaokao366touser.model.framework.parser;



import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.gaokao366.gaokao366touser.model.framework.bean.BaseResponse;

public class SubBaseParser<T extends BaseResponse> extends BaseParser{

	public Class<T> cls;
	public SubBaseParser(Class<T> cls){
		this.cls = cls ; 
	}

	@Override
	public T parse(String paramString) {
		T result = null;
		try {
			result = JSONObject.parseObject(paramString,cls);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return result;
	}

}
