package com.langchao.bigdata.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import org.apache.log4j.PropertyConfigurator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisCluster;

    /**
 * @ClassName: JedisUtil
 * @Description: TODO(jedis 数据工具类
 * 供实时业务分析取数用)
 * @author yebn
 * @time 2017-7-4上午9:57:28
 */
public class JedisUtil {
	
//	 jedis 数据缓存连接池
//	 private static JedisPool   pool = new JedisPool(RedisConstant.ADDR, RedisConstant.PORT); ;
	 private static JedisCluster jc=null;
	 static JedisUtil jedisUtil = new JedisUtil();
	 private static final  Logger log = LoggerFactory.getLogger(JedisUtil.class);
	 static{
		 PropertyConfigurator.configure(Thread.currentThread().getContextClassLoader().getResourceAsStream("resources/log4j.properties"));
	 }
	 public static JedisCluster redisClusterInstance(){
		 if(null == jc ){
			 Set<HostAndPort> jedisClusterNodes=new HashSet<HostAndPort>();
				Properties properties= new Properties();
				InputStream in =Thread.currentThread().getContextClassLoader().getResourceAsStream("resources/redis.properties");
				if(in==null){
					
					throw new RuntimeException("PropertiesUtil读取配置过程中redis.properties文件找不到！");
				}
				try {
					properties.load(in);
					if(!properties.containsKey("redis.cluster.addr")){
						throw new RuntimeException("redis.properties中没有找到[ redis.cluster.addr ]的key!");
					}
					String str = (String) properties.get("redis.cluster.addr");
					if(str==null||str.length()==0){
						throw new RuntimeException("redis.properties中[redis.cluster.addr]没有指定集群地址!!");
					}
					String[] strArr = str.split(",");
					for(int i=0;i<strArr.length;i++){
						String ipAddr = strArr[i].split("#")[0];
						String port = strArr[i].split("#")[1];
						jedisClusterNodes.add(new HostAndPort(String.valueOf(ipAddr),Integer.parseInt(port)));
					}
				} catch (IOException e) {
					throw new RuntimeException("PropertiesUtil 出现IO异常！"+e,e);
				}
				log.info("REDIS CLUSTER STARTING....");
				jc=new RedisCluster(jedisClusterNodes);
				log.info("REDIS CLUSTER STOPPING....");
		 }
		 return jc;
	 }
	 
	 /**构造一内部类,集成JedisCluster */
	private static class RedisCluster extends JedisCluster{
		private static final Logger logger = LoggerFactory.getLogger(RedisCluster.class);
		private Set<HostAndPort> nodes;
		public RedisCluster(Set<HostAndPort> nodes) {
			super(nodes);
			Iterator<HostAndPort> iter =  nodes.iterator();
			while(iter.hasNext()){
				HostAndPort hostAndPort =iter.next();
				log.info("HOST:"+hostAndPort.getHost()+"PORT:"+hostAndPort.getPort());
			}
		
		}
		
		private void setNodes(Set<HostAndPort> nodes ){
			this.nodes=nodes;
		}
		 
	 }
	 
	 public static JedisUtil getInstance (){
		 return jedisUtil;
	 }
	 
//	 static{
//
//		 //初始化连接池
//		 if(pool == null ){
//			 JedisPoolConfig config = new JedisPoolConfig();
//			 config.setMaxActive(RedisConstant.MAX_ACTIVE);
//	         config.setMaxIdle(RedisConstant.MAX_IDLE);
//	         config.setMaxWait(RedisConstant.MAX_WAIT);
//	         config.setTestOnBorrow(RedisConstant.TEST_ON_BORROW);
//	         if(!isAuth()){
//	        	   pool = new JedisPool(config, RedisConstant.ADDR, RedisConstant.PORT);
//	         }
//		 }
//	 
//	 }
	 
	 
	 private JedisUtil(){}
	 
	 /**
	  * 
	     * @Title: isAuth
	     * @Description: TODO(是否需要密码登录jedis 连接池)
	 	* @author: yebn
	     * @date: 2017-7-4
	     * @version: V1.0
	  */
	 private  static boolean isAuth(){
		 return false;
	 }
	 
	 /**
	  * 
	     * @Title: getJedis
	     * @Description: (取jedis实例)
	 	* @author: yebn
	     * @date: 2017-7-4
	     * @version: V1.0
	  */
	 public synchronized static JedisCluster getJedis(){
//		 Jedis jedis = new Jedis(RedisConstant.ADDR,RedisConstant.PORT);
//		 try
//		 {
//			 if(pool !=null )
//			 {
//				jedis = pool.getResource();
//			 } 
//		 }catch (Exception e ){
//			 e.printStackTrace();
//		 } 
		 	return redisClusterInstance();
	 }
	 
