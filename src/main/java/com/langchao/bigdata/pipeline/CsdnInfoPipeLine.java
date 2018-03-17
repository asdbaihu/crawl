package com.langchao.bigdata.pipeline;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.langchao.bigdata.dao.CsdnDao;
import com.langchao.bigdata.model.CsdnInfo;

import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.pipeline.PageModelPipeline;

@Component("csdnInfoPipeLine")
public class CsdnInfoPipeLine implements PageModelPipeline<CsdnInfo> {
	
	private int articleSize=0;
	@Resource
	private CsdnDao csdnDao ; 
	
	public void process(CsdnInfo csdnInfo, Task task) {
		System.err.println(csdnInfo.toString());
//		csdnDao.addCsdn(csdnInfo);
		articleSize++;
	}

	public int getArticleSize(){
		return articleSize;
	}

}
