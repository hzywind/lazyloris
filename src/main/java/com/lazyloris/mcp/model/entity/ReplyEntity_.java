package com.lazyloris.mcp.model.entity;

import javax.persistence.metamodel.*;

@StaticMetamodel(ReplyEntity.class)
public class ReplyEntity_ {
    public static volatile SingularAttribute<ReplyEntity, Long> id;
    public static volatile SingularAttribute<ReplyEntity, java.util.Date> created;
    public static volatile SingularAttribute<ReplyEntity, Integer> hit;
    public static volatile SingularAttribute<ReplyEntity, Integer> index;
    public static volatile SingularAttribute<ReplyEntity, String> name;
    public static volatile SingularAttribute<ReplyEntity, com.lazyloris.mcp.model.KeywordMatchType> keywordMatchType;
    public static volatile SingularAttribute<ReplyEntity, String> keywords;
}