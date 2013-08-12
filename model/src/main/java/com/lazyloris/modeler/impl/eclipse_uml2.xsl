<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="2.0" exclude-result-prefixes="xmi uml fn fo xs" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:fo="http://www.w3.org/1999/XSL/Format" xmlns:xs="http://www.w3.org/2001/XMLSchema" xmlns:fn="http://www.w3.org/2005/xpath-functions" xmlns:xmi="http://schema.omg.org/spec/XMI/2.1" xmlns:uml="http://www.eclipse.org/uml2/3.0.0/UML">
	<xsl:output method="xml" encoding="utf-8" indent="yes"/>
	<xsl:variable name="pkg" select="/uml:Model/packagedElement[@xmi:type='uml:Package']/@name"/>
	<xsl:template match="/uml:Model">
		<model>
			<xsl:attribute name="name"><xsl:value-of select="@name"/></xsl:attribute>
			<xsl:apply-templates select="packagedElement[@xmi:type='uml:Package']"/>
		</model>
	</xsl:template>
	<xsl:template match="packagedElement[@xmi:type='uml:Package']">
		<module>
			<xsl:attribute name="name"><xsl:value-of select="@name"/></xsl:attribute>
			<xsl:apply-templates select="packagedElement[@xmi:type='uml:Class' or @xmi:type='uml:Enumeration']"/>
		</module>
	</xsl:template>
	<xsl:template match="packagedElement[@xmi:type='uml:Class']">
		<interface>
			<xsl:attribute name="name"><xsl:value-of select="@name"/></xsl:attribute>
			<xsl:apply-templates select="ownedAttribute"/>
			<xsl:variable name="classId" select="@xmi:id"/>
			<xsl:apply-templates select="//packagedElement[@xmi:type='uml:Association']/ownedEnd[@type=$classId]"/>
			<xsl:if test="boolean(generalization)">
				<xsl:variable name="parent" select="generalization/@general"/>
				<parent>
					<xsl:attribute name="interface">
						<xsl:value-of select="//packagedElement[@xmi:id=$parent]/@name"/>
					</xsl:attribute>
				</parent>
			</xsl:if>
		</interface>
	</xsl:template>
	<xsl:template match="packagedElement[@xmi:type='uml:Association']/ownedEnd">
		<xsl:variable name="id" select="@xmi:id"/>
		<xsl:choose>
			<xsl:when test="boolean(../@navigableOwnedEnd)"><!--unidirenctional association-->
				<xsl:if test="$id != ../@navigableOwnedEnd">
					<xsl:call-template name="association">
						<xsl:with-param name="endNode" select="../ownedEnd[@xmi:id=../@navigableOwnedEnd]"/>
						<xsl:with-param name="bidirectional" select="'no'"/>
					</xsl:call-template>
				</xsl:if>
			</xsl:when>
			<xsl:otherwise><!--bidirectional association-->
				<xsl:call-template name="association">
					<xsl:with-param name="endNode" select="../ownedEnd[@xmi:id != $id]"/>
					<xsl:with-param name="bidirectional" select="'yes'"/>
				</xsl:call-template>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>
	<xsl:template name="association">
		<xsl:param name="endNode"/>
		<xsl:param name="bidirectional"/>
		<association>
			<xsl:if test="string-length($endNode/@name) &gt; 0">
				<xsl:attribute name="name"><xsl:value-of select="$endNode/@name"/></xsl:attribute>
			</xsl:if>
			<xsl:attribute name="interface"><xsl:value-of select="//packagedElement[@xmi:type = 'uml:Class' and @xmi:id = $endNode/@type]/@name"/></xsl:attribute>
			<xsl:attribute name="type">
				<xsl:choose>
					<xsl:when test="$endNode/@aggregation = 'composite'"><xsl:value-of select="'COMPOSITE'"/></xsl:when>
					<xsl:when test="$endNode/upperValue/@value = '*'"><xsl:value-of select="'MANY'"/></xsl:when>
					<xsl:otherwise><xsl:value-of select="'SINGLE'"/></xsl:otherwise>
				</xsl:choose>
			</xsl:attribute>
			<xsl:if test="$bidirectional = 'yes'">
				<xsl:attribute name="end">
					<xsl:variable name="id" select="$endNode/@xmi:id"/>
					<xsl:variable name="endName" select="$endNode/../ownedEnd[@xmi:id != $id]/@name"/>
					<xsl:choose>
						<xsl:when test="string-length($endName) = 0">
							<xsl:value-of select="//packagedElement[@xmi:type = 'uml:Class' and @xmi:id = $endNode/../ownedEnd[@xmi:id != $id]/@type]/@name"/>
						</xsl:when>
						<xsl:otherwise>
							<xsl:value-of select="$endName"/>
						</xsl:otherwise>
					</xsl:choose>
				</xsl:attribute>
			</xsl:if>
		</association>
	</xsl:template>
	<xsl:template match="packagedElement[@xmi:type='uml:Enumeration']">
		<enum>
			<xsl:attribute name="name"><xsl:value-of select="@name"/></xsl:attribute>
			<xsl:for-each select="ownedLiteral">
				<item>
					<xsl:attribute name="value"><xsl:value-of select="@name"/></xsl:attribute>
				</item>
			</xsl:for-each>
		</enum>
	</xsl:template>
	<xsl:template match="ownedAttribute">
		<attribute>
			<xsl:attribute name="name"><xsl:value-of select="@name"/></xsl:attribute>
			<xsl:if test="boolean(defaultValue/@value)">
				<xsl:attribute name="default"><xsl:value-of select="defaultValue/@value"/></xsl:attribute>
			</xsl:if>
			<xsl:if test="boolean(@isReadOnly)">
				<xsl:attribute name="readOnly"><xsl:value-of select="@isReadOnly"/></xsl:attribute>
			</xsl:if>
			<type>
				<xsl:if test="boolean(@type)">
					<xsl:variable name="typeId" select="@type"/>
					<xsl:variable name="typeNode" select="//packagedElement[@xmi:id=$typeId]"/>
					<xsl:choose>
						<xsl:when test="$typeNode/@xmi:type = 'uml:Enumeration'">
							<xsl:attribute name="dataType">ENUM</xsl:attribute>
							<xsl:attribute name="name"><xsl:value-of select="concat($pkg, '.', $typeNode/@name)"/></xsl:attribute>
						</xsl:when>
						<xsl:when test="$typeNode/@xmi:type = 'uml:DataType' and $typeNode/@name = 'Meta'">
							<xsl:attribute name="dataType">META</xsl:attribute>
							<xsl:attribute name="name">com.lazyloris.util.model.Meta</xsl:attribute>
						</xsl:when>
						<xsl:when test="$typeNode/@xmi:type = 'uml:DataType'">
							<xsl:attribute name="dataType">JAVA</xsl:attribute>
							<xsl:attribute name="name"><xsl:value-of select="$typeNode/@name"/></xsl:attribute>
						</xsl:when>
					</xsl:choose>
				</xsl:if>
				<xsl:if test="boolean(type) and type/@xmi:type='uml:PrimitiveType'">
					<xsl:attribute name="dataType">PRIMITIVE</xsl:attribute>
					<xsl:attribute name="name"><xsl:value-of select="substring-after(type/@href, '#')"/></xsl:attribute>
				</xsl:if>
			</type>
		</attribute>
	</xsl:template>
</xsl:stylesheet>
