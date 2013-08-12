package ${module.name}.repository.impl;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;

import com.lazyloris.model.impl.AbstractRepository;

import ${module.name}.*;
import ${module.name}.entity.*;
import ${module.name}.repository.Repository;

public class RepositoryImpl extends AbstractRepository implements Repository {
    
<#list module.interfaces as interface>
    public ${interface.name} create${interface.name}() {
        return new ${interface.name}Entity();
    }
    
    public List<${interface.name}> findAll${interface.name}() {
        CriteriaBuilder cb = getCriteriaBuilder();
        CriteriaQuery<${interface.name}Entity> cq = (CriteriaQuery<${interface.name}Entity>) cb.createQuery(${interface.name}Entity.class);
        cq.from(${interface.name}Entity.class);
        List<${interface.name}> result = new ArrayList<${interface.name}>();
        for (${interface.name}Entity entity : getEntityManager().createQuery(cq).getResultList()) {
            result.add(entity);
        }
        return result;
    }
    
    public ${interface.name} load${interface.name}(Long id) {
        return (${interface.name}) getEntityManager().find(${interface.name}Entity.class, id);
    }
    
</#list>
}
