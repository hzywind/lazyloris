<?xml version="1.0" encoding="UTF-8"?>
<persistence xmlns="http://java.sun.com/xml/ns/persistence"
			 xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
			 xsi:schemaLocation="http://java.sun.com/xml/ns/persistence http://java.sun.com/xml/ns/persistence/persistence_1_0.xsd"
			 version="1.0">
	<persistence-unit name="${model.name}">
<#list module.interfaces as interface>
		<class>${interface.package}.entity.${interface.name}Entity</class>
</#list>
	</persistence-unit>
</persistence>