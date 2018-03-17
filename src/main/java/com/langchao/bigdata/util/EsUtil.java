package com.langchao.bigdata.util;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.List;
import org.elasticsearch.action.bulk.BulkItemResponse;
import org.elasticsearch.action.bulk.BulkRequestBuilder;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class EsUtil {
    // es服务器的host
    //private static final String host = "192.168.11.7";
    private static final String host = "192.168.14.8";
    // es服务器暴露给client的port
    private static final int port = 9300;
    // jackson用于序列化操作的mapper
    private static final ObjectMapper mapper = new ObjectMapper();
    
    /**
     * 获得连接
     * 
     */
    private static Client getClient(String host, int port) throws UnknownHostException {
        Client client = TransportClient.builder()
                .build()
                .addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName(host), port));
        return client;
    }
    
public static void insertDocFromWWW( String url,String pageKey ,String title ,String cTime,String sInfo){
    // 添加到ES索引中
    IndexInfo ib = new IndexInfo();
    ib.setIndexName("news_index");
    ib.setIndexType("news");
    
    News n = new News();
    n.setId(pageKey);
    if ("[]".equals(title)) {
        title = url;
    }
    n.setTitle(title);
    n.setcTime(cTime);
    n.setsInfo(sInfo);
    
    // jackson用于序列化操作的mapper
    try {
        ObjectMapper mapper = new ObjectMapper();
        byte[] json = mapper.writeValueAsBytes(n);
        EsUtil.addDocument(ib, n.getId(), json);
    }
    catch (Exception e) {
        e.printStackTrace();
    }
}
    

    /**
     * 查询
     * 
     * @param ib 查询的索引信息
     * @param colName 查询的列
     * @param qryStr 查询内容
     * @return SearchHit[] 查询结果
     * @throws Exception
     */
    public static SearchHit[] search(IndexInfo ib, String colName, String qryStr) throws Exception {
        Client client = getClient(host, port);
        QueryBuilder qb = new BoolQueryBuilder().must(QueryBuilders.matchQuery(colName, qryStr));
        
        SearchResponse response = client.prepareSearch(ib.getIndexName()).setTypes(ib.getIndexType()).setQuery(qb).execute().actionGet();
        
        SearchHit[] hits = response.getHits().getHits();
        client.close();
        return hits;
    }
    /**
     * 新增document
     * 
     * @param ib 查询的索引信息
     * @param id 插入数据的ID
     * @param json 插入数据信息
     * @throws UnknownHostException
     * @throws JsonProcessingException
     */
    public static void addDocument(IndexInfo ib, String id, byte[] json) throws UnknownHostException, JsonProcessingException {
        Client client = getClient(host, port);
        client.prepareIndex(ib.getIndexName(), ib.getIndexType(), id + "").setSource(json).get();
        client.close();
    }
    /**
     * 创建索引&&添加数据。先查询是否存在，如已有索引则先删除索引
     * 
     * @param indexbean 创建index的基础信息
     * @param itemList 索引内容列表
     * @throws Exception
     */
    public static void createIndex(IndexInfo indexbean, List<BaseObj> itemList) throws Exception {
        Client client = getClient(host, port);
        // 如果存在就先删除索引
        if (client.admin().indices().prepareExists(indexbean.getIndexName()).get().isExists()) {
            client.admin().indices().prepareDelete(indexbean.getIndexName()).get();
        }
        // 创建索引,并设置mapping.
        String mappingStr = indexbean.getMappingStr(); // "{ \"goods\" : { \"properties\": { \"id\": { \"type\": \"long\" }, \"name\": {\"type\": \"string\", \"analyzer\": \"ik_max_word\"}, \"regionIds\": {\"type\": \"string\",\"index\": \"not_analyzed\"}}}}";
        client.admin().indices().prepareCreate(indexbean.getIndexName()).addMapping(indexbean.getIndexType(), mappingStr).get();
        
        // 批量处理request
        BulkRequestBuilder bulkRequest = client.prepareBulk();
        
        byte[] json;
        for (BaseObj item : itemList) {
            json = mapper.writeValueAsBytes(item);
            bulkRequest.add(new IndexRequest(indexbean.getIndexName(), indexbean.getIndexType(), item.getId() + "").source(json));
        }
        
        // 执行批量处理request
        BulkResponse bulkResponse = bulkRequest.get();
        
        // 处理错误信息
        if (bulkResponse.hasFailures()) {
            System.out.println("====================批量创建索引过程中出现错误 下面是错误信息==========================");
            long count = 0L;
            for (BulkItemResponse bulkItemResponse : bulkResponse) {
                System.out.println("发生错误的 索引id为 : " + bulkItemResponse.getId() + " ，错误信息为：" + bulkItemResponse.getFailureMessage());
                count++;
            }
            System.out.println("====================批量创建索引过程中出现错误 上面是错误信息 共有: " + count + " 条记录==========================");
        }
        client.close();
    }
}
