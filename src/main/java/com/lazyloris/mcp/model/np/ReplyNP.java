package com.lazyloris.mcp.model.np;

import com.lazyloris.model.NP;
import com.lazyloris.mcp.model.*;

public class ReplyNP implements NP<Reply> {
    private long id;
    
    private java.util.Date created;

    private int hit;

    private int index;

    private String name;

    private com.lazyloris.mcp.model.KeywordMatchType keywordMatchType;

    private String keywords;

    public Long getId() {
        return this.id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }

    public java.util.Date getCreated() {
        return this.created;
    }
    
    public void setCreated(java.util.Date created) {
        this.created = created;
    }
    
    public int getHit() {
        return this.hit;
    }
    
    public void setHit(int hit) {
        this.hit = hit;
    }
    
    public int getIndex() {
        return this.index;
    }
    
    public void setIndex(int index) {
        this.index = index;
    }
    
    public String getName() {
        return this.name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public com.lazyloris.mcp.model.KeywordMatchType getKeywordMatchType() {
        return this.keywordMatchType;
    }
    
    public void setKeywordMatchType(com.lazyloris.mcp.model.KeywordMatchType keywordMatchType) {
        this.keywordMatchType = keywordMatchType;
    }
    
    public String getKeywords() {
        return this.keywords;
    }
    
    public void setKeywords(String keywords) {
        this.keywords = keywords;
    }
    
    @Override
    public void copy(Reply t) {
        setId(t.getId());
        setCreated(t.getCreated());
        setId(t.getId());
        setHit(t.getHit());
        setId(t.getId());
        setIndex(t.getIndex());
        setId(t.getId());
        setName(t.getName());
        setId(t.getId());
        setKeywordMatchType(t.getKeywordMatchType());
        setId(t.getId());
        setKeywords(t.getKeywords());
    }
}