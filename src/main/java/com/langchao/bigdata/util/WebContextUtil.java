package com.langchao.bigdata.util;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Bean 实例化工具
 * @author yuenbin
 *
 */
public class WebContextUtil implements ApplicationContextAware {

	private static ApplicationContext applicationContex;
	
	
	public static void  getAllDefBeans(){
		checkApplicationContext();
		String[] beans = applicationContex.getBeanDefinitionNames();
		if(beans !=null ){
			for(int i =0;i<beans.length;i++){
				System.out.println(beans[i].toString());
			}
		}
		
	}
	
	public static <T> T getBean(String name){
		checkApplicationContext();
		return (T)applicationContex.getBean(name);
	}
	
	public static <T> T getBean(Class<?> cls ){
		checkApplicationContext();
		return (T)applicationContex.getBeansOfType(cls);
	}
	
	public void setApplicationContext(ApplicationContext applicationContex)
			throws BeansException {
		this.applicationContex=applicationContex;
	}
	
	public static void cleanApplicationContext(){
		applicationContex=null;
	}
	
	private static void checkApplicationContext(){
		if(applicationContex == null ){
			applicationContex = new ClassPathXmlApplicationContext("classpath:resources/*.xml");
		}
	}
	
}
