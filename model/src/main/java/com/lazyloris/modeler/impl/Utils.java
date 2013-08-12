package com.lazyloris.modeler.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.lazyloris.modeler.Association;
import com.lazyloris.modeler.Interface;
import com.lazyloris.modeler.Model;
import com.lazyloris.modeler.Module;

public class Utils {

    public String lowerInitial(String str) {
        return str.substring(0, 1).toLowerCase() + str.substring(1);
    }

    public String getAssociationName(Association a) {
        String name = a.getName();
        if (name == null || name.length() == 0) {
            name = lowerInitial(a.getInterface().getName());
            if ((a.getType() == Association.Type.MANY || a.getType() == Association.Type.COMPOSITE)
                    && !name.endsWith("s"))
                name += 's';
        }
        return name;
    }

    public String getAssociationColumnName(Association a, Interface i) {
        if (a.getType() == Association.Type.MANY
                || a.getType() == Association.Type.COMPOSITE) {
            if (a.getEnd() != null) {
                if (a.getEnd().getType() == Association.Type.SINGLE) {
                    // bidirectional one-to-many, use the single end name as
                    // column name
                    String name = a.getEnd().getName();
                    if (name == null)
                        name = a.getEnd().getInterface().getName();
                    return (name + "ID").toUpperCase();
                } else if (a.getEnd().getType() == Association.Type.SINGLE) {
                    // bidirectional many-to-many
                    return (i.getName() + "ID").toUpperCase();
                }
            }
            // unidirectional one-to-many
            return (i.getName() + "ID").toUpperCase();
        } else {
            // many-to-one
            String name = a.getName();
            if (name == null)
                name = a.getInterface().getName();
            return (name + "ID").toUpperCase();
        }
    }

    Map<Association, String> manyToManyTableNames = new HashMap<Association, String>();

    public String getManyToManyTableName(Association a, Model m) {
        String name = manyToManyTableNames.get(a);
        if (name == null)
            name = manyToManyTableNames.get(a.getEnd());
        if (name == null) {
            name = "T_" + m.getName() + "_" + a.getInterface().getName() + "_"
                    + a.getEnd().getInterface().getName();
            name = name.toUpperCase();
            manyToManyTableNames.put(a, name);
        }
        return name;
    }

    public boolean isManyToManyMappedBy(Association a) {
        return (manyToManyTableNames.get(a) != null)
                || (manyToManyTableNames.get(a.getEnd()) != null);
    }

    public boolean hasChildrenInterfaces(Interface intf, Module module) {
        if (intf.getParent() != null)
            return false;
        for (Interface i : module.getInterfaces()) {
            if (intf == i.getParent()) {
                return true;
            }
        }
        return false;
    }

    public List<Interface> getChildren(Interface intf, Module module) {
        List<Interface> children = new ArrayList<Interface>();
        for (Interface i : module.getInterfaces()) {
            if (i.getParent() == intf) {
                children.add(i);
            }
        }
        return children;
    }

}
