package com.langchao.bigdata.pipeline;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.pipeline.PageModelPipeline;

import com.langchao.bigdata.dao.CsdnDao;
import com.langchao.bigdata.model.CountryInfo;

@Component("countryInfoPipeLine")
public class CountryInfoPipeLine implements PageModelPipeline<CountryInfo> {
	private Logger log = LoggerFactory.getLogger(CountryInfoPipeLine.class);
	
	private int articleSize=0;
	
	private Map dataMap = new HashMap();
	
	@SuppressWarnings("unchecked")
	public void process(CountryInfo countryInfo, Task task) {
		Class<?> cls = CountryInfo.class;
		
		Field[] field = cls.getDeclaredFields();
		for(int i=0;i<field.length;i++){
			field[i].setAccessible(true);//设置属性可以访问
			
			String name = field[i].getName();//获取属性名称
		
			try {
				System.out.println("属性名称:["+name+",属性值:"+field[i].get(countryInfo)+""+"]");
				log.debug("属性名称:["+name+",属性值:"+field[i].get(countryInfo)+""+"]");
				dataMap.put(name,field[i].get(countryInfo));
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}//获取属性值
		}
	}

	public int getArticleSize(){
		return articleSize;
	}
}
