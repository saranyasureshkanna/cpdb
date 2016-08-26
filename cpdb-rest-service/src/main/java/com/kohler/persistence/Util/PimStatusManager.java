package com.kohler.persistence.Util;

import java.util.Hashtable;

public class PimStatusManager {
	private static final Hashtable<String,Integer> statusTable = new Hashtable<String, Integer>();
	//
	public static final Integer statusStopped = 0;
	public static final Integer statusInProgress = 1;
		
	static {
		statusTable.put(PimUtil.Attributes.puniSchema, statusStopped);
		statusTable.put(PimUtil.Attributes.pcenSchema, statusStopped);
	}
	
	public synchronized  Hashtable<String, Integer> getStatusTable(){
		return statusTable;
	}
	
	public synchronized  void put(String schema, Integer status){
		statusTable.put(schema, status);
	}
	
}