	 /**
	  * 
	     * @Title: returnResource
	     * @Description: TODO(释放jedis资源)
	 	* @author: yebn
	     * @date: 2017-7-4
	     * @version: V1.0
	  */
	 public static void returnResource(final Jedis jedis ){
		 if(jedis !=null ){
//			 pool.returnResource(jedis);
		 }
	 }

	 /**
	  * 
	     * @Title: get
	     * @Description: TODO(根据key取值)
	 	* @author: yebn
	     * @date: 2017-7-4
	     * @version: V1.0
	  */
	 public static String get(String key){
		 String value="";
		 try{
			 value = getJedis().get(key);
		 }catch(Exception e ){
			 
		 }finally{
			 //释放jedis资源
			 returnResource(null);
		 }
		 return value;
	 }
	 /**
	  * 
	     * @Title: set
	     * @Description: TODO(存入key-value)
	 	* @author: yebn
	     * @date: 2017-7-4
	     * @version: V1.0
	  */
	  public static String set(String key,String value){
	        try {  
	            return getJedis().set(key, value);  
	        } catch (Exception e) {  
	              
	            return "0";  
	        } finally {  
	            returnResource(null);  
	        }  
	    }  
	  
	  /**
	   * 
	      * @Title: del
	      * @Description: TODO(删除 redis 值,key可以是一个数组)
	  	* @author: yebn
	      * @date: 2017-7-4
	      * @version: V1.0
	   */
	    public static Long del(String...keys){  
	        try {  
	            return getJedis().del(keys);  
	        } catch (Exception e) {  
	              
	            return 0L;  
	        } finally {  
	            returnResource(null);  
	        }  
	    }  
	    
	    /**
	     * 
	        * @Title: append
	        * @Description: TODO(根据key追加value)
	    	* @author: yebn
	        * @date: 2017-7-4
	        * @version: V1.0
	     */
	    public static Long append(String key ,String str){  
	        Long res = null;  
	        try {  
	            res = getJedis().append(key, str);  
	        } catch (Exception e) {  
	              
	            return 0L;  
	        } finally {  
	            returnResource(null);  
	        }  
	        return res;  
	    }  
	  
	    /**
	     * 
	        * @Title: exists
	        * @Description: TODO(判断指定key是否存在)
	    	* @author: yebn
	        * @date: 2017-7-4
	        * @version: V1.0
	     */
	    public static Boolean exists(String key){
	        try {  
	            return getJedis().exists(key);  
	        } catch (Exception e) {  
	              
	              
	            return false;  
	        } finally {  
	            returnResource( null);  
	        }  
	    }  
	    
	    /**
	     * 
	        * @Title: exists
	        * @Description: TODO(判断指定key是否存在)
	    	* @author: yebn
	        * @date: 2017-7-4
	        * @version: V1.0
	     */
	    public static Boolean exists(byte[] bytes){
	        try {  
	            return getJedis().exists(bytes);  
	        } catch (Exception e) {  
	              
	              
	            return false;  
	        } finally {  
	            returnResource( null);  
	        }  
	    }  
	    
	    /**
	     * 
	        * @Title: setnx
	        * @Description: TODO(指定key设置指定value)
	    	* @author: yebn
	        * @date: 2017-7-4
	        * @version: V1.0
	     */
	    public static Long setnx(String key ,String value){
	        try {
	        	if(exists(key)){
	        		  return getJedis().setnx(key, value);  
	        	}
	        } catch (Exception e) {  
	              
	            return 0L;  
	        } finally {  
	            returnResource( null);  
	        }  
	        return 0l;
	    }  
	  
	    /**
	     * 
	        * @Title: setex
	        * @Description: TODO(指定key设置指定value,并设置周期)
	    	* @author: yebn
	        * @date: 2017-7-4
	        * @version: V1.0
	     */
	    public static String setex(String key,String value,int seconds){
	        String res = null;  
	        try {  
	        	if(exists(key)){
	        		  res = getJedis().setex(key, seconds, value);  
	        	}
	          
	        } catch (Exception e) {  
	              
	              
	        } finally {  
	            returnResource( null);  
	        }  
	        return res;  
	    }  
	    
