package com.itsum.jc.moudles;

import jmini.db.annotation.AP_FIELD;
import jmini.db.annotation.AP_MODEL;
import jmini.db.modules.Module;

/**
 * 广材网,造价通sessionId
 * @author yh
 *
 */
@AP_MODEL(tableName = "T_SESSION")
public class TSession extends Module {
	
	private static final long serialVersionUID = 1L;

	/**SESSIONID*/
	@AP_FIELD(fieldName = "SESSIONID")
	private String sessionId;
	
	/**type*/
	@AP_FIELD(fieldName = "TYPE")
	private String type;

	private String typeName;
	
	public String getSessionId() {
		return sessionId;
	}

	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getTypeName() {
		return typeName;
	}

	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}


	
}
