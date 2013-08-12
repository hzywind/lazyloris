package ${interface.package}.hibernate;

<#if !interface.parent??>
import com.lazyloris.model.impl.*;
</#if>
import ${interface.package}.*;

<#if interface.parent??>
public class ${interface.name}Impl extends ${interface.parent.name}Impl implements ${interface.name} {
<#else>
public class ${interface.name}Impl extends AbstractEntity<${interface.name}, Long> implements ${interface.name} {
</#if>

<#list interface.attributes as attribute>
    <#if attribute.type.dataType = "JAVA">
        <#if attribute.type.name = "Date">
    private java.util.Date ${attribute.name}<#if attribute.default?? && attribute.default = "now"> = new java.util.Date()</#if>;

        <#else>
    private ${attribute.type.name} ${attribute.name};

        </#if>
    <#elseif attribute.type.dataType = "PRIMITIVE">
    private ${attribute.type.name} ${attribute.name};

    <#elseif attribute.type.dataType = "META">
    private com.lazyloris.model.Meta ${attribute.name} = new com.lazyloris.model.impl.MetaImpl();

    <#elseif attribute.type.dataType = "ENUM">
    private ${attribute.type.name} ${attribute.name}<#if attribute.default??> = ${attribute.type.name}.${attribute.default}</#if>;

    </#if>
</#list>
<#list interface.associations as association>
    <#if association.type = "MANY" || association.type = "COMPOSITE">
    private java.util.List<${association.interface.name}> ${utils.getAssociationName(association)} = new java.util.ArrayList<${association.interface.name}>();

    <#elseif association.type = "SINGLE">
        <#if association.end?? && (association.end.type = "MANY" || association.end.type = "COMPOSITE")>
    private ${association.interface.name} ${utils.getAssociationName(association)};

        <#else>
        </#if>
    </#if>
</#list>

<#list interface.attributes as attribute>
    @Override
    <#if attribute.type.dataType = "JAVA">
        <#if attribute.type.name = "Date">
    public java.util.Date get${attribute.name?cap_first}() {
        <#else>
    public ${attribute.type.name} get${attribute.name?cap_first}() {
        </#if>
    <#elseif attribute.type.dataType = "PRIMITIVE">
        <#if attribute.type.name = "boolean">
    public boolean is${attribute.name?cap_first}() {
        <#else>
    public ${attribute.type.name} get${attribute.name?cap_first}() {
        </#if>
    <#elseif attribute.type.dataType = "META">
    public com.lazyloris.model.Meta get${attribute.name?cap_first}() {
    <#elseif attribute.type.dataType = "ENUM">
    public ${attribute.type.name} get${attribute.name?cap_first}() {
    </#if>
        return this.${attribute.name};
    }
    
    <#if !attribute.readOnly && attribute.type.dataType != "META">
    @Override
    public void set${attribute.name?cap_first}(<#if attribute.type.name = "Date">java.util.Date<#else>${attribute.type.name}</#if> ${attribute.name}) {
        this.${attribute.name} = ${attribute.name};
    }
    
    </#if>
</#list>
<#macro createChildren i a>
    <#list utils.getChildren(i, module) as child>
    public ${child.name} create${child.name}() {
        ${child.name} ${child.name?uncap_first} = new ${child.name}Impl();
        ${utils.getAssociationName(a)}.add(${child.name?uncap_first});
        <#if a.end?? && a.end.type = "SINGLE">
        ${child.name?uncap_first}.set${utils.getAssociationName(a.end)?cap_first}(this);
        </#if>
        return ${child.name?uncap_first};
    }
    
    <@createChildren i=child a=a/>
    </#list>
</#macro>
<#list interface.associations as association>
    <#if association.type = "MANY" || association.type = "COMPOSITE">
    @Override
    public java.util.List<${association.interface.name}> get${utils.getAssociationName(association)?cap_first}() {
        return this.${utils.getAssociationName(association)};
    }
    
        <#if association.type = "COMPOSITE">
    public ${association.interface.name} create${association.interface.name}() {
        ${association.interface.name} ${association.interface.name?uncap_first} = new ${association.interface.name}Impl();
        ${utils.getAssociationName(association)}.add(${association.interface.name?uncap_first});
        <#if association.end?? && association.end.type = "SINGLE">
        ${association.interface.name?uncap_first}.set${utils.getAssociationName(association.end)?cap_first}(this);
        </#if>
        return ${association.interface.name?uncap_first};
    }
    
    <@createChildren i=association.interface a=association/>
        </#if>
    <#elseif association.type = "SINGLE" && association.end??>
    @Override
    public ${association.interface.name} get${utils.getAssociationName(association)?cap_first}() {
        return this.${utils.getAssociationName(association)};
    }
    
    @Override
    public void set${utils.getAssociationName(association)?cap_first}(${association.interface.name} ${utils.getAssociationName(association)}) {
        this.${utils.getAssociationName(association)} = ${utils.getAssociationName(association)};
    }
    </#if>
</#list>
}