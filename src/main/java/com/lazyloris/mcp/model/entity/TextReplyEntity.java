package com.lazyloris.mcp.model.entity;

import javax.persistence.*;

import com.lazyloris.mcp.model.*;

@Entity(name = "TextReply")
@Table(name = "T_MCP_TEXTREPLY")
@Access(AccessType.FIELD)
public class TextReplyEntity extends ReplyEntity implements TextReply {

    @Column(name = "TEXTCONTENT")
    private String textContent;

    @Override
    public String getTextContent() {
        return this.textContent;
    }
    
    @Override
    public void setTextContent(String textContent) {
        this.textContent = textContent;
    }
    
}