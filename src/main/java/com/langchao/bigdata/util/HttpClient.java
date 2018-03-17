package com.langchao.bigdata.util;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpUriRequest;

/**
 * HTTP client工具
 * @author yuenbin
 */
public class HttpClient {

	/**
	 * 默认超时时间1分钟
	 */
	private static final int DEFAULT_TIMEOUT_MINUTES = 1;

	/**
	 * GET请求方法
	 */
	private static final String METHOD_TYPE_GET = "GET";

	/**
	 * POST请求方法
	 */
	private static final String METHOD_TYPE_POST = "POST";

	/**
	 * form表单数据类型
	 */
	private static final String CONTENT_TYPE_FORM = "application/x-www-form-urlencoded";

	/**
	 * xml报文数据类型
	 */
	private static final String CONTENT_TYPE_XML = "text/xml";

	/**
	 * json数据类型
	 */
	private static final String CONTENT_TYPE_JSON = "application/json";

	/**
	 * 默认字符集UTF-8
	 */
	private static final String DEFAULT_CHARSET = "UTF-8";

	/**
	 * 执行GET方式表单请求
	 * 
	 * @param url
	 * @param msg
	 * @return
	 * @throws IOException
	 */
	public static String doGet(String url, String msg) throws IOException {
		return doSend(url, msg, METHOD_TYPE_GET, CONTENT_TYPE_FORM, DEFAULT_CHARSET, DEFAULT_TIMEOUT_MINUTES);
	}

	/**
	 * 执行GET方式表单请求
	 * 
	 * @param url
	 * @param msg
	 * @param charset
	 * @return
	 * @throws IOException
	 */
	public static String doGet(String url, String msg, String charset) throws IOException {
		return doSend(url, msg, METHOD_TYPE_GET, CONTENT_TYPE_FORM, charset, DEFAULT_TIMEOUT_MINUTES);
	}

	/**
	 * 执行GET方式表单请求
	 * 
	 * @param url
	 * @param msg
	 * @param timeoutMinutes
	 * @return
	 * @throws IOException
	 */
	public static String doGet(String url, String msg, int timeoutMinutes) throws IOException {
		return doSend(url, msg, METHOD_TYPE_GET, CONTENT_TYPE_FORM, DEFAULT_CHARSET, timeoutMinutes);
	}

	/**
	 * 执行GET方式表单请求
	 * 
	 * @param url
	 * @param msg
	 * @param charset
	 * @param timeoutMinutes
	 * @return
	 * @throws IOException
	 */
	public static String doGet(String url, String msg, String charset, int timeoutMinutes) throws IOException {
		return doSend(url, msg, METHOD_TYPE_GET, CONTENT_TYPE_FORM, charset, timeoutMinutes);
	}

	/**
	 * 执行POST方式表单请求
	 * 
	 * @param url
	 * @param msg
	 * @return
	 * @throws IOException
	 */
	public static String doPost(String url, String msg) throws IOException {
		return doSend(url, msg, METHOD_TYPE_POST, CONTENT_TYPE_FORM, DEFAULT_CHARSET, DEFAULT_TIMEOUT_MINUTES);
	}

	/**
	 * 执行POST方式表单请求
	 * 
	 * @param url
	 * @param msg
	 * @param charset
	 * @return
	 * @throws IOException
	 */
	public static String doPost(String url, String msg, String charset) throws IOException {
		return doSend(url, msg, METHOD_TYPE_POST, CONTENT_TYPE_FORM, charset, DEFAULT_TIMEOUT_MINUTES);
	}

	/**
	 * 执行POST方式表单请求
	 * 
	 * @param url
	 * @param msg
	 * @param timeoutMinutes
	 * @return
	 * @throws IOException
	 */
	public static String doPost(String url, String msg, int timeoutMinutes) throws IOException {
		return doSend(url, msg, METHOD_TYPE_POST, CONTENT_TYPE_FORM, DEFAULT_CHARSET, timeoutMinutes);
	}

	/**
	 * 执行POST方式表单请求
	 * 
	 * @param url
	 * @param msg
	 * @param charset
	 * @param timeoutMinutes
	 * @return
	 * @throws IOException
	 */
	public static String doPost(String url, String msg, String charset, int timeoutMinutes) throws IOException {
		return doSend(url, msg, METHOD_TYPE_POST, CONTENT_TYPE_FORM, charset, timeoutMinutes);
	}

	/**
	 * 执行POST方式报文请求
	 * 
	 * @param url
	 * @param msg
	 * @return
	 * @throws IOException
	 */
	public static String doPostXml(String url, String msg) throws IOException {
		return doSend(url, msg, METHOD_TYPE_POST, CONTENT_TYPE_XML, DEFAULT_CHARSET, DEFAULT_TIMEOUT_MINUTES);
	}

	/**
	 * 执行POST方式报文请求
	 * 
	 * @param url
	 * @param msg
	 * @param charset
	 * @return
	 * @throws IOException
	 */
	public static String doPostXml(String url, String msg, String charset) throws IOException {
		return doSend(url, msg, METHOD_TYPE_POST, CONTENT_TYPE_XML, charset, DEFAULT_TIMEOUT_MINUTES);
	}

	/**
	 * 执行POST方式报文请求
	 * 
	 * @param url
	 * @param msg
	 * @param timeoutMinutes
	 * @return
	 * @throws IOException
	 */
	public static String doPostXml(String url, String msg, int timeoutMinutes) throws IOException {
		return doSend(url, msg, METHOD_TYPE_POST, CONTENT_TYPE_XML, DEFAULT_CHARSET, timeoutMinutes);
	}

