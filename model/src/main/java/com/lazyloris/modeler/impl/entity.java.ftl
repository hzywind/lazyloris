package ${interface.package}.entity;

import javax.persistence.*;

<#if !interface.parent??>
import com.lazyloris.model.impl.*;
</#if>
import ${interface.package}.*;

@Entity(name = "${interface.name}")
@Table(name = "T_${model.name}_${interface.name?upper_case}")
@Access(AccessType.FIELD)
<#if utils.hasChildrenInterfaces(interface, module)>
@Inheritance(strategy = InheritanceType.JOINED)
</#if>
<#if interface.parent??>
public class ${interface.name}Entity extends ${interface.parent.name}Entity implements ${interface.name} {
<#else>
public class ${interface.name}Entity extends AbstractEntity<${interface.name}, Long> implements ${interface.name} {
</#if>

<#list interface.attributes as attribute>
    <#if attribute.type.dataType = "JAVA">
        <#if attribute.type.name = "Date">
    @Column(name = "${attribute.name?upper_case}")
    private java.util.Date ${attribute.name}<#if attribute.default?? && attribute.default = "now"> = new java.util.Date()</#if>;

        <#else>
    @Column(name = "${attribute.name?upper_case}")
    private ${attribute.type.name} ${attribute.name};

        </#if>
    <#elseif attribute.type.dataType = "PRIMITIVE">
    @Column(name = "${attribute.name?upper_case}")
    private ${attribute.type.name} ${attribute.name};

    <#elseif attribute.type.dataType = "META">
    @org.hibernate.annotations.Type(type = "com.lazyloris.model.hibernate.BlobMetaType")
    @Column(name = "${attribute.name?upper_case}")
    private com.lazyloris.model.Meta ${attribute.name} = new com.lazyloris.model.impl.MetaImpl();

    <#elseif attribute.type.dataType = "ENUM">
    @Column(name = "${attribute.name?upper_case}")
    @Enumerated(EnumType.STRING)
    private ${attribute.type.name} ${attribute.name}<#if attribute.default??> = ${attribute.type.name}.${attribute.default}</#if>;

    </#if>
</#list>
<#list interface.associations as association>
    <#if association.type = "MANY" || association.type = "COMPOSITE">
        <#if association.end?? && association.end.type = "MANY">
            <#if utils.isManyToManyMappedBy(association)>
    @ManyToMany(mappedBy = "${utils.getAssociationName(association.end)}", cascade = { CascadeType.PERSIST, CascadeType.MERGE }, targetEntity = ${association.interface.name}Entity.class)
            <#else>
    @ManyToMany(cascade = { CascadeType.PERSIST, CascadeType.MERGE }, targetEntity = ${association.interface.name}Entity.class)
    @JoinTable(name = "${utils.getManyToManyTableName(association, model)}", joinColumns = { @JoinColumn(name = "${utils.getAssociationColumnName(association, interface)}") }, inverseJoinColumns = { @JoinColumn(name = "${utils.getAssociationColumnName(association.end, association.interface)}") })
            </#if>
        <#else>
    @OneToMany(cascade = <#if association.type = "COMPOSITE">CascadeType.ALL<#else>{ CascadeType.PERSIST, CascadeType.MERGE }</#if>, targetEntity = ${association.interface.name}Entity.class)
    @JoinColumn(name = "${utils.getAssociationColumnName(association, interface)}") 
        </#if>
        <#if association.stereotypes?size &gt; 0>
        	<#list association.stereotypes as stereotype>
        		<#if stereotype?starts_with("batchChildren")>
    @org.hibernate.annotations.BatchSize(size = ${stereotype?substring(13)})
        		</#if>
        	</#list>
        </#if>
    private java.util.List<${association.interface.name}> ${utils.getAssociationName(association)} = new java.util.ArrayList<${association.interface.name}>();

    <#elseif association.type = "SINGLE">
        <#if association.end?? && (association.end.type = "MANY" || association.end.type = "COMPOSITE")>
    @ManyToOne(targetEntity = ${association.interface.name}Entity.class, cascade = { CascadeType.PERSIST, CascadeType.MERGE }<#if association.end.type = "COMPOSITE">, fetch = FetchType.EAGER</#if>)
    @JoinColumn(name = "${utils.getAssociationColumnName(association, interface)}")
    private ${association.interface.name} ${utils.getAssociationName(association)};

        <#else>
        </#if>
    </#if>
</#list>
<#if !interface.parent??>
    @Override
    @Id
    @Column(name = "${interface.name?upper_case}ID")
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "${model.name}_SEQUENCE")
    @TableGenerator(name = "${model.name}_SEQUENCE", table = "T_${model.name}_SEQUENCE", pkColumnName = "T", pkColumnValue = "${interface.name?upper_case}", valueColumnName = "SEQ", allocationSize = 1000, initialValue = 1)
    @Access(AccessType.PROPERTY)
    public Long getId() {
        return super.getId();
    }

</#if>
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
        ${child.name} ${child.name?uncap_first} = new ${child.name}Entity();
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
        ${association.interface.name} ${association.interface.name?uncap_first} = new ${association.interface.name}Entity();
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