<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="2.0" exclude-result-prefixes="xmi uml fo" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:fo="http://www.w3.org/1999/XSL/Format" xmlns:uml="http://schema.omg.org/spec/UML/2.2" xmlns:xmi="http://schema.omg.org/spec/XMI/2.1">
	<xsl:output method="xml" encoding="utf-8" indent="yes"/>
	<xsl:variable name="pkg" select="//uml:Model/packagedElement[@xmi:type='uml:Package']/@name"/>
	<xsl:template match="/">
		<xsl:apply-templates select="xmi:XMI/uml:Model"/>
	</xsl:template>
	<xsl:template match="uml:Model">
		<model>
			<xsl:attribute name="name"><xsl:value-of select="@name"/></xsl:attribute>
			<xsl:apply-templates select="packagedElement[@xmi:type='uml:Package']"/>
		</model>
	</xsl:template>
	<xsl:template match="packagedElement[@xmi:type='uml:Package' and string-length(@name) &gt; 0]">
		<module>
			<xsl:attribute name="name"><xsl:value-of select="@name"/></xsl:attribute>
			<xsl:apply-templates select="packagedElement[@xmi:type='uml:Class' or @xmi:type='uml:Enumeration']"/>
		</module>
	</xsl:template>
	<xsl:template match="packagedElement[@xmi:type='uml:Class'][@name != 'Meta']">
		<interface>
			<xsl:attribute name="name"><xsl:value-of select="@name"/></xsl:attribute>
			<xsl:apply-templates select="ownedAttribute"/>
			<!--
			<xsl:variable name="classId" select="@xmi:id"/>
			<xsl:apply-templates select="//packagedElement[@xmi:type='uml:Association']/ownedEnd[@type=$classId]"/>
			-->
			<xsl:if test="boolean(generalization)">
				<xsl:variable name="parent" select="generalization/@general"/>
				<parent>
					<xsl:attribute name="interface"><xsl:value-of select="//packagedElement[@xmi:id=$parent]/@name"/></xsl:attribute>
				</parent>
			</xsl:if>
			<xsl:variable name="classId" select="@xmi:id"/>
			<xsl:apply-templates select="//*[@base_Class = $classId]"/>
		</interface>
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
	<xsl:template match="ownedAttribute[not(@association)]">
		<attribute>
			<xsl:attribute name="name"><xsl:value-of select="@name"/></xsl:attribute>
			<xsl:if test="boolean(defaultValue)">
				<xsl:attribute name="default"><xsl:choose><xsl:when test="defaultValue/@value"><xsl:value-of select="defaultValue/@value"/></xsl:when><xsl:when test="defaultValue/@xmi:type = 'uml:InstanceValue'"><xsl:variable name="instanceId" select="defaultValue/@instance"/><xsl:value-of select="//*[@xmi:id=$instanceId]/@name"/></xsl:when><xsl:otherwise>
							</xsl:otherwise></xsl:choose></xsl:attribute>
			</xsl:if>
			<xsl:if test="boolean(@isReadOnly)">
				<xsl:attribute name="readOnly"><xsl:value-of select="@isReadOnly"/></xsl:attribute>
			</xsl:if>
			<type>
				<xsl:choose>
					<xsl:when test="boolean(type/xmi:Extension)">
						<xsl:choose>
							<xsl:when test="type/xmi:Extension/referenceExtension/@referentPath = 'UML Standard Profile::UML2 Metamodel::AuxiliaryConstructs::PrimitiveTypes::String'">
								<xsl:attribute name="dataType">JAVA</xsl:attribute>
								<xsl:attribute name="name">String</xsl:attribute>
							</xsl:when>
							<xsl:when test="type/xmi:Extension/referenceExtension/@referentPath = 'UML Standard Profile::MagicDraw Profile::datatypes::date'">
								<xsl:attribute name="dataType">JAVA</xsl:attribute>
								<xsl:attribute name="name">Date</xsl:attribute>
							</xsl:when>
							<xsl:otherwise>
								<xsl:attribute name="dataType">PRIMITIVE</xsl:attribute>
								<xsl:attribute name="name"><xsl:value-of select="substring-after(type/xmi:Extension/referenceExtension/@referentPath, 'UML Standard Profile::MagicDraw Profile::datatypes::')"/></xsl:attribute>
							</xsl:otherwise>
						</xsl:choose>
					</xsl:when>
					<xsl:when test="@type">
						<xsl:variable name="typeRef" select="@type"/>
						<xsl:variable name="typeName" select="//*[@xmi:id = $typeRef]/@name"/>
						<xsl:variable name="typeType" select="//*[@xmi:id = $typeRef]/@xmi:type"/>
						<xsl:if test="$typeName = 'Meta'">
							<xsl:attribute name="dataType">META</xsl:attribute>
							<xsl:attribute name="name">com.lazyloris.util.model.Meta</xsl:attribute>
						</xsl:if>
						<xsl:if test="$typeType = 'uml:Enumeration'">
							<xsl:attribute name="dataType">ENUM</xsl:attribute>
							<xsl:attribute name="name"><xsl:value-of select="$pkg"/>.<xsl:value-of select="$typeName"/></xsl:attribute>
						</xsl:if>
					</xsl:when>
				</xsl:choose>
			</type>
		</attribute>
	</xsl:template>
	<xsl:template match="ownedAttribute[@association]">
		<association>
			<xsl:if test="@name">
				<xsl:attribute name="name"><xsl:value-of select="@name"/></xsl:attribute>
			</xsl:if>
			<xsl:variable name="type" select="@type"/>
			<xsl:attribute name="interface">
				<xsl:value-of select="//packagedElement[@xmi:id=$type]/@name"/>
			</xsl:attribute>
			<xsl:attribute name="type">
				<xsl:choose>
					<xsl:when test="@aggregation">
						<xsl:choose>
							<xsl:when test="@aggregation = 'composite'">
								<xsl:value-of select="'COMPOSITE'"/>
							</xsl:when>
							<xsl:otherwise>
								<xsl:value-of select="'MANY'"/>
							</xsl:otherwise>
						</xsl:choose>
					</xsl:when>
					<xsl:when test=".//upperValue/@value = '1'">
						<xsl:value-of select="'SINGLE'"/>
					</xsl:when>
					<xsl:when test=".//upperValue/@value = '*'">
						<xsl:value-of select="'MANY'"/>
					</xsl:when>
				</xsl:choose>
			</xsl:attribute>
			<xsl:variable name="assoId" select="@association"/>
			<xsl:variable name="asso" select="//packagedElement[@xmi:id=$assoId]"/>
			<xsl:variable name="ownerId" select="@xmi:id"/>
			<xsl:if test="//packagedElement[@xmi:type='uml:Association'][@xmi:id=$assoId]/memberEnd[@xmi:idref!=$ownerId]">
				<xsl:variable name="endId" select="//packagedElement[@xmi:type='uml:Association'][@xmi:id=$assoId]/memberEnd[@xmi:idref!=$ownerId]/@xmi:idref"/>
				<xsl:if test="not(//packagedElement[@xmi:type='uml:Association'][@xmi:id=$assoId]/ownedEnd)">
					<xsl:attribute name="end">
						<xsl:variable name="end" select="//ownedAttribute[@xmi:id=$endId]"/>
						<xsl:choose>
							<xsl:when test="string-length($end/@name) &gt; 0">
								<xsl:value-of select="$end/@name"/>
							</xsl:when>
							<xsl:otherwise>
								<xsl:value-of select="//packagedElement/ownedAttribute[@association=$assoId][@xmi:id=$ownerId]/../@name"/>
							</xsl:otherwise>
						</xsl:choose>
					</xsl:attribute>
				</xsl:if>
			</xsl:if>
			<!--<xsl:attribute name="interface">
				<xsl:variable name="endId" select="$asso/memberEnd[@xmi:idref != $ownerId]/@xmi:idref"/>
				<xsl:value-of select="//ownedAttribute[@xmi:id = $endId]/../@name"/>
				<xsl:value-of select="$endId"/>
			</xsl:attribute>-->
			<xsl:apply-templates select="//*[@base_Association = $assoId]"/>
		</association>
	</xsl:template>
	<xsl:template match="//*[@base_Association] | //*[@base_Class]">
		<stereotype><xsl:value-of select="local-name()"/></stereotype>
	</xsl:template>
</xsl:stylesheet>
