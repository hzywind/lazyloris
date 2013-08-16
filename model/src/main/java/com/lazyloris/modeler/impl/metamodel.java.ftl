package ${interface.package}.entity;

import javax.persistence.metamodel.*;

@StaticMetamodel(${interface.name}Entity.class)
public class ${interface.name}Entity_ <#if interface.parent??>extends ${interface.parent.name}Entity_ </#if>{
 <#if interface.parent??>
 <#else>
    public static volatile SingularAttribute<${interface.name}Entity, Long> id;
 </#if>
 <#list interface.attributes as attribute>
    <#if attribute.type.dataType = "JAVA">
        <#if attribute.type.name = "Date">
    public static volatile SingularAttribute<${interface.name}Entity, java.util.Date> ${attribute.name};
        <#else>
    public static volatile SingularAttribute<${interface.name}Entity, ${attribute.type.name}> ${attribute.name};
        </#if>
    <#elseif attribute.type.dataType = "PRIMITIVE">
        <#if attribute.type.name = "int">
    public static volatile SingularAttribute<${interface.name}Entity, Integer> ${attribute.name};
        <#else>
    public static volatile SingularAttribute<${interface.name}Entity, ${attribute.type.name?cap_first}> ${attribute.name};
        </#if>
    <#elseif attribute.type.dataType = "META">
    public static volatile SingularAttribute<${interface.name}Entity, com.lazyloris.model.Meta> ${attribute.name};
    <#elseif attribute.type.dataType = "ENUM">
    public static volatile SingularAttribute<${interface.name}Entity, ${attribute.type.name}> ${attribute.name};
    </#if>
 </#list>
 <#list interface.associations as association>
    <#if association.type = "MANY" || association.type = "COMPOSITE">
    public static volatile ListAttribute<${interface.name}Entity, ${association.interface.name}Entity> ${utils.getAssociationName(association)};
    <#elseif association.type = "SINGLE" && association.end??>
    public static volatile SingularAttribute<${interface.name}Entity, ${association.interface.name}Entity> ${utils.getAssociationName(association)};
    <#else>
    /**Unidirectional many to one**/
    public static volatile SingularAttribute<${interface.name}Entity, ${association.interface.name}Entity> ${utils.getAssociationName(association)};
    </#if>
 </#list>
}