	    /**
	     * 
	        * @Title: setrange
	        * @Description: TODO(针对指定位置的key替换指定str)
	    	* @author: yebn
	        * @date: 2017-7-4
	        * @version: V1.0
	     */
	    public static Long setrange(String key,String str,int offset){
	        try {  
	        	if(exists(key))
	        	{
	        		 return getJedis().setrange(key, offset, str);  
	        	}
	           
	        } catch (Exception e) {  
	              
	            return 0L;  
	        } finally {  
	            returnResource( null);  
	        }  
	        
	        return 0l;
	    }  
	    
	    /**
	     * 
	        * @Title: mget
	        * @Description: TODO(通过批量key获取批量list)
	    	* @author: yebn
	        * @date: 2017-7-4
	        * @version: V1.0
	     */
	    public static List<String> mget(String...keys){
	        List<String> values = null;  
	        try {  
	           
	        	values = getJedis().mget(keys);  
	          
	        } catch (Exception e) {  
	              
	              
	        } finally {  
	            returnResource(null);  
	        }  
	        return values;  
	    }  
 
	    /**
	     * 
	        * @Title: mset
	        * @Description: TODO(批量设置keys)
	    	* @author: yebn
	        * @date: 2017-7-4
	        * @version: V1.0
	     */
	    public static String mset(String...keysvalues){
	        String res = null;  
	        try {  
	            res = getJedis().mset(keysvalues);  
	        } catch (Exception e) {  
	              
	              
	        } finally {  
	            returnResource(null);  
	        }  
	        return res;  
	    }  
	    
	    /**
	     * 
	        * @Title: msetnx
	        * @Description: TODO(批量设置keys,如果key存在则失败回滚)
	    	* @author: yebn
	        * @date: 2017-7-4
	        * @version: V1.0
	     */
	    public static Long msetnx(String...keysvalues){
	        Long res = 0L;  
	        try {  
	            res =getJedis().msetnx(keysvalues);  
	        } catch (Exception e) {  
	              
	              
	        } finally {  
	            returnResource(null);  
	        }  
	        return res;  
	    }  
	    
	    /**
	     * 
	        * @Title: getset
	        * @Description: TODO(设置key值,并返回旧值)
	    	* @author: yebn
	        * @date: 2017-7-4
	        * @version: V1.0
	     */
	    public static String getset(String key,String value){  
	        String res = null;  
	        try {  
	            res = getJedis().getSet(key, value);  
	        } catch (Exception e) {  
	              
	              
	        } finally {  
	            returnResource(null);  
	        }  
	        return res;  
	    }  
	  
	    /**
	     * 
	        * @Title: incr
	        * @Description: TODO(对key指定的value加1)
	    	* @author: yebn
	        * @date: 2017-7-4
	        * @version: V1.0
	     */
	    public static Long incr(String key){  
	        Long res = null;  
	        try {  
	            res = getJedis().incr(key);  
	        } catch (Exception e) {  
	              
	              
	        } finally {  
	            returnResource( null);  
	        }  
	        return res;  
	    }  
	    
	    /**
	     * 
	        * @Title: incrBy
	        * @Description: TODO(针对key增加指定值)
	    	* @author: yebn
	        * @date: 2017-7-4
	        * @version: V1.0
	     */
	    public static Long incrBy(String key,Long integer){
	        Long res = null;  
	        try {  
	            res = getJedis().incrBy(key, integer);  
	        } catch (Exception e) {  
	              
	              
	        } finally {  
	            returnResource(null);  
	        }  
	        return res;  
	    }  
	  
	    /**
	     * 
	        * @Title: decr
	        * @Description: TODO(减1操作)
	    	* @author: yebn
	        * @date: 2017-7-4
	        * @version: V1.0
	     */
	    public static Long decr(String key) {  
	        Long res = null;  
	        try {  
	            res = getJedis().decr(key);  
	        } catch (Exception e) {  
	              
	              
	        } finally {  
	            returnResource(null);  
	        }  
	        return res;  
	    }  
	    
	    /**
	     * 
	        * @Title: decrBy
	        * @Description: TODO(减指定值)
	    	* @author: yebn
	        * @date: 2017-7-4
	        * @version: V1.0
	     */
	    public static Long decrBy(String key,Long integer){  
	        Long res = null;  
	        try {  
	            res = getJedis().decrBy(key, integer);  
	        } catch (Exception e) {  
	              
	              
	        } finally {  
	            returnResource(null);  
	        }  
	        return res;  
	    }  
	    
	    /**
	     * 
	        * @Title: serlen
	        * @Description: TODO(根据key返回指定值得长度)
	    	* @author: yebn
	        * @date: 2017-7-4
	        * @version: V1.0
	     */
	    public static Long serlen(String key){  
	        Long res = null;  
	        try {  
	            res = getJedis().strlen(key);  
	        } catch (Exception e) {  
	              
	              
	        } finally {  
	            returnResource(null);  
	        }  
	        return res;  
	    }  
	    
