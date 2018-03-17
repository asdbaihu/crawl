package com.langchao.bigdata.crawl;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.TimeUnit;


import org.apache.http.HttpResponse;
import org.apache.http.client.CookieStore;
import org.apache.http.client.HttpResponseException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.cookie.BasicClientCookie;
import org.apache.log4j.PropertyConfigurator;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.openqa.selenium.By;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Component;

import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.model.OOSpider;

import com.langchao.bigdata.model.BsEntBasic;
import com.langchao.bigdata.pipeline.BsEntBasicPipeLine;
import com.langchao.bigdata.util.ChromeDriver;
import com.langchao.bigdata.util.HttpClient;
import com.langchao.bigdata.util.SslUtils;

/**
 * 爬虫的具体实现
 * @author yuenbin
 *
 */
@Component
public class CrawlForBs {
 private static final  Logger log = LoggerFactory.getLogger(CrawlForBs.class);
  private static HttpClient httpClient = new HttpClient();
	/**指明子类实例化*/
	@Qualifier("bsEntBasicPipeLine")
	@Autowired
	private BsEntBasicPipeLine bsEntBasicPipeLine;
	
	private static Set<Cookie> cookies ;
	
	private static String filePath="D:\\cookie.txt";
	
	private static String baseUrl = "http://www.runoob.com/";
	
	private static String driverPath ="D:\\chromedriver.exe";
	 static{
		 PropertyConfigurator.configure(Thread.currentThread().getContextClassLoader().getResourceAsStream("resources/log4j.properties"));
	 }
	public void crawl(){
		
		  System.err.println("=======WebMagic 爬虫启动！=========");
	    long startTime, endTime;
	    startTime = System.currentTimeMillis();
	    //如果是https请求则忽略掉
	    URL u;
		try {
			u = new URL("https://bj.tianyancha.com/search/");
			 if("https".equalsIgnoreCase(u.getProtocol())){
		            SslUtils.ignoreSsl();
		            OOSpider.create(Site.me()
		            		.setDomain("www.tianyancha.com")
		            		.addCookie("d534b0fc290df538b4758", "1520236231,1520254417,1520254827")
		            		.addCookie("Hm_lvt_e92c8d65d92d534b0fc290df538b4758", "1520236231")
		            		.addCookie("RTYCID", "16742da3ba054a68a9aeffa4a18244eb")
		            		.addCookie("TYCID", "db2f4610204911e8807441d9d8331e6a")
		            		.addCookie("aliyungf_tc", "AQAAABgkfnufPA8AuB4IcP/WvX5OhdrD")
		            		.addCookie("auth_token", "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiIxNzc3ODAzNzY2OSIsImlhdCI6MTUyMDI1NDQ4MSwiZXhwIjoxNTM1ODA2NDgxfQ.yeIgkG3oAIRUn6e4X4w-G0L3GdIffasaWWvRRgoAK9QA3HNkKcYCq9oFnIPzUDl6ZzFyEPUTaQB17Dn7blNVqQ")
		            		.addCookie("bannerFlag", "true")
		            		.addCookie("csrfToken", "AVF4AHNPUMRyhCmHSUsZA9uM")
		            		.addCookie("ssuid", "2777471656")
		            		.addCookie("tyc-user-info", "%257B%2522token%2522%253A%2522eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiIxNzc3ODAzNzY2OSIsImlhdCI6MTUyMDI0NzE4OCwiZXhwIjoxNTM1Nzk5MTg4fQ.dAw3iM33dbpmjy93Ycuypo3pfoJWYpadtQIfWrs8Os8bgsaXj2Gy43Dh45P9yDmtm0fIVWxhaY8kBvz5qIabIw%2522%252C%2522integrity%2522%253A%25220%2525%2522%252C%2522state%2522%253A%25220%2522%252C%2522vipManager%2522%253A%25220%2522%252C%2522vnum%2522%253A%25220%2522%252C%2522onum%2522%253A%25220%2522%252C%2522mobile%2522%253A%252217778037669%2522%257D")
		            		.addCookie("undefined", "db2f4610204911e8807441d9d8331e6a")
		            		.addHeader("Cookie", "aliyungf_tc=AQAAACbldjK7fQAAGmA4OnL9weODkphE; csrfToken=yIIkwG-929IrMGr1jQxIVYec; TYCID=f9de4550236611e8af9defa1a7db8577; undefined=f9de4550236611e8af9defa1a7db8577; Hm_lvt_e92c8d65d92d534b0fc290df538b4758=1520578597; ssuid=4853011095; tyc-user-info=%257B%2522token%2522%253A%2522eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiIxNzc3ODAzNzY2OSIsImlhdCI6MTUyMDU3OTYyOCwiZXhwIjoxNTM2MTMxNjI4fQ.C_3S1G34MuPKwIYzpRI2Pd1OHZSn5G24kYanYA6R0UfS2XIqyNghnOY14Z1sc8BgoYjoAUx7vRMMQ9YgfZH5Kg%2522%252C%2522integrity%2522%253A%25220%2525%2522%252C%2522state%2522%253A%25220%2522%252C%2522vipManager%2522%253A%25220%2522%252C%2522vnum%2522%253A%25220%2522%252C%2522onum%2522%253A%25220%2522%252C%2522mobile%2522%253A%252217778037669%2522%257D; auth_token=eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiIxNzc3ODAzNzY2OSIsImlhdCI6MTUyMDU3OTYyOCwiZXhwIjoxNTM2MTMxNjI4fQ.C_3S1G34MuPKwIYzpRI2Pd1OHZSn5G24kYanYA6R0UfS2XIqyNghnOY14Z1sc8BgoYjoAUx7vRMMQ9YgfZH5Kg; Hm_lpvt_e92c8d65d92d534b0fc290df538b4758=1520579646")
		    				.setCharset("UTF-8")
		                    .addHeader("User-Agent","Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/64.0.3282.186 Safari/537.36")
		                    .addHeader("Connection", "keep-alive").addHeader("Referer", "http://www.imooc.com/"),
		                    bsEntBasicPipeLine, BsEntBasic.class)
		                    .addUrl("https://bj.tianyancha.com/")
		                    .thread(5).run();
		    	
		        }  
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}  
		
		  endTime = System.currentTimeMillis();
		  System.err.println("======= WebMagic 爬虫结束,共耗时:"+(endTime-startTime)/1000+"秒,共抓取总数:"+bsEntBasicPipeLine.getArticleSize()+"=========");

	}
	
