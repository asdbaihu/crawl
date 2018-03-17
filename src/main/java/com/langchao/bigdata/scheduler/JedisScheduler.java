package com.langchao.bigdata.scheduler;


import java.util.HashMap;
import java.util.Map;

import org.apache.commons.httpclient.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.seleniumhq.jetty7.util.log.Log;
import org.slf4j.Logger;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.langchao.bigdata.util.JedisUtil;
import com.langchao.bigdata.util.MD5;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import us.codecraft.webmagic.Request;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.scheduler.DuplicateRemovedScheduler;
import us.codecraft.webmagic.scheduler.MonitorableScheduler;
import us.codecraft.webmagic.scheduler.Scheduler;
import us.codecraft.webmagic.scheduler.component.DuplicateRemover;

/**
 * webMagic 爬虫调度管理
 * 对爬取的链接进行去重处理
 * 爬虫url的去重使用redis队列管理，url通过MD5加密之后存放到redis集群中
 * @author yuenbin
 */
public class JedisScheduler extends DuplicateRemovedScheduler 
implements DuplicateRemover{
	private JedisCluster jedisCluster=JedisUtil.redisClusterInstance(); 
	private Logger log = null; 
	//存放队列
	private static final String LIST_PRIX="LIST_PRIX_";
	//存放集合
	private static final String SET_PRIX="SET_PRIX_";
	//存放map集合
	private static final String MAP_PRIX="MAP_PRIX_";
	 
	public JedisScheduler(Logger log ){
		setDuplicateRemover(this);
		this.log=log;
	}
	
	/**
	 * 从队列头部
	 * 移除一个元素
	 */
	@Override
	public synchronized Request poll(Task task) {
		String listKey = getListKey(task);
		String url = jedisCluster.lpop(listKey);
		if(url == null ){
			return null;
		}
		String mapKey = getMapKey(task);
		String field = MD5.MD5Encode(url);
		String text = jedisCluster.hget(mapKey, field);
		if(text !=null ){
			 JSONArray array = JSON.parseArray(text); 
			 BasicNameValuePair[] nameValuePairs = new BasicNameValuePair[array.size()];  
			 log.info("Request 中 BasicNameValuePair 是："+nameValuePairs);
                for (int i = 0; i < array.size(); i++) {
                    JSONObject json = JSONObject  
                            .parseObject(array.getString(i));  
                    nameValuePairs[i] = new BasicNameValuePair(json.getString("name"), json.getString("value"));  
                }
                Request r = new Request(url);  
                Map<String, Object> map = new HashMap<String, Object>();  
                map.put("nameValuePair", nameValuePairs);  
                r.setMethod("post");  
                r.setExtras(map);  
                return r;  
		}
		Request request = new Request(url);
		return request;
	}
	
	
	@Override
	protected void pushWhenNoDuplicate(Request request, Task task) {
		String listKey=getListKey(task);
		jedisCluster.rpush(listKey, request.getUrl());
		if(request.getExtras() != null){
			 log.info("Request 中 getExtras 是："+request.getExtras());
			String field = MD5.MD5Encode(request.getUrl());
			String value = JSON.toJSONString(request.getExtra("nameValuePair"));
			jedisCluster.hset(getMapKey(task), field, value);
		}
	}

	/**
	 * 判断该集合是否有重复
	 */
	@Override
	public boolean isDuplicate(Request request, Task task) {
		String setKey = getSetKey(task);
		boolean isDuplicate = jedisCluster.sismember(setKey, request.getUrl());
		if(!isDuplicate){
			jedisCluster.sadd(setKey, request.getUrl());
		}
		return isDuplicate;
	}
	
	/**
	 * 
	 */
	@Override
	public void resetDuplicateCheck(Task task) {
		String setKey = getSetKey(task);
		jedisCluster.del(setKey);
	}
	
//	/**
//	 * 获取队列长度
//	 */
//	@Override
//	public int getLeftRequestsCount(Task task) {
//		Long  size = jedisCluster.llen(getListKey(task));
//		log.info("REDIS 集群中的总的队列元素共:"+size.intValue());
//		return size.intValue();
//	}

	/**
	 * 获取集合大小
	 */
	@Override
	public int getTotalRequestsCount(Task task) {
		Long total = jedisCluster.scard(getSetKey(task));
		log.info("REDIS 集群中的总的集合元素共:"+total.intValue());
		return total.intValue();
	}
	
	/**
	 * 获取队列key
	 * @param task
	 * @return
	 */
	private static String getListKey(Task task ){
		return LIST_PRIX+task.getUUID();
	}
	/**
	 * 获取集合key
	 * @param task
	 * @return
	 */
	private static String getSetKey(Task task ){
		return SET_PRIX+task.getUUID();
	}
	/**
	 * 获取map key
	 * @param task
	 * @return
	 */
	private static String getMapKey(Task task ){
		return MAP_PRIX+task.getUUID();
	}
	
}
