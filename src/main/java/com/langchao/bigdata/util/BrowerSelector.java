package com.langchao.bigdata.util;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

public class BrowerSelector {
	public static WebDriver Brower(WebDriver dr, String BrowerNmae, String url) throws InterruptedException {
		if (BrowerNmae.equals("cc")) {

			ChromeDriver chrome = new ChromeDriver("D:\\driverAndjar\\chromedriver.exe");
			dr = chrome.getdriver();

		} else if (BrowerNmae.equals("ff")) {

			dr = new FirefoxDriver();

		}  
		dr.manage().window().maximize();
		dr.get(url);
		Thread.sleep(1000);
        return dr;
	}

}