	/**
	 * 执行POST方式报文请求
	 * 
	 * @param url
	 * @param msg
	 * @param charset
	 * @param timeoutMinutes
	 * @return
	 * @throws IOException
	 */
	public static String doPostXml(String url, String msg, String charset, int timeoutMinutes) throws IOException {
		return doSend(url, msg, METHOD_TYPE_POST, CONTENT_TYPE_XML, charset, timeoutMinutes);
	}

	/**
	 * 执行POST方式报文请求
	 * 
	 * @param url
	 * @param jsonStr
	 * @return
	 * @throws IOException
	 */
	public static String doPostJson(String url, String jsonStr) throws IOException {
		return doSend(url, jsonStr, METHOD_TYPE_POST, CONTENT_TYPE_JSON, DEFAULT_CHARSET, DEFAULT_TIMEOUT_MINUTES);
	}

	/**
	 * 执行POSt报文请求
	 * 
	 * @param url
	 * @param jsonStr
	 * @param charSet
	 * @return
	 * @throws IOException
	 */
	public static String doPostJson(String url, String jsonStr, String charSet) throws IOException {
		return doSend(url, jsonStr, METHOD_TYPE_POST, CONTENT_TYPE_JSON, charSet, DEFAULT_TIMEOUT_MINUTES);
	}

	/**
	 * 执行POSt报文请求
	 * 
	 * @param url
	 * @param jsonStr
	 * @param charSet
	 * @param timeoutMinutes
	 * @return
	 * @throws IOException
	 */
	public static String doPostJson(String url, String jsonStr, String charSet, int timeoutMinutes) throws IOException {
		return doSend(url, jsonStr, METHOD_TYPE_POST, CONTENT_TYPE_JSON, charSet, timeoutMinutes);
	}

	/**
	 * 发送请求并返回响应消息
	 * 
	 * @param url
	 * @param msg
	 * @param methodType
	 * @param contentType
	 * @param charset
	 * @param timeoutMinutes
	 * @return
	 * @throws IOException
	 */
	private static String doSend(String url, String msg, String methodType, String contentType, String charset, int timeoutMinutes) throws IOException {

		HttpURLConnection conn = null;
		try {
			if (METHOD_TYPE_GET.equalsIgnoreCase(methodType)) {
				url += '?' + msg;
			}

			conn = getConnection(url, methodType, contentType, charset, timeoutMinutes);

			if (METHOD_TYPE_POST.equalsIgnoreCase(methodType)) {
				write(conn, charset, msg);
			}

			return read(conn, charset);
		} finally {
			if (conn != null) {
				conn.disconnect();
			}
		}
	}

	/**
	 * 将消息读入
	 * 
	 * @param conn
	 * @param charset
	 * @return
	 * @throws IOException
	 */
	private static String read(HttpURLConnection conn, String charset) throws IOException {

		BufferedReader in = null;
		try {
			in = new BufferedReader(new InputStreamReader(conn.getInputStream(), charset));
			String t = null;
			StringBuilder sb = new StringBuilder();
			while ((t = in.readLine()) != null) {
				sb.append(t).append("\n");
			}
			return sb.toString().trim();
		} finally {
			if (in != null) {
				in.close();
			}
		}
	}

	/**
	 * 将消息写出
	 * 
	 * @param conn
	 * @param charset
	 * @param msg
	 * @throws IOException
	 */
	private static void write(HttpURLConnection conn, String charset, String msg) throws IOException {

		PrintWriter out = null;
		try {
			out = new PrintWriter(new OutputStreamWriter(conn.getOutputStream(), charset));
			out.print(msg);
		} finally {
			if (out != null) {
				out.close();
			}
		}
	}

	/**
	 * 创建一个http连接
	 * 
	 * @param url
	 * @param methodType
	 * @param contentType
	 * @param charset
	 * @param timeoutMinutes
	 * @return
	 * @throws IOException
	 */
	private static HttpURLConnection getConnection(String url, String methodType, String contentType, String charset, int timeoutMinutes) throws IOException {

		HttpURLConnection conn = (HttpURLConnection) new URL(url).openConnection();
		
		conn.setDoInput(true);
		conn.setDoOutput(true);
		conn.setUseCaches(false);
		int timeout = timeoutMinutes * 60 * 1000;
		conn.setConnectTimeout(timeout);
		conn.setReadTimeout(timeout);
		conn.setRequestMethod(methodType);
		conn.setRequestProperty("Content-Type", contentType + ";charset=" + charset);

		return conn;
	}
	
	/**
	 * 执行一个请求
	 * @param httpUriRequest
	 * @return
	 */
	public static StringBuffer  execute(HttpUriRequest httpUriRequest ){
		StringBuffer result = new StringBuffer();
		org.apache.http.client.HttpClient httpClient = (org.apache.http.client.HttpClient) new HttpClient();
		HttpResponse httpReponse=null ;
		 BufferedReader br=null;
		try {
			  httpReponse = httpClient.execute(httpUriRequest);
			  InputStream is = httpReponse.getEntity().getContent();
			  //读取数据流内容
			    br = new BufferedReader(new InputStreamReader(is));
			  String  line ="";
			  while((line = br.readLine() )!=null){
				  result.append(line);
			  }
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}finally {
			try {
				br.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		return result;
		
	}
}
