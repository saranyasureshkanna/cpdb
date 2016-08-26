package com.kohler.puni.persistence.validations;

import java.util.Arrays;

import org.json.JSONObject;

public class PIMValidations {
	
	
	static final String[] validStatusesForItemG = {"1", "6","91", "96", "101", "201", "301"};
	static final String[] validStatusesForItemI = {"1", "6","91", "96"};
	
	public static boolean isChildJSONValid(JSONObject childJson){
	
		boolean isValid = true;String status =null;
		
		if((childJson.getString("itemType")).equals("G")){
			status = childJson.getString("status");		
			isValid = Arrays.asList(validStatusesForItemG).contains(status) ? true:false;		
		}else if((childJson.getString("itemType")).equals("I")){
			status = childJson.getString("status");
			isValid = Arrays.asList(validStatusesForItemI).contains(status) ? true:false;	
			
		}
		
		return isValid;
	}
}
