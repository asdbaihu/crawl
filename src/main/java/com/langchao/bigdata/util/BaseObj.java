package com.langchao.bigdata.util;

public class BaseObj {
    protected String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public BaseObj() {
        super();
    }

    public BaseObj(String id) {
        super();
        this.id = id;
    }
    
}
