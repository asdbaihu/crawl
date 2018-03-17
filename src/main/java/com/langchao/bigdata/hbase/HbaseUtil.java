package com.langchao.bigdata.hbase;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.HColumnDescriptor;
import org.apache.hadoop.hbase.HTableDescriptor;
import org.apache.hadoop.hbase.KeyValue;
import org.apache.hadoop.hbase.MasterNotRunningException;
import org.apache.hadoop.hbase.ZooKeeperConnectionException;
import org.apache.hadoop.hbase.client.Delete;
import org.apache.hadoop.hbase.client.Get;
import org.apache.hadoop.hbase.client.HBaseAdmin;
import org.apache.hadoop.hbase.client.HTable;
import org.apache.hadoop.hbase.client.HTableInterface;
import org.apache.hadoop.hbase.client.HTablePool;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.ResultScanner;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.filter.Filter;
import org.apache.hadoop.hbase.filter.FilterList;
import org.apache.hadoop.hbase.filter.SingleColumnValueFilter;
import org.apache.hadoop.hbase.filter.CompareFilter.CompareOp;
import org.apache.hadoop.hbase.util.Bytes;

public class HbaseUtil {
    public static Configuration configuration;
    public static int num = 0;
    static {
        configuration = HBaseConfiguration.create();
        configuration.set("hbase.zookeeper.property.clientPort", "2181");
        configuration.set("hbase.zookeeper.quorum", "192.168.1.102"); // 多个主机,分割
        configuration.set("hbase.master", "192.168.1.101:600000");
    }
    
