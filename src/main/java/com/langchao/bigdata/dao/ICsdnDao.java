package com.langchao.bigdata.dao;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.mybatis.spring.SqlSessionTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;

import com.langchao.bigdata.model.CsdnInfo;

/**
 * 使用mybatis 注解方式
 * 开启事务
 * @author yuenbin
 *
 */
public class ICsdnDao implements CsdnDao {
	
	Logger log = LoggerFactory.getLogger(getClass());
	
	@Resource
	private SqlSessionTemplate sqlSessionTemplate ;
	
	public int addCsdn(CsdnInfo csdnInfo) {
		return sqlSessionTemplate.insert(getClass().getName()+".addCsdn");
	}

	public int addCsdn(Map map) {
		return sqlSessionTemplate.insert(getClass().getName()+".addCsdn",map);
	
	}

 
	public int addCiopi(Map map) {
		System.out.println("sqlSessionTemplate;"+sqlSessionTemplate.toString());
		return sqlSessionTemplate.insert(getClass().getName()+".addCiopi",map);
	}
	
	/**
	 * 增加国家或地区爬行接口
	 */
	
	public int addCountryInfo(Map map) {
		return sqlSessionTemplate.insert(getClass().getName()+".addCountryInfo",map);
	}
	/**
	 * 增加企业基本信息&企业年报信息爬虫
	 * 当出现任何异常的时候,实行回滚操作
	 * @param addBsEntBasic
	 * @param addBsEntTrAn
	 * @return
	 */
	@Transactional(rollbackFor=Exception.class)
	public void  addBsEntBasic(Map addBsEntBasic){
		//企业资产信息
	  sqlSessionTemplate.insert(getClass().getName()+".addBsEntBasic",addBsEntBasic);
	}
	
	public void addBsEntTrAn(Map addBsEntTrAn){
		sqlSessionTemplate.insert(getClass().getName()+".addBsEntTrAn",addBsEntTrAn);
	}
	
	public String selectBaseDict(Map paramsMap ){
		String temp="1100";
		List list = this.sqlSessionTemplate.selectList(getClass().getName()+".selectBaseDict", paramsMap);
		if(list !=null && list.size()>0){
			Map tem = (Map)list.get(0);
			temp = tem.get("DICT_KEY")+"";
		}
		
		return temp;
	}
 
}
