package com.kohler.persistence.domain.json;

public enum ActionType {

	ADD {
		public String getAction() {
			return "Add";
		}
	},
	MODIFY { 
		public String getAction() {
			return "MODIFY";
		}
	},
	DELETE {
		public String getAction() {
			return "DELETE";
		}
	};
	public abstract String getAction();

	public static ActionType getType(String action) {
		ActionType actionType = ADD;
		if ("MODIFY".equalsIgnoreCase(action)) {
			actionType = MODIFY;
		}
		if ("DELETE".equalsIgnoreCase(action)) {
			actionType = DELETE;
		}
		if ("ADD".equalsIgnoreCase(action)) {
			actionType = ADD;
		}
		return actionType;
	}

}
