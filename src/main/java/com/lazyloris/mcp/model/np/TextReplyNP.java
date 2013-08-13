package com.lazyloris.mcp.model.np;

import com.lazyloris.mcp.model.*;

public class TextReplyNP extends ReplyNP {
    
    private String textContent;

    public String getTextContent() {
        return this.textContent;
    }
    
    public void setTextContent(String textContent) {
        this.textContent = textContent;
    }
    
    public void copy(TextReply t) {
        super.copy(t);
        setId(t.getId());
        setTextContent(t.getTextContent());
    }
}