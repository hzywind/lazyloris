package ${interface.package};

<#if interface.parent??>
public interface ${interface.name} extends ${interface.parent.name} {
<#else>
import com.lazyloris.model.Entity;

public interface ${interface.name} extends Entity<${interface.name}, Long> {
</#if>

 <#list interface.attributes as attribute>
    <#if attribute.type.dataType = "JAVA">
        <#if attribute.type.name = "Date">
    java.util.Date get${attribute.name?cap_first}();
        <#else>
    ${attribute.type.name} get${attribute.name?cap_first}();
        </#if>
    <#elseif attribute.type.dataType = "PRIMITIVE">
        <#if attribute.type.name = "boolean">
    boolean is${attribute.name?cap_first}();
        <#else>
    ${attribute.type.name} get${attribute.name?cap_first}();
        </#if>
    <#elseif attribute.type.dataType = "META">
    com.lazyloris.model.Meta get${attribute.name?cap_first}();
    <#elseif attribute.type.dataType = "ENUM">
    ${attribute.type.name} get${attribute.name?cap_first}();
    </#if>
    
    <#if !attribute.readOnly && attribute.type.dataType != "META">
    void set${attribute.name?cap_first}(<#if attribute.type.name = "Date">java.util.Date<#else>${attribute.type.name}</#if> ${attribute.name});
    
    </#if>
 </#list>
 <#macro createChildren i>
    <#list utils.getChildren(i, module) as child>
    ${child.name} create${child.name}();
    
    <@createChildren i=child/>
    </#list>
 </#macro>
 <#list interface.associations as association>
    <#if association.type = "MANY" || association.type = "COMPOSITE">
    java.util.List<${association.interface.name}> get${utils.getAssociationName(association)?cap_first}();
    
        <#if association.type = "COMPOSITE">
    ${association.interface.name} create${association.interface.name}();
    
    <@createChildren i=association.interface/>
        </#if>
    
    <#elseif association.type = "SINGLE" && association.end??>
    ${association.interface.name} get${utils.getAssociationName(association)?cap_first}();
    
    void set${utils.getAssociationName(association)?cap_first}(${association.interface.name} ${utils.getAssociationName(association)});
    <#else>
    /**Unidirectional many to one**/
    ${association.interface.name} get${utils.getAssociationName(association)?cap_first}();
    
    void set${utils.getAssociationName(association)?cap_first}(${association.interface.name} ${utils.getAssociationName(association)});
    </#if>
 </#list>
}