package com.langchao.bigdata.model;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.model.AfterExtractor;
import us.codecraft.webmagic.model.annotation.TargetUrl;

import com.langchao.bigdata.dao.CsdnDao;
import com.langchao.bigdata.dao.ICsdnDao;
import com.langchao.bigdata.util.TransCodUtil;
import com.langchao.bigdata.util.WebContextUtil;
import com.sun.beans.WeakCache;

/**
 * 
 * @author yuenbin
 *
 */
@TargetUrl("http://114.xixik.com/country")
public class CountryInfo implements AfterExtractor {
	
	private String counCode;// 国家或地区编号
	
    private String counName;//国家或地区中文名称
	
    private String shortName;// 国家或地区中文简称
	
    private String engName;// 国家或地区英文名称
	
    private String shortEngName;// 国家或地区英文简称
    public String getCounCode() {
		return counCode;
	}

	public void setCounCode(String counCode) {
		this.counCode = counCode;
	}

	public String getCounName() {
		return counName;
	}

	public void setCounName(String counName) {
		this.counName = counName;
	}

	public String getShortName() {
		return shortName;
	}

	public void setShortName(String shortName) {
		this.shortName = shortName;
	}

	public String getEngName() {
		return engName;
	}

	public void setEngName(String engName) {
		this.engName = engName;
	}

	public String getShortEngName() {
		return shortEngName;
	}

	public void setShortEngName(String shortEngName) {
		this.shortEngName = shortEngName;
	}

	@Override
    public String toString() {
        return "counCode=" + this.getCounCode() + ", counName=" + this.getCounName() +
        		", shortName=" + this.getShortName() + ", engName=" + this.getEngName() + ", shortEngName="
                + this.getShortEngName() + "";
    }

	public void afterProcess(Page page) {
//			WebContextUtil.getAllDefBeans();
		  CsdnDao csdnDao = WebContextUtil.getBean("csdnDao"); 
		  int seq=0;
		  synchronized (this) {
				for(int j=3;j<=242;j++){
					//抓取code 
					String counCode =page.getHtml().xpath("/html/body/div[3]/div[7]/table/tbody/tr["+j+"]/td[6]/text()").toString();
					this.setCounCode(counCode);
					//抓取中文简称
					String shortName = page.getHtml().xpath("/html/body/div[3]/div[7]/table/tbody/tr["+j+"]/td[2]/html()").toString();
					try {
						shortName=new String(shortName.getBytes("gb2312"),"ISO-8859-1");
						shortName=new String(shortName.getBytes("ISO-8859-1"),"UTF-8");
					} catch (UnsupportedEncodingException e) {
						e.printStackTrace();
					}
					this.setShortName(TransCodUtil.getUTF8StringFromGBKString(shortName));
					//抓取英文简称
					String shortEngName = page.getHtml().xpath("/html/body/div[3]/div[7]/table/tbody/tr["+j+"]/td[3]/text()").toString();
					this.setShortEngName(shortEngName);
					// 抓取中文以及英文名称信息
			        String info =page.getHtml().xpath("/html/body/div[3]/div[7]/table/tbody/tr["+j+"]/td[7]/text()").toString();
			        String[] infos=info.split("\\s");
			        try {
						infos[1]=new String(infos[1].getBytes("UTF-8"),"UTF-8");
						infos[1]=new String(infos[1].getBytes("ISO-8859-1"),"UTF-8");
					} catch (UnsupportedEncodingException e) {
						e.printStackTrace();
					}
							this.setCounName(TransCodUtil.getUTF8StringFromGBKString(infos[1]));
							String str="";
						   	for(int i=2;i<infos.length;i++){
						   		str+= infos[i]+" ";
				        		this.setEngName(str);
				        	}
			    		Map dataMap = new HashMap();
			    		dataMap.put("id",++seq);
			    		dataMap.put("counCode", this.getCounCode().trim());
			    		dataMap.put("counName", this.getCounName().trim());
			    		dataMap.put("shortName", this.getShortName().trim());
			    		dataMap.put("engName", this.getEngName().trim());
			    		dataMap.put("shortEngName", this.getShortEngName().trim());
//			    		System.out.println(dataMap);
			    		System.out.println(this.toString());
			        	csdnDao.addCountryInfo(dataMap);
			        	try {
							Thread.sleep(300);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
			}
		}
     
	}
}