	    /**
	     * 
	        * @Title: hset
	        * @Description: TODO(通过key给指定的filed 设置值)
	    	* @author: yebn
	        * @date: 2017-7-4
	        * @version: V1.0
	     */
	    public  static Long hset(String key,String field,String value) {  
	        Long res = null;  
	        try {  
	            res = getJedis().hset(key, field, value);  
	        } catch (Exception e) {  
	              
	              
	        } finally {  
	            returnResource(null);  
	        }  
	        return res;  
	    }  
	    
	    /**
	     * 
	        * @Title: hkeys
	        * @Description: TODO(根据key返回所有field)
	    	* @author: yebn
	        * @date: 2017-7-4
	        * @version: V1.0
	     */
	    public static Set<String> hkeys(String key){  
	        Set<String> res = null;  
	        try {  
	            res = getJedis().hkeys(key);  
	        } catch (Exception e) {  
	              
	              
	        } finally {  
	            returnResource(null);  
	        }  
	        return res;  
	    }  
	    
	    /**
	     * 
	        * @Title: hgetall
	        * @Description: TODO(根据key返回所有的field value)
	    	* @author: yebn
	        * @date: 2017-7-4
	        * @version: V1.0
	     */
	    public static Map<String, String> hgetall(String key){
	        Map<String, String> res = null;  
	        try {  
	            res = getJedis().hgetAll(key);  
	        } catch (Exception e) {  
	            // TODO  
	        } finally {  
	            returnResource(null);  
	        }  
	        return res;  
	    }  
	    
	    /**
	     * 
	        * @Title: lpush
	        * @Description: TODO(通过key向list头部添加字符串)
	    	* @author: yebn
	        * @date: 2017-7-4
	        * @version: V1.0
	     */
	    public static Long lpush(String key ,String strs){
	        Long res = null;  
	        try {  
	            res = getJedis().lpush(key, strs);  
	        } catch (Exception e) {  
	              
	              
	        } finally {  
	            returnResource(null);  
	        }  
	        return res;  
	    }  
	  
	    /**
	     * 
	        * @Title: rpush
	        * @Description: TODO(向list尾部添加字符串)
	    	* @author: yebn
	        * @date: 2017-7-4
	        * @version: V1.0
	     */
	    public static Long rpush(String key ,String strs){
	        Long res = null;  
	        try {  
	            res = getJedis().rpush(key, strs);  
	        } catch (Exception e) {  
	              
	              
	        } finally {  
	            returnResource( null);  
	        }  
	        return res;  
	    } 
	    
	    /**
	        * @Title: lpop
	        * @Description: TODO(删除指定key的list值)
	    	* @author: yebn
	        * @date: 2017-7-4
	        * @version: V1.0
	     */
	      public static String lpop(String key){
	        String res = null;  
	        try {  
	            res = getJedis().lpop(key);  
	        } catch (Exception e) {  
	              
	              
	        } finally {  
	            returnResource( null);  
	        }  
	        return res;  
	    }  
	      
	      
	      public  static   byte[] lpop(byte[] key){
		        byte[] res = null;  
		        try {  
		            res = getJedis().lpop(key);  
		        } catch (Exception e) {  
		              
		              
		        } finally {  
		            returnResource(null);  
		        }  
		        return res;  
		    }  
	    
	    /**
	     * 
	        * @Title: lset
	        * @Description: TODO(向list指定下标 设置值)
	    	* @author: yebn
	        * @date: 2017-7-4
	        * @version: V1.0
	     */
	    public static String lset(String key ,Long index, String value){
	        String res = null;  
	        try {  
	            res = getJedis().lset(key, index, value);  
	        } catch (Exception e) {  
	              
	              
	        } finally {  
	            returnResource(null);  
	        }  
	        return res;  
	    }  
	    
	    /**
	        * @Title: lrange
	        * @Description: TODO(通过key获取list指定下标位置的value)
	    	* @author: yebn
	        * @date: 2017-7-4
	        * @version: V1.0
	     */
	    public List<String> lrange(String key,long start,long end){  
	        List<String> res = null;  
	        try {  
	            res = getJedis().lrange(key, start, end);  
	        } catch (Exception e) {  
	              
	              
	        } finally {  
	            returnResource(null);  
	        }  
	        return res;  
	    }  
	    
