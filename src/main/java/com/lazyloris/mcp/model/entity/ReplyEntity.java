package com.lazyloris.mcp.model.entity;

import javax.persistence.*;

import com.lazyloris.model.impl.*;
import com.lazyloris.mcp.model.*;

@Entity(name = "Reply")
@Table(name = "T_MCP_REPLY")
@Access(AccessType.FIELD)
@Inheritance(strategy = InheritanceType.JOINED)
public class ReplyEntity extends AbstractEntity<Reply, Long> implements Reply {

    @Column(name = "CREATED")
    private java.util.Date created;

    @Column(name = "HIT")
    private int hit;

    @Column(name = "INDEX")
    private int index;

    @Column(name = "NAME")
    private String name;

    @Column(name = "KEYWORDMATCHTYPE")
    @Enumerated(EnumType.STRING)
    private com.lazyloris.mcp.model.KeywordMatchType keywordMatchType;

    @Column(name = "KEYWORDS")
    private String keywords;

    @Override
    @Id
    @Column(name = "REPLYID")
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "MCP_SEQUENCE")
    @TableGenerator(name = "MCP_SEQUENCE", table = "T_MCP_SEQUENCE", pkColumnName = "T", pkColumnValue = "REPLY", valueColumnName = "SEQ", allocationSize = 1000, initialValue = 1)
    @Access(AccessType.PROPERTY)
    public Long getId() {
        return super.getId();
    }

    @Override
    public java.util.Date getCreated() {
        return this.created;
    }
    
    @Override
    public void setCreated(java.util.Date created) {
        this.created = created;
    }
    
    @Override
    public int getHit() {
        return this.hit;
    }
    
    @Override
    public void setHit(int hit) {
        this.hit = hit;
    }
    
    @Override
    public int getIndex() {
        return this.index;
    }
    
    @Override
    public void setIndex(int index) {
        this.index = index;
    }
    
    @Override
    public String getName() {
        return this.name;
    }
    
    @Override
    public void setName(String name) {
        this.name = name;
    }
    
    @Override
    public com.lazyloris.mcp.model.KeywordMatchType getKeywordMatchType() {
        return this.keywordMatchType;
    }
    
    @Override
    public void setKeywordMatchType(com.lazyloris.mcp.model.KeywordMatchType keywordMatchType) {
        this.keywordMatchType = keywordMatchType;
    }
    
    @Override
    public String getKeywords() {
        return this.keywords;
    }
    
    @Override
    public void setKeywords(String keywords) {
        this.keywords = keywords;
    }
    
}