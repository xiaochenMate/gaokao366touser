package com.gaokao366.gaokao366touser.model.framework.network;


import java.util.Map;

public class Request {
	
	private ServerInterfaceDefinition serverInterfaceDefinition;
	private Map<String, String> paramsMap;

	public Request(ServerInterfaceDefinition serverInterfaceDefinition,
			Map<String, String> paramsMap ) {
		super();
		this.serverInterfaceDefinition = serverInterfaceDefinition;
		this.paramsMap = paramsMap;
	}
	
	public Map<String, String> getParamsMap() {
		return paramsMap;
	}
	public void setParamsMap(Map<String, String> paramsMap) {
		this.paramsMap = paramsMap;
	}
	
	
	public ServerInterfaceDefinition getServerInterfaceDefinition() {
		return serverInterfaceDefinition;
	}
	public void setServerInterfaceDefinition(
			ServerInterfaceDefinition serverInterfaceDefinition) {
		this.serverInterfaceDefinition = serverInterfaceDefinition;
	}

	/*public BaseParser<?> getJsonParser() {
		return jsonParser;
	}
	public void setJsonParser(BaseParser<?> jsonParser) {
		this.jsonParser = jsonParser;
	}*/
	
}