	    /**   
	     * 设置 list   
	     * @param <T>   
	     * @param key   
	     * @param value   
	     */    
	    public static <T> void setList(String key ,List<T> list){
	        try {
	        		getJedis().del(key.getBytes());
	        		for(int i=0;i<list.size();i++){
	        			getJedis().lpush(key.getBytes(),serialize(list.get(i)));
	        			//getJedis().set(key.getBytes(),serialize(list));   
	        		}
	            
	        } catch (Exception e) {    
	        }    
	    }    
	    
	    public static <T> void setList2(String key ,List<T> list){
	        try {
	        		getJedis().del(key);
	        		for(int i=0;i<list.size();i++){
	        			getJedis().lpush(key,(String)list.get(i));
	        			//getJedis().set(key.getBytes(),serialize(list));   
	        		}
	            
	        } catch (Exception e) {    
	        }    
	    }    
	    
	    /**   
	     * 获取list   
	     * @param <T>   
	     * @param key   
	     * @return list   
	     */    
	    @SuppressWarnings("unchecked")
		public  static <Map> List<Map> getList(String key,int startPos,int endPos){
	        if(getJedis() == null || !exists(key.getBytes())){    
	            return null;    
	        }    
	       
	        List<byte[]> in =  getJedis().lrange(key.getBytes(), startPos, endPos);      
	        List<Map> list = new ArrayList<Map>();
	        for(int i=0;i<in.size();i++){
	        	list.add((Map) deserialize(in.get(i)));
	        }
//	        List<T> list = (List<T>) deserialize(in);      
	        return list;    
	    }    
	    
	    /**   
	     * 设置 map   
	     * @param <T>   
	     * @param key   
	     * @param value   
	     */    
	    public static <T> void setMap(String key ,Map<String,T> map){    
	        try {    
	            getJedis().set(key.getBytes(),serialize(map));    
	        } catch (Exception e) {    
	             
	        }    
	    }    
	    /**   
	     * 获取list   
	     * @param <T>   
	     * @param key   
	     * @return list   
	     */    
	    @SuppressWarnings("unchecked")
		public static <T> Map<String,T> getMap(String key){
	        if(getJedis() == null || !getJedis().exists(key.getBytes())){    
	            return null;    
	        }    
	        byte[] in = getJedis().get(key.getBytes());      
	        Map<String,T> map = (Map<String, T>) deserialize(in);      
	        return map;    
	    }    
	    
	    /**
	        * @Title: serialize
	        * @Description: TODO(序列号对象:即将对象转成二进制流)
	    	* @author: yebn
	        * @date: 2017-7-4
	        * @version: V1.0
	     */
	    public static byte[] serialize(Object value) {
	        if (value == null) {    
	            throw new NullPointerException("无法序列化空对象!");    
	        }    
	        byte[] rv=null;    
	        ByteArrayOutputStream bos = null;    
	        ObjectOutputStream os = null;    
	        try {    
	            bos = new ByteArrayOutputStream();    
	            os = new ObjectOutputStream(bos);    
	            os.writeObject(value);    
	            os.close();    
	            bos.close();    
	            rv = bos.toByteArray(); 
	        } catch (IOException e) {    
	            throw new IllegalArgumentException("非法序列化对象!", e);    
	        } finally {    
	            try {  
	                 if(os!=null)os.close();  
	                 if(bos!=null)bos.close();  
	            }catch (Exception e2) {  
	             e2.printStackTrace();  
	            }    
	        }    
	        return rv;    
	    }    
	    
	    /**
	     * 
	        * @Title: deserialize
	        * @Description: TODO(反序列号:即将二进制流转成对象)
	    	* @author: yebn
	        * @date: 2017-7-4
	        * @version: V1.0
	     */
	    public static Object deserialize(byte[] in) {    
	        Object rv=null;    
	        ByteArrayInputStream bis = null;    
	        ObjectInputStream is = null;    
	        try {    
	            if(in != null) {    
	                bis=new ByteArrayInputStream(in);    
	                is=new ObjectInputStream(bis);    
	                rv=is.readObject();    
	                is.close();    
	                bis.close();    
	            }    
	        } catch (Exception e) {    
	            e.printStackTrace();  
	         }finally {    
	             try {  
	                 if(is!=null)is.close();  
	                 if(bis!=null)bis.close();  
	             } catch (Exception e2) {
	                 e2.printStackTrace();  
	             }  
	         }  
	        return rv;    
	    }  
	    
	    public static void main(String[] args) {
	    	redisClusterInstance().set("1", "redis测试");
	    	System.out.println(redisClusterInstance().get("1"));
	    }
}
