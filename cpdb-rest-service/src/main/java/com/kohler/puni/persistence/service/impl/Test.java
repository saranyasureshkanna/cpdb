package com.kohler.puni.persistence.service.impl;

import org.json.JSONObject;

public class Test {

	public static void main(String args[])
	{
		new PimServiceCrSellingImpl().processCSItemsJSON(new JSONObject(), "PUNI");
		
	}
}
 