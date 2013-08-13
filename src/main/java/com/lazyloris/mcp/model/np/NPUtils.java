package com.lazyloris.mcp.model.np;

import java.util.ArrayList;
import java.util.List;

import com.lazyloris.mcp.model.*;

public class NPUtils {
    
    public static ReplyNP packageReply(Reply reply) {
        ReplyNP npReply = new ReplyNP();
        npReply.copy(reply);
        return npReply;
    }
    
    public static List<ReplyNP> packageReplyList(List<Reply> collection) {
        List<ReplyNP> npReplyList = new ArrayList<ReplyNP>();
        for (Reply obj : collection) {
            npReplyList.add(packageReply(obj));
        }
        return npReplyList;
    }
    
    public static TextReplyNP packageTextReply(TextReply textReply) {
        TextReplyNP npTextReply = new TextReplyNP();
        npTextReply.copy(textReply);
        return npTextReply;
    }
    
    public static List<TextReplyNP> packageTextReplyList(List<TextReply> collection) {
        List<TextReplyNP> npTextReplyList = new ArrayList<TextReplyNP>();
        for (TextReply obj : collection) {
            npTextReplyList.add(packageTextReply(obj));
        }
        return npTextReplyList;
    }
    
}