    /**
     * 创建表
     * 
     * @param tableName 表名
     * @param columList 列名list
     */
    public static void createTable(String tableName, List<String> columList) {
        System.out.println("start create table ......" + tableName);
        try {
            HBaseAdmin hBaseAdmin = new HBaseAdmin(configuration);
            if (hBaseAdmin.tableExists(tableName)) {// 如果存在要创建的表，那么先删除，再创建
                hBaseAdmin.disableTable(tableName);
                hBaseAdmin.deleteTable(tableName);
                System.out.println(tableName + " is exist,detele....");
            }
            // 创建表对象
            HTableDescriptor tableDescriptor = new HTableDescriptor(tableName);
            for (String c : columList) {
                // 添加列
                tableDescriptor.addFamily(new HColumnDescriptor(c));
            }
            // 创建表
            hBaseAdmin.createTable(tableDescriptor);
        }
        catch (MasterNotRunningException e) {
            e.printStackTrace();
        }
        catch (ZooKeeperConnectionException e) {
            e.printStackTrace();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("end create table ......");
    }
    
    /**
     * 插入一行数据
     * 
     * @param tableName 表名
     * @param rowKey 行的rowKey
     * @param columns 列簇list{colName,colValue}
     */
    public static void insertData(String tableName, String rowKey, List<Map> columns) {
        System.out.println("start insert data ......");
        HTablePool pool = new HTablePool(configuration, 1000);
        // 如今应用的api版本中pool.getTable返回的类型是HTableInterface ，无法强转为HTable
        // HTable table = (HTable)pool.getTable(tableName);
        HTableInterface table = pool.getTable(tableName);
        // 一个PUT代表一行数据，再NEW一个PUT表示第二行数据,每行一个唯一的ROWKEY，此处rowkey为put构造方法中传入的值
        Put put = new Put(rowKey.getBytes());
        for (Map m : columns) {
            // m.put("url", url);
            // m.put("title", title);
            // m.put("cTime", cTime);
            // m.put("sInfo", sInfo);
            Iterator it = m.keySet().iterator();
            List<NameValuePair> lnv = new ArrayList<NameValuePair>();
            while (it.hasNext()) {
                String key = it.next() + "";
                Object o = m.get(key);
                put.add(key.toString().getBytes(), null, o.toString().getBytes());
            }
        }
        try {
            table.put(put);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("end insert data ......");
    }
    
    /**
     * 查询表中数据
     * 
     * @param tableName
     */
    public static void QueryAll(String tableName) {
        HTablePool pool = new HTablePool(configuration, 1000);
        // 如今应用的api版本中pool.getTable返回的类型是HTableInterface ，无法强转为HTable
        // HTable table = (HTable)pool.getTable(tableName);
        HTableInterface table = pool.getTable(tableName);
        try {
            ResultScanner rs = table.getScanner(new Scan());
            for (Result r : rs) {
                num++;
                System.out.println("rowkey==值:" + new String(r.getRow()));
                for (KeyValue keyValue : r.raw()) {
                    String colName = new String(keyValue.getFamily());
                    if (!"sInfo".equals(colName)) { // / 测试时不要打印 sInfo
                        System.out.println("列：" + colName + "==值:" + new String(keyValue.getValue()));
                    }
                }
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("本次查询得到数据：" + num + "条...");
    }
    
    /**
     * 根据rowKey获取一行数据
     * 
     * @param tableName
     * @param rowKey
     */
    public static void QueryByRowKey(String tableName, String rowKey) {
        
        HTablePool pool = new HTablePool(configuration, 1000);
        // 如今应用的api版本中pool.getTable返回的类型是HTableInterface ，无法强转为HTable
        // HTable table = (HTable)pool.getTable(tableName);
        HTableInterface table = pool.getTable(tableName);
        try {
            Get scan = new Get(rowKey.getBytes());// 根据rowkey查询
            Result r = table.get(scan);
            System.out.println("QueryByRowKey获得到rowkey:" + new String(r.getRow()));
            for (KeyValue keyValue : r.raw()) {
                System.out.println("列：" + new String(keyValue.getFamily()) + "==值:" + new String(keyValue.getValue()));
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    /**
     * 根据列值查询数据
     * 
     * @param tableName
     * @param columns
     */
    public static void QueryByColumns(String tableName, List<Map> columns) {
        
        try {
            HTablePool pool = new HTablePool(configuration, 1000);
            // 如今应用的api版本中pool.getTable返回的类型是HTableInterface ，无法强转为HTable
            // HTable table = (HTable)pool.getTable(tableName);
            HTableInterface table = pool.getTable(tableName);
            
            List<Filter> filters = new ArrayList<Filter>();
            
            for (Map m : columns) {
                Filter filter1 = new SingleColumnValueFilter(m.get("colName").toString().getBytes(), null, CompareOp.EQUAL,
                        m.get("colValue").toString().getBytes());
                filters.add(filter1);
            }
            
            FilterList filterList1 = new FilterList(filters);
            
            Scan scan = new Scan();
            scan.setFilter(filterList1);
            ResultScanner rs = table.getScanner(scan);
            for (Result r : rs) {
                System.out.println("QueryByCondition3获得到rowkey:" + new String(r.getRow()));
                for (KeyValue keyValue : r.raw()) {
                    System.out.println("列：" + new String(keyValue.getFamily()) + "==值:" + new String(keyValue.getValue()));
                }
            }
            rs.close();
            
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    /**
     * 根据rowKey删除一行数据
     * 
     * @param tablename 表名
     * @param rowKey 行的rowKey
     */
    public static void deleteRow(String tablename, String rowKey) {
        try {
            HTable table = new HTable(configuration, tablename);
            List list = new ArrayList();
            Delete d1 = new Delete(rowKey.getBytes());
            list.add(d1);
            
            table.delete(list);
            System.out.println("删除行成功!");
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    /**
     * 删除表
     * 
     * @param tableName 表名
     */
    public static void dropTable(String tableName) {
        try {
            System.out.println("start drop table ......");
            HBaseAdmin admin = new HBaseAdmin(configuration);
            admin.disableTable(tableName);
            admin.deleteTable(tableName);
            System.out.println("end drop table ......");
        }
        catch (MasterNotRunningException e) {
            e.printStackTrace();
        }
        catch (ZooKeeperConnectionException e) {
            e.printStackTrace();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
    
}
