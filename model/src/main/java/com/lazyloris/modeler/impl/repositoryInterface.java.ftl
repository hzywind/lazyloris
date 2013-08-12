package ${module.name}.repository;

import java.util.List;
import com.lazyloris.model.PersistenceRepository;
import ${module.name}.*;

public interface Repository extends PersistenceRepository {
    
<#list module.interfaces as interface>
    ${interface.name} create${interface.name}();
    
    List<${interface.name}> findAll${interface.name}();
    
    ${interface.name} load${interface.name}(Long id);
    
</#list>
}
