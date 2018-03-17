package com.langchao.bigdata.pipeline;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.pipeline.PageModelPipeline;

import com.langchao.bigdata.dao.CsdnDao;
import com.langchao.bigdata.model.BsEntBasic;
/**
 * 企业基本信息数据库通道建立
 * @author yuenbin
 *
 */
@Component("bsEntBasicPipeLine")
public class BsEntBasicPipeLine implements PageModelPipeline<BsEntBasic> {
	private Logger log = LoggerFactory.getLogger(BsEntBasicPipeLine.class);
	
	private int articleSize=0;
	
	private Map dataMap = new HashMap();
	
	@Resource
	CsdnDao csdnDao ;
	@SuppressWarnings("unchecked")
	public void process(BsEntBasic bsEntBasic, Task task) {
		//企业基本信息
		Map addBsEntBasic=new HashMap();
		//企业年报信息
		Map addBsEntTrAn =new HashMap();
		Class<?> cls = BsEntBasic.class;
		Field[] field = cls.getDeclaredFields();
		for(int i=0;i<field.length;i++){
			field[i].setAccessible(true);//设置属性可以访问
			String name = field[i].getName();//获取属性名称
			try {
				addBsEntBasic.put(name,field[i].get(bsEntBasic));
				addBsEntTrAn.put(name,field[i].get(bsEntBasic));
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} 
		}
		
		String MMB_TYPE = csdnDao.selectBaseDict(addBsEntBasic);
		addBsEntBasic.put("MMB_TYPE", MMB_TYPE);
		//初始化年报参数
		addBsEntTrAn.put("TEL",addBsEntBasic.get("PHONE"));
		addBsEntTrAn.put("ADDR", addBsEntBasic.get("DOM"));
		addBsEntTrAn.put("BUSI_STATUS", "01");
		addBsEntTrAn.put("EMP_NUM", "0");
		addBsEntTrAn.put("ISSUE_DEPTID", addBsEntBasic.get("REG_ORG"));
		addBsEntTrAn.put("IS_VALID", "01");
		csdnDao.addBsEntBasic(addBsEntBasic);
		csdnDao.addBsEntTrAn(addBsEntTrAn);
		   
//		csdnDao.addBsEntBasic(addBsEntBasic, addBsEntTrAn);
		log.info("企业基本信息{"+addBsEntBasic+"},\n企业年报{"+addBsEntTrAn+"}");
		this.articleSize++;
	}

	public int getArticleSize(){
		return articleSize;
	}
 
}
