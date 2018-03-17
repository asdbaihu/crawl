package com.langchao.bigdata.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;

import org.seleniumhq.jetty7.util.log.Log;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 下载接口
 * @author yuenbin
 *
 */
public class DownLoader {
	
	private static final Logger log = LoggerFactory.getLogger(DownLoader.class);

    public static void download(String urlString, String filename,String savePath) throws Exception {
        // 构造URL
        URL url = new URL(urlString);
        // 打开连接
        URLConnection con = url.openConnection();
        //设置请求超时为5s
        con.setConnectTimeout(5*1000);
        // 输入流
        InputStream is = con.getInputStream();

        // 1K的数据缓冲
        byte[] bs = new byte[1024];
        // 读取到的数据长度
        int len;
        // 输出的文件流
        File sf=new File(savePath);
        if(!sf.exists()){
            sf.mkdirs();
        }
//        OutputStream os = new FileOutputStream(sf.getPath()+"\\"+filename);
        ObjectOutputStream os = new ObjectOutputStream(new FileOutputStream(new File(sf.getPath()+"\\"+filename)));
        long startTime = System.currentTimeMillis();
        log.info("开始下载图片....");
        // 开始读取
        while ((len = is.read(bs)) != -1) {
            os.write(bs, 0, len);
        }
        long endTime = System.currentTimeMillis();
        log.info("图片下载完毕,耗时:"+(endTime-startTime)/1000+"S");
        // 完毕，关闭所有链接
        os.close();
        is.close();
    }
    
    public static void main(String[] args) {
		try {
//			String searchImgReg = "(?x)(src|SRC|background|BACKGROUND)=('|\")/?(([\\w-]+/)*([\\w-]+\\.(jpg|JPG|png|PNG|gif|GIF)))('|\")";
//			String searchImgReg2 = "(?x)(src|SRC|background|BACKGROUND)=('|\")(http://([\\w-]+\\.)+[\\w-]+(:[0-9]+)*(/[\\w-]+)*(/[\\w-]+\\.(jpg|JPG|png|PNG|gif|GIF)))('|\")";

			DownLoader.download("http://t2.hddhhn.com/uploads/tu/201803/9999/e8ead53bf3.jpg", "aa", "F:\\image");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}