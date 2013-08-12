<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE hibernate-mapping PUBLIC
	"-//Hibernate/Hibernate Mapping DTD 3.0//EN"
	"http://hibernate.sourceforge.net/hibernate-mapping-3.0.dtd">
<hibernate-mapping default-access="field">
<#list module.enums as enum>
	<typedef name="${module.name}.${enum.name}" class="com.lazyloris.model.hibernate.StringEnumType">
		<param name="enumClassname">${module.name}.${enum.name}</param>
	</typedef>

</#list>
</hibernate-mapping>