package com.langchao.bigdata.crawl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.management.JMException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.langchao.bigdata.scheduler.JedisScheduler;
import com.langchao.bigdata.util.DownLoader;

import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.model.OOSpider;
import us.codecraft.webmagic.monitor.SpiderMonitor;
import us.codecraft.webmagic.processor.PageProcessor;

public class MMPaqu  implements PageProcessor{
	private static final Logger log = LoggerFactory.getLogger(MMPaqu.class);
	private static final String filePath="F:\\image\\";
	private static final String initUrl="https://mm.taobao.com/json/request_top_list.htm?page=1";
//	private static final String initUrl="http://www.27270.com/meishitupian";
	 //列表页正则表达式
    private static final String REGEX_PAGE_URL = "https://mm\\.taobao\\.com/json/request_top_list\\.htm\\?page=\\w+";
//    private static final String REGEX_PAGE_URL = "http:\\/\\/www\\.27270\\.com/meishitupian";


    // 爬取的列表页，页数。
    private  final int PAGE_SIZE = 100;

    private static int num=0;
    // 抓取网站的相关配置，包括：编码、抓取间隔、重试次数等
    private static Site site = Site.me().setRetryTimes(10).setSleepTime(1000);
	
	@Override
		public void process(Page page ) {
			//获取所有列
			List<String> targetPageList = new ArrayList<String>();
			for(int i =0;i<PAGE_SIZE ;i++){
				targetPageList.add("https://mm.taobao.com/json/request_top_list.htm?page="+(i+1));
			}
			
			page.addTargetRequests(targetPageList);
			//匹配列表页
			if(page.getUrl().regex(REGEX_PAGE_URL).match()){
				//获取所有的链接页
				 List<String> urls = page.getHtml().xpath("//a[@class='lady-name']/@href").all();
				 for(int j=0;j<urls.size();j++){
					  page.addTargetRequest(urls.get(j).replace("self/model_card.htm","self/info/model_info_show.htm"));
				 }
				 log.info("AAAAAA:"+urls.toString());
			}else{
				num++;
	            // 获取 class为mm-p-info-cell clearfix 的ul /li /span/文本，作为图片保存图片名。
	            String nickname = page.getHtml().xpath("//div[@class='mm-p-info mm-p-base-info']/ul/li/span/html()").toString();
	            // 获取 class为mm-p-modelCard 的div /a /img  src 值，此为图片URL.
	            String imgUrl = page.getHtml().xpath("//div[@class='mm-p-modelCard']/a").css("img","src").toString();
//				String imgUrl =page.getHtml().xpath("//*[@id=\"picBody\"]/p/a/img/@src").toString();
//				String imgName = page.getHtml().xpath("//*[@id=\"picBody\"]/p/a/img/@alt").toString();
	            try {
	                // 根据图片URL 下载图片方法
	            	log.info("图片下载链接为:https:"+imgUrl+",nickname="+nickname);
	                DownLoader.download("https:"+imgUrl, nickname+".jpg",filePath);
			}catch(Exception e ){
				log.error("图片下载出现异常!");
				}	
			}
		}
@Override
public Site getSite() {
	return this.site;
}

public static void main(String[] args) {
    long startTime, endTime;
    System.out.println("========MM爬虫【启动】！=========");
    startTime = new Date().getTime();
   Spider spider =  Spider.create(new MMPaqu());
   spider.addUrl(initUrl);
   spider.setScheduler(new JedisScheduler(log));
   //添加spider监控
   try {
	SpiderMonitor.instance().register(spider);
} catch (JMException e) {
	e.printStackTrace();
}
   	spider.thread(5).run();
    endTime = new Date().getTime();
    System.out.println("========MM爬虫【结束】！=========");
    System.out.println("一共爬到" + num + "个MM！用时为：" + (endTime - startTime) / 1000 + "s");

}


}
