package com.langchao.bigdata.crawl;

import javax.annotation.Resource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.langchao.bigdata.dao.CsdnDao;
import com.langchao.bigdata.model.CsdnInfo;


 @RunWith(SpringJUnit4ClassRunner.class)
 @ContextConfiguration(locations={"classpath:resources/application-mybatis.xml"})
public class AppTest 
{ 
	 @Resource
	 private CsdnDao csdnDao;
	 
//	 @Ignore
	 @Test
	 public void test(){
		 //配置mybatis方法
//		 Map map = new HashMap();
//		 map.put("id", 1);
//		 map.put("title", "sss");
//		 map.put("tags", "sss");
//		 map.put("view", 3);
//		 map.put("date", "2017-06-09-21");
//		 map.put("category", "dsfds");
//		 map.put("comments", 4);
//		 map.put("copyright", 2);
		 //注解mybatis方法
		 CsdnInfo csdnInfo = new CsdnInfo();
		 csdnInfo.setId(31);
		 csdnInfo.setTitle("xxx");
		 csdnInfo.setTags("ddd");
		 csdnInfo.setView(3);
		 csdnInfo.setDate("2017-06-09-21");
		 csdnInfo.setCategory("ddd");
		 csdnInfo.setComments(4);
		 csdnInfo.setCopyright(1);
//		 csdnDao.addCsdn(csdnInfo);
		 System.out.println("成功!");
	 }
	
}
