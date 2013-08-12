<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE hibernate-configuration PUBLIC
		"-//Hibernate/Hibernate Configuration DTD 3.0//EN"
		"http://hibernate.sourceforge.net/hibernate-configuration-3.0.dtd">
<hibernate-configuration>
    <session-factory name="${model.name}">
        <property name="format_sql">${props['format_sql']}</property>
        <property name="show_sql">${props['show_sql']}</property>
        <property name="use_sql_comments">${props['use_sql_comments']}</property>
        <#if module.enums?size &gt; 0>
        <mapping resource="${module.name?replace(".","/")}/hibernate/types.hbm.xml"/>
        </#if>
        <#list module.interfaces as interface>
        	<#if !interface.parent??>
        <mapping resource="${interface.package?replace(".","/")}/hibernate/${interface.name}Impl.hbm.xml"/>
        	</#if>
        </#list>
    </session-factory>
</hibernate-configuration>
