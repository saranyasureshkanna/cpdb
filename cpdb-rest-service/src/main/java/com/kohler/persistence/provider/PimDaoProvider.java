package com.kohler.persistence.provider;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;

import com.kohler.persistence.provider.impl.PimPcenDaoMapperImpl;
import com.kohler.persistence.provider.impl.PimPuniDaoMapperImpl;

public class PimDaoProvider {
	
	@Autowired
	private  PimPcenDaoMapperImpl pcenServiceMapper;
	
	@Autowired
	private  PimPuniDaoMapperImpl puniServiceMapper;
	
	@Autowired
	private SqlSession sqlSessionTemplateSql;
	
	@Autowired
	private SqlSession sqlSessionTemplate;
	
	
	public  PimDaoMapper get(String schemaType){
		PimDaoMapper pimDaoBean = null;
		System.out.println("Schema name"+schemaType);
		if (schemaType.equalsIgnoreCase("PUNI")){
			pimDaoBean = puniServiceMapper;
		}else {
			pimDaoBean = pcenServiceMapper;
		}
		System.out.println("PIMDAOBean"+pimDaoBean.getClass());
		return pimDaoBean;
		//If schema type is PCEN obtain pcenschemamapper else puniSchemamapper
		 
	}
	
	public SqlSession getSqlSession(String schemaType){
		
		if (schemaType.equalsIgnoreCase("PUNI")){
			return sqlSessionTemplate;
		}else {
			return sqlSessionTemplateSql;
		}
		
	}
}
