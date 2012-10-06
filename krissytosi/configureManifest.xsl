<?xml version="1.0" encoding="UTF-8" ?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    version=".0" xmlns:android='http://schemas.android.com/apk/res/android'>

    <xsl:param name="VERSIONNAME" />
    <xsl:param name="VERSIONCODE" />
    <xsl:output indent="yes" />

    <xsl:template match="/">
        <xsl:text>&#13;</xsl:text>
        <xsl:apply-templates select="@*|node()" />
    </xsl:template>

    <xsl:template match="/manifest/@android:versionName">
        <xsl:attribute name="android:versionName">
            <xsl:value-of select="$VERSIONNAME" />
        </xsl:attribute>
    </xsl:template>

    <xsl:template match="/manifest/@android:versionCode">
        <xsl:attribute name="android:versionCode">
            <xsl:value-of select="$VERSIONCODE" />
        </xsl:attribute>
    </xsl:template>

    <xsl:template match="@*|node()">
        <xsl:copy>
            <xsl:apply-templates select="@*|node()" />
        </xsl:copy>
    </xsl:template>
</xsl:stylesheet>
