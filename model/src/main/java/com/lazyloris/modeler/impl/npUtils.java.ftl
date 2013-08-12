package ${module.name}.np;

import java.util.ArrayList;
import java.util.List;

import ${module.name}.*;

public class NPUtils {
    
<#list module.interfaces as interface>
    public static ${interface.name}NP package${interface.name}(${interface.name} ${interface.name?uncap_first}) {
        ${interface.name}NP np${interface.name} = new ${interface.name}NP();
        np${interface.name}.copy(${interface.name?uncap_first});
        return np${interface.name};
    }
    
    public static List<${interface.name}NP> package${interface.name}List(List<${interface.name}> collection) {
        List<${interface.name}NP> np${interface.name}List = new ArrayList<${interface.name}NP>();
        for (${interface.name} obj : collection) {
            np${interface.name}List.add(package${interface.name}(obj));
        }
        return np${interface.name}List;
    }
    
</#list>
}
