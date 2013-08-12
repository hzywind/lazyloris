package ${module.name};

public enum ${enum.name} {
<#list enum.enumValues as value>
    ${value}<#if value_has_next>,</#if>
</#list>
}
