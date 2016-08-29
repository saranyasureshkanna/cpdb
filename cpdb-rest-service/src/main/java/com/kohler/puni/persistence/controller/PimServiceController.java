package com.kohler.puni.persistence.controller;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.log4j.Logger;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.kohler.persistence.Util.PimStatusManager;
import com.kohler.persistence.Util.PimUtil;
import com.kohler.persistence.domain.json.Schema;
import com.kohler.puni.persistence.service.impl.PimServiceCrSellingImpl;
import com.kohler.puni.persistence.service.impl.PimServiceImpl;

@Component
@Path("/pim")
public class PimServiceController {

	private static final Logger log = Logger.getLogger(PimServiceController.class);
	private static  String schemaObj = null;

	@Autowired
	private PimServiceImpl pimService;

	@Autowired
	private PimServiceCrSellingImpl pimCSService;
	
	@Autowired
	private PimStatusManager pimStatusManager;

	@GET
	@Path("/getItems")
	public Response getItems() {

		String result = "Hello World";
		return Response.status(200).entity(result).build();

	}
	
	

	@POST
	@Path("/postItemGroups/{schema}")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response postItemsHierarchies(String json,
			@PathParam("schema") String schema) {
		log.debug("postItemGroups request" + schema);
		JSONObject jsonObj = new JSONObject(json.trim());
		String responseJSON = null;
		schemaObj = (schema.equals(PimUtil.Attributes.pcenSchema))
				? PimUtil.Attributes.pcenSchema : PimUtil.Attributes.puniSchema;
		if(pimStatusManager.getStatusTable().get(schemaObj) == PimStatusManager.statusStopped) {
			System.out.println(schemaObj+"schema will start importing");
			pimStatusManager.put(schemaObj, PimStatusManager.statusInProgress);
		}else {
			log.error(new JSONObject().put("error", PimUtil.Messages.SCHEMAIMPORTINPROGRESS.getMessage()).toString());
			return Response.status(500).entity(new JSONObject().put("error", PimUtil.Messages.SCHEMAIMPORTINPROGRESS.getMessage()).toString()).build();
		}
		JSONObject requestJsonObj = jsonObj.getJSONObject("request");
		log.info("Request Json" + JSONObject.quote(json));
		
		
		responseJSON = pimService.processItemHierarchiesJSON(requestJsonObj,
				schema);
		if (responseJSON != null && responseJSON.length() > 0){
			System.out.println ("setting status to 1 postitemhier");
			pimStatusManager.put(schemaObj, PimStatusManager.statusStopped);
			return Response.status(200).entity(responseJSON).build();
			
		} else {
			return Response.status(500).entity(PimUtil.Messages.REQUESTFAILED.getMessage()).build();
		}
		

	}

	@POST
	@Path("/postItems/{schema}")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response postItems(String json, @PathParam("schema") String schema, @PathParam("clientId")String clientId) {
		log.debug("postItems request" + schema);
		JSONObject jsonObj = new JSONObject(json.trim());
		String responseJSON = null;
		
		JSONObject requestJsonObj = jsonObj.getJSONObject("request");
		
		schemaObj = (schema.equals(PimUtil.Attributes.pcenSchema))
				? PimUtil.Attributes.pcenSchema : PimUtil.Attributes.puniSchema;
		if(pimStatusManager.getStatusTable().get(schemaObj) == PimStatusManager.statusStopped) {
			// Show schema import is in progress
			System.out.println(schemaObj+"schema will start importing");
			pimStatusManager.put(schemaObj, PimStatusManager.statusInProgress);
		}else {
			log.error(new JSONObject().put("error", PimUtil.Messages.SCHEMAIMPORTINPROGRESS.getMessage()).toString());
			return Response.status(500).entity(new JSONObject().put("error", PimUtil.Messages.SCHEMAIMPORTINPROGRESS.getMessage()).toString()).build();
		}
		responseJSON = pimService.processItemsJSON(requestJsonObj, schema);
		if (responseJSON != null && responseJSON.length() > 0){
			System.out.println ("setting status to 1 postitems");
			pimStatusManager.put(schemaObj, PimStatusManager.statusStopped);
			return Response.status(200).entity(responseJSON).build();
			
		} else {
			return Response.status(500).entity(new JSONObject().put("error", PimUtil.Messages.REQUESTFAILED.getMessage()).toString()).build();
		}

	}
	
	@POST
	@Path("/postCSItems/{schema}")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response postCSItems(String json, @PathParam("schema") String schema, @PathParam("clientId")String clientId) {
		log.debug("postCSItems request" + schema);
		JSONObject jsonObj = new JSONObject(json.trim());
		
		
		String responseJSON = null;
		  
		JSONObject requestJsonObj = jsonObj.getJSONObject("request");
		
		schemaObj = (schema.equals(PimUtil.Attributes.pcenSchema))
				? PimUtil.Attributes.pcenSchema : PimUtil.Attributes.puniSchema;
		if(pimStatusManager.getStatusTable().get(schemaObj) == PimStatusManager.statusStopped) {
			// Show schema import is in progress
			System.out.println(schemaObj+"schema will start importing");
			pimStatusManager.put(schemaObj, PimStatusManager.statusInProgress);
		}else {
			log.error(new JSONObject().put("error", PimUtil.Messages.SCHEMAIMPORTINPROGRESS.getMessage()).toString());
			return Response.status(500).entity(new JSONObject().put("error", PimUtil.Messages.SCHEMAIMPORTINPROGRESS.getMessage()).toString()).build();
		}
		responseJSON = pimCSService.processCSItemsJSON(requestJsonObj, schema);
		if (responseJSON != null && responseJSON.length() > 0){
			System.out.println ("setting status to 1 postitems");
			pimStatusManager.put(schemaObj, PimStatusManager.statusStopped);
			return Response.status(200).entity(responseJSON).build();
			
		} else {
			return Response.status(500).entity(new JSONObject().put("error", PimUtil.Messages.REQUESTFAILED.getMessage()).toString()).build();
		}

	}



}
