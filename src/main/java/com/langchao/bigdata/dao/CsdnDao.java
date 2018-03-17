package com.langchao.bigdata.dao;

import java.util.Map;

import org.apache.ibatis.annotations.Insert;

import com.langchao.bigdata.model.Ciopi;
import com.langchao.bigdata.model.CsdnInfo;

/**
 * 使用mybatis 注解方式
 * @author yuenbin
 */
public interface CsdnDao  {

//	  @Insert("	INSERT INTO csdnblog(id,title,date,tags,category,view,comments,copyright) values(#{id},#{title},#{date},#{tags},#{category},#{view},#{comments},#{copyright})")
//	  int addCsdn(CsdnInfo csdnInfo);
//	  
//	  @Insert("	INSERT INTO ciopi(title,content) values(#{title},#{content})")
//	  int addCiopi(Ciopi ciopi);
	  
	  int addCsdn(Map map);
	  
	  int addCiopi(Map map);
	  
	  int addCountryInfo(Map map);
	  
	    void  addBsEntBasic(Map map1);
	    void  addBsEntTrAn(Map map);
	    
	    String selectBaseDict(Map paramsMap );
	    
}
