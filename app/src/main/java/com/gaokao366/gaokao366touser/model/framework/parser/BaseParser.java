package com.gaokao366.gaokao366touser.model.framework.parser;


import com.gaokao366.gaokao366touser.model.framework.bean.BaseResponse;
import com.gaokao366.gaokao366touser.model.framework.bean.DataHull;

public abstract class BaseParser<T extends BaseResponse> {

	public DataHull<T> getParseResult(String paramString){
		DataHull dataHull = new DataHull();
		dataHull.dataEntry = parse(paramString);
		return dataHull;
	}

	public abstract  T parse(String paramString);
}
