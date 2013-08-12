package ${interface.package}.np;

<#if !interface.parent??>
import com.lazyloris.model.NP;
</#if>
import ${interface.package}.*;

<#if interface.parent??>
public class ${interface.name}NP extends ${interface.parent.name}NP {
<#else>
public class ${interface.name}NP implements NP<${interface.name}> {
    private long id;
</#if>
    
<#list interface.attributes as attribute>
    <#if attribute.type.dataType = "JAVA">
        <#if attribute.type.name = "Date">
    private java.util.Date ${attribute.name};

        <#else>
    private ${attribute.type.name} ${attribute.name};

        </#if>
    <#elseif attribute.type.dataType = "PRIMITIVE">
    private ${attribute.type.name} ${attribute.name};

    <#elseif attribute.type.dataType = "META">
    private com.lazyloris.model.Meta ${attribute.name};

    <#elseif attribute.type.dataType = "ENUM">
    private ${attribute.type.name} ${attribute.name}<#if attribute.default??> = ${attribute.type.name}.${attribute.default}</#if>;

    </#if>
</#list>
<#list interface.associations as association>
    <#if association.type = "MANY" || association.type = "COMPOSITE">
    private java.util.List<${association.interface.name}NP> ${utils.getAssociationName(association)} = new java.util.ArrayList<${association.interface.name}NP>();

    <#elseif association.type = "SINGLE">
        <#if association.end?? && (association.end.type = "MANY" || association.end.type = "COMPOSITE")>
    private ${association.interface.name}NP ${utils.getAssociationName(association)};

        <#else>
        </#if>
    </#if>
</#list>
<#if !interface.parent??>
    public Long getId() {
        return this.id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }

</#if>
<#list interface.attributes as attribute>
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
    public void set${attribute.name?cap_first}(<#if attribute.type.name = "Date">java.util.Date<#else>${attribute.type.name}</#if> ${attribute.name}) {
        this.${attribute.name} = ${attribute.name};
    }
    
    </#if>
</#list>
<#list interface.associations as association>
    <#if association.type = "MANY" || association.type = "COMPOSITE">
    public java.util.List<${association.interface.name}NP> get${utils.getAssociationName(association)?cap_first}() {
        return this.${utils.getAssociationName(association)};
    }

    <#elseif association.type = "SINGLE" && association.end??>
    public ${association.interface.name}NP get${utils.getAssociationName(association)?cap_first}() {
        return this.${utils.getAssociationName(association)};
    }
    
    public void set${utils.getAssociationName(association)?cap_first}(${association.interface.name}NP ${utils.getAssociationName(association)}) {
        this.${utils.getAssociationName(association)} = ${utils.getAssociationName(association)};
    }
    </#if>

</#list>
<#if !interface.parent??>
    @Override
</#if>
    public void copy(${interface.name} t) {
<#if interface.parent??>
        super.copy(t);
</#if>
<#list interface.attributes as attribute>
        setId(t.getId());
    <#if attribute.type.dataType = "META">
        this.${attribute.name} = t.get${attribute.name?cap_first}().copy();
    <#else>
        set${attribute.name?cap_first}(t.<#if attribute.type.name = "boolean">is<#else>get</#if>${attribute.name?cap_first}());
    </#if>
</#list>
    }
}