package com.lazyloris.mcp.model.repository.impl;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;

import com.lazyloris.model.impl.AbstractRepository;

import com.lazyloris.mcp.model.*;
import com.lazyloris.mcp.model.entity.*;
import com.lazyloris.mcp.model.repository.Repository;

public class RepositoryImpl extends AbstractRepository implements Repository {
    
    public Reply createReply() {
        return new ReplyEntity();
    }
    
    public List<Reply> findAllReply() {
        CriteriaBuilder cb = getCriteriaBuilder();
        CriteriaQuery<ReplyEntity> cq = (CriteriaQuery<ReplyEntity>) cb.createQuery(ReplyEntity.class);
        cq.from(ReplyEntity.class);
        List<Reply> result = new ArrayList<Reply>();
        for (ReplyEntity entity : getEntityManager().createQuery(cq).getResultList()) {
            result.add(entity);
        }
        return result;
    }
    
    public Reply loadReply(Long id) {
        return (Reply) getEntityManager().find(ReplyEntity.class, id);
    }
    
    public TextReply createTextReply() {
        return new TextReplyEntity();
    }
    
    public List<TextReply> findAllTextReply() {
        CriteriaBuilder cb = getCriteriaBuilder();
        CriteriaQuery<TextReplyEntity> cq = (CriteriaQuery<TextReplyEntity>) cb.createQuery(TextReplyEntity.class);
        cq.from(TextReplyEntity.class);
        List<TextReply> result = new ArrayList<TextReply>();
        for (TextReplyEntity entity : getEntityManager().createQuery(cq).getResultList()) {
            result.add(entity);
        }
        return result;
    }
    
    public TextReply loadTextReply(Long id) {
        return (TextReply) getEntityManager().find(TextReplyEntity.class, id);
    }
    
}
