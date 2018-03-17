package com.langchao.bigdata.util;

public class IndexInfo {
    String indexName;
    String indexType;
    String mappingStr;
    
    public String getIndexName() {
        return indexName;
    }
    
    public void setIndexName(String indexName) {
        this.indexName = indexName;
    }
    
    public String getIndexType() {
        return indexType;
    }
    
    public void setIndexType(String indexType) {
        this.indexType = indexType;
    }
    
    public String getMappingStr() {
        return mappingStr;
    }
    
    public void setMappingStr(String mappingStr) {
        this.mappingStr = mappingStr;
    }
}
