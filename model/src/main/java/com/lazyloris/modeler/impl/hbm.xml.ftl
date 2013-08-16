<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE hibernate-mapping PUBLIC
	"-//Hibernate/Hibernate Mapping DTD 3.0//EN"
	"http://hibernate.sourceforge.net/hibernate-mapping-3.0.dtd">
<hibernate-mapping default-access="field">
	<class name="${interface.package}.hibernate.${interface.name}Impl" table="T_${model.name}_${interface.name?upper_case}">
		<id name="id" column="${interface.name?upper_case}ID" type="long">
			<generator class="org.hibernate.id.enhanced.TableGenerator">
				<param name="table_name">T_${model.name}_SEQUENCE</param>
				<param name="segment_column_name">T</param>
				<param name="segment_value">${interface.name?upper_case}</param>
				<param name="value_column_name">SEQ</param>
				<param name="increment_size">1000</param>
				<param name="optimizer">pooled</param>
			</generator>
		</id>
<#list interface.attributes as attribute>
		<property name="${attribute.name}" column="${attribute.name?upper_case}" <#if attribute.type.dataType = "META">type="com.lazyloris.model.hibernate.BlobMetaType"<#elseif attribute.type.dataType = "ENUM">type="${attribute.type.name}"</#if>/>
</#list>
<#list interface.associations as association>
	<#if association.type = "MANY" || association.type = "COMPOSITE">
		<#if association.end?? && association.end.type = "MANY">
		<!-- many-to-many -->
		<bag name="${utils.getAssociationName(association)}" table="${utils.getManyToManyTableName(association, model)}" cascade="save-update" <#if utils.isManyToManyMappedBy(association)>inverse="false"</#if>>
			<key column="${utils.getAssociationColumnName(association, interface)}"/>
			<many-to-many column="${utils.getAssociationColumnName(association.end, association.interface)}" class="${association.interface.package}.hibernate.${association.interface.name}Impl" />
		</bag>
		<#else>
		<!-- one-to-many -->
		<bag name="${utils.getAssociationName(association)}" cascade="<#if association.type = "COMPOSITE">all<#else>save-update</#if>">
			<key column="${utils.getAssociationColumnName(association, interface)}" />
			<one-to-many class="${association.interface.package}.hibernate.${association.interface.name}Impl" />
		</bag>
		</#if>
	<#elseif association.type = "SINGLE">
		<#if association.end?? && (association.end.type = "MANY" || association.end.type = "COMPOSITE")>
		<!-- bidirectional many-to-one -->
		<many-to-one name="${utils.getAssociationName(association)}" column="${utils.getAssociationColumnName(association, interface)}" class="${association.interface.package}.hibernate.${association.interface.name}Impl" />
		<#else>
		<!-- unidirectional many-to-one -->
		<many-to-one name="${utils.getAssociationName(association)}" column="${utils.getAssociationColumnName(association, interface)}" class="${association.interface.package}.hibernate.${association.interface.name}Impl" />
		</#if>
	</#if>
</#list>

<#macro joinedSubclasses child keyCol modName mod>
		<joined-subclass name="${child.package}.hibernate.${child.name}Impl" table="T_${modName}_${child.name?upper_case}">
			<key column="${keyCol}"/>
	<#list child.attributes as attribute>
			<property name="${attribute.name}" column="${attribute.name?upper_case}" <#if attribute.type.dataType = "META">type="com.lazyloris.model.hibernate.BlobMetaType"<#elseif attribute.type.dataType = "ENUM">type="${attribute.type.name}"</#if>/>
	</#list>

	<#list utils.getChildren(child, mod) as c>
			<@joinedSubclasses child=c keyCol=keyCol modName=modName mod=mod/>
	</#list>
		</joined-subclass>

</#macro>
<#list utils.getChildren(interface, module) as child>
	<@joinedSubclasses child=child keyCol="${interface.name?upper_case}ID" modName="${model.name}" mod=module/>
</#list>


	</class>
</hibernate-mapping>