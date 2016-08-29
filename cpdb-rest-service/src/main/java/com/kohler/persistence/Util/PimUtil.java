package com.kohler.persistence.Util;

public class PimUtil {
	
	public static enum Messages {SCHEMAIMPORTINPROGRESS("There is a schema import in progress"),
		REQUESTSUCCESS("Request Success"),
		REQUESTFAILED("PIM Service Request Failed"),
		SUCCESSMESSAGE("Successful insert/update"),
		SUCCESSDELETEMESSAGE("Successful delete"),
		ERROR("Exception while insert/update"),
		ERRORINDELETE("Exception while delete"),
		VALIDATIONFAILED("Validation Failed "),
		ITEMNOTFOUND("Item Not found"),
		WARNING("No Existing Relationship Between Items");
		
		
		String message;
		
		private Messages(String message){
			this.message = message;
		}
		
		public String getMessage() {
			return this.message;
		}
	}
	
	public static final class Attributes {
		
		public static final String statusMessage = "status_message";
		public static final String statusLevel = "status_level";
		public static final String status = "status";
		public static final String itemNo = "itemNo";
		public static final String itemType = "itemType";
		
		public static final String languageCodeEN = "EN";
		public static final String languageCodeSP = "SP";
		public static final String ancestors = "ANCESTORS";
		public static final String statusError = "E";
		public static final String statusSuccess = "S";
		public static final String statusWarning = "W";
		public static final String keepWith = "1";
		public static final String defaultStyleItemId = "77";
		public static final String puniSchema = "PUNI";
		public static final String pcenSchema = "PCEN";
		public static final String itemLinkTypeId = "-1";
		public static final String priority="0";
		public static final String crSellItemId="6";
	}
	
}
