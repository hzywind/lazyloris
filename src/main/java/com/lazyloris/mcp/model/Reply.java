package com.lazyloris.mcp.model;

import com.lazyloris.model.Entity;

public interface Reply extends Entity<Reply, Long> {

    java.util.Date getCreated();
    
    void setCreated(java.util.Date created);
    
    int getHit();
    
    void setHit(int hit);
    
    int getIndex();
    
    void setIndex(int index);
    
    String getName();
    
    void setName(String name);
    
    com.lazyloris.mcp.model.KeywordMatchType getKeywordMatchType();
    
    void setKeywordMatchType(com.lazyloris.mcp.model.KeywordMatchType keywordMatchType);
    
    String getKeywords();
    
    void setKeywords(String keywords);
    
}