	/**
	 * 实现WebMagic 自动登录
	 */
	private     void webMagicLogin(){
		 // 初始化参数据
//		System.setProperty("sun.net.client.defaultConnectTimeout", "19500");
//		System.setProperty("sun.net.client.defaultReadTimeout", "19500");
        System.setProperty("webdriver.chrome.driver", "D:\\chromedriver_2.35\\chromedriver.exe");
		 ChromeDriver cd = new ChromeDriver(driverPath);
		 WebDriver chromeDriver = cd.getdriver();
		//加载页面
		 chromeDriver.manage().window().maximize();
		chromeDriver.get(baseUrl);
		//等待页面加载完成
		try {
			Thread.sleep(300);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		log.info("WebMagic 页面加载完成.....");
		//获取页面元素
		//用户名
		WebElement userName = chromeDriver
				.findElement(By.id("j_username"));
		log.info(userName.toString());
//				.findElement(By.cssSelector(".pb30 .position-rel"))
//				.findElement(By.cssSelector("input[ng-model=contact.phone]"));
//		//密码
//		WebElement passWd = chromeDriver
//				.findElement(By.cssSelector(".modulein .modulein1 .mobile_box .pl30 .pr30 .f14 .collapse .in"))
//				.findElement(By.cssSelector(".pb40 .position-rel"))
//				.findElement(By.cssSelector("input[ng-model=contact.word]"));
//		//自动登录
//		WebElement remember = chromeDriver
//				.findElement(By.cssSelector(".modulein .modulein1 .mobile_box .pl30 .pr30 .f14 .collapse .in"))
//				.findElement(By.cssSelector(".contact.autoLogin"));
//		//登录按钮
//		WebElement btn = chromeDriver
//				.findElement(By.cssSelector(".modulein .modulein1 .mobile_box .pl30 .pr30 .f14 .collapse .in"))
//				.findElement(By.cssSelector(".c-white .b-c9 .pt8 .f18 .text-center .login_btn"));
		//操作页面元素
//		userName.clear();
//		userName.sendKeys("17778037669");
//		passWd.clear();
//		passWd.sendKeys("wst123456");
//		remember.click();
		
		//提交表单(相当于点击login按钮)
//		btn.submit();
		try {
			Thread.sleep(3000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	
		//获取cookie
		cookies = chromeDriver.manage().getCookies();
		CookieStore cookieStore = new BasicCookieStore();
		Iterator<Cookie> iter =cookies.iterator();
		while(iter.hasNext()){
			Cookie cookie = iter.next();
			BasicClientCookie bcc = new BasicClientCookie(cookie.getName(), cookie.getValue());
			bcc.setDomain(cookie.getDomain());
			bcc.setPath(cookie.getPath());
			cookieStore.addCookie(bcc);
		}
		
		ObjectOutputStream out = null ; 
		try {
			out=new ObjectOutputStream(new FileOutputStream(new File(filePath)));
			out.writeObject(cookieStore);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			try {
				out.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		chromeDriver.close();
		
	}
	
	/**
	 * 使用http-client 的方式进行模拟登陆
	 */
	private static void printPageInfo(){
		//构造页面
		HttpUriRequest httpUriRequest =new HttpPost("https://www.tianyancha.com/login");
//		Accept: text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8
//				Accept-Encoding: gzip, deflate, br
//				Accept-Language: zh-CN,zh;q=0.9
//				Cache-Control: no-cache
//				Connection: keep-alive
		//构造请求头
		httpUriRequest.setHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8");
		httpUriRequest.setHeader("Accept-Encoding", "gzip, deflate, br");
		httpUriRequest.setHeader("Accept-Language","zh-CN,zh;q=0.9");
		httpUriRequest.setHeader("Cache-Control", "no-cache");
		httpUriRequest.setHeader("Connection", "keep-alive");
		//增加浏览器UA识别信息
		httpUriRequest.setHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/65.0.3325.146 Safari/537.36");
		StringBuffer result = new StringBuffer();
		result = HttpClient.execute(httpUriRequest);
		log.info(result.toString());
	}
	
	
	private static void login (){
		String html =null; 
		try {
			  html = HttpClient.doGet("https://www.tianyancha.com/login", "", "UTF-8");
			  Document doc = Jsoup.parse(html);
			  Element tel = doc.select(".pb30 .position-rel").get(0);
			  Element pass = doc.select(".pb40 .position-rel").get(0);
			  
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		System.out.println(html);
	}
	
	
	public static void main(String[] args) {
		ApplicationContext app = new ClassPathXmlApplicationContext("classpath:resources/*.xml");
		final CrawlForBs crawl = app.getBean(CrawlForBs.class);
		crawl.crawl();
		
	}
		 
}
