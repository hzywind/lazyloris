package com.lazyloris.modeler.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.lazyloris.modeler.Association;
import com.lazyloris.modeler.Attribute;
import com.lazyloris.modeler.Constants;
import com.lazyloris.modeler.Enum;
import com.lazyloris.modeler.Interface;
import com.lazyloris.modeler.Model;
import com.lazyloris.modeler.Module;
import com.lazyloris.modeler.Type;
import com.lazyloris.modeler.Type.DataType;

public class ModelParserImpl extends XsltModelParser {

	private Set<Interface> cache = new HashSet<Interface>();
	private Map<Interface, String> pendingReferences = new HashMap<Interface, String>();
	private Map<Type, String> pendingTypes = new HashMap<Type, String>();
	private Map<Association, String> pendingAssociations = new HashMap<Association, String>();
	private Map<Association, String> pendingAssociationEnds = new HashMap<Association, String>();

	@Override
	List<Model> parse(Document doc) {
		List<Model> models = new ArrayList<Model>();
		Element element = doc.getDocumentElement();
		if (element.getLocalName().equals(Constants.ELEMENT_NAME_MODEL)) {
			Model model = parseModel(element);
			models.add(model);
		}
		return models;
	}

	private Model parseModel(Element element) {
		ModelImpl model = new ModelImpl();
		String name = element.getAttribute(Constants.ATTRIBUTE_NAME_NAME);
		if (!isEmpty(name))
			model.name = name;
		for (Element node : childElements(element,
				Constants.ELEMENT_NAME_MODULE)) {
			Module module = parseModule(node);
			model.modules.add(module);
			if (!pendingReferences.isEmpty()) {
				for (Map.Entry<Interface, String> entry : pendingReferences
						.entrySet()) {
					((InterfaceImpl) entry.getKey()).parent = lookupInterface(entry
							.getValue());
				}
				pendingReferences.clear();
			}
			if (!pendingTypes.isEmpty()) {
				for (Map.Entry<Type, String> entry : pendingTypes.entrySet()) {
					((TypeImpl) entry.getKey()).interf = lookupInterface(entry
							.getValue());
				}
				pendingTypes.clear();
			}
			if (!pendingAssociations.isEmpty()) {
				for (Map.Entry<Association, String> entry : pendingAssociations
						.entrySet()) {
					((AssociationImpl) entry.getKey()).interf = lookupInterface(entry
							.getValue());
				}
				pendingAssociations.clear();
			}
			if (!pendingAssociationEnds.isEmpty()) {
				for (Map.Entry<Association, String> entry : pendingAssociationEnds
						.entrySet()) {
					boolean found = false;
					for (Association a : ((AssociationImpl) entry.getKey()).interf
							.getAssociations()) {
						if (entry.getValue().equals(a.getName())) {
							((AssociationImpl) entry.getKey()).end = a;
							found = true;
							break;
						}
					}
					if (!found) {
						for (Association a : ((AssociationImpl) entry.getKey()).interf
								.getAssociations()) {
							if (entry.getValue().equals(
									a.getInterface().getName())) {
								((AssociationImpl) entry.getKey()).end = a;
								found = true;
								break;
							}
						}
					}
					if (!found) {
						throw new RuntimeException(
								String
										.format(
												"Could not find out the end [%s] of association [%s].",
												entry.getValue(), entry
														.getKey().getName()));
					}
				}
				pendingAssociationEnds.clear();
			}
		}
		return model;
	}

	private Module parseModule(Element element) {
		ModuleImpl module = new ModuleImpl();
		String name = element.getAttribute(Constants.ATTRIBUTE_NAME_NAME);
		if (!isEmpty(name))
			module.name = name;
		for (Element node : childElements(element,
				Constants.ELEMENT_NAME_INTERFACE)) {
			Interface interf = parseInterface(node, module.name);
			cache.add(interf);
			module.interfaces.add(interf);
		}
		for (Element node : childElements(element, Constants.ELEMENT_NAME_ENUM)) {
			Enum e = parseEnum(node);
			module.enums.add(e);
		}
		return module;
	}

	private Enum parseEnum(Element element) {
		EnumImpl e = new EnumImpl();
		String name = element.getAttribute(Constants.ATTRIBUTE_NAME_NAME);
		if (!isEmpty(name))
			e.name = name;
		for (Element node : childElements(element, Constants.ELEMENT_NAME_ITEM)) {
			e.items.add(node.getAttribute(Constants.ATTRIBUTE_NAME_VALUE));
		}
		return e;
	}

	private Interface parseInterface(Element element, String moduleName) {
		InterfaceImpl interf = new InterfaceImpl();
		interf.pkg = moduleName;
		String name = element.getAttribute(Constants.ATTRIBUTE_NAME_NAME);
		if (!isEmpty(name)) {
			interf.name = name;
			interf.fullName = moduleName + "." + name;
		}
		for (Element node : childElements(element,
				Constants.ELEMENT_NAME_ATTRIBUTE)) {
			Attribute attr = parseAttribute(node);
			interf.attributes.add(attr);
		}
		for (Element node : childElements(element,
				Constants.ELEMENT_NAME_ASSOCIATION)) {
			Association asso = parseAssociation(node);
			interf.associations.add(asso);
		}
		for (Element node : childElements(element,
				Constants.ELEMENT_NAME_PARENT)) {
			String parentName = node
					.getAttribute(Constants.ATTRIBUTE_NAME_INTERFACE);
			Interface parent = lookupInterface(parentName);
			if (parent == null)
				pendingReferences.put(interf, parentName);
			else
				interf.parent = parent;
		}
		return interf;
	}

	private Association parseAssociation(Element element) {
		AssociationImpl asso = new AssociationImpl();
		String name = element.getAttribute(Constants.ATTRIBUTE_NAME_NAME);
		if (!isEmpty(name))
			asso.name = name;
		asso.type = com.lazyloris.modeler.Association.Type
				.valueOf(element.getAttribute(Constants.ATTRIBUTE_NAME_TYPE));
		String interfName = element
				.getAttribute(Constants.ATTRIBUTE_NAME_INTERFACE);
		if (!isEmpty(interfName)) {
			Interface interf = lookupInterface(interfName);
			if (interf != null)
				asso.interf = interf;
			else
				pendingAssociations.put(asso, interfName);
		}
		String end = element.getAttribute(Constants.ATTRIBUTE_NAME_END);
		if (!isEmpty(end)) {
			if (asso.interf != null) {
				for (Association a : asso.interf.getAssociations()) {
					if (end.equals(a.getName())) {
						asso.end = a;
						break;
					}
				}
				if (asso.end == null) {
					pendingAssociationEnds.put(asso, end);
				}
			} else {
				pendingAssociationEnds.put(asso, end);
			}
		}
		asso.stereotypes = parseStereotypes(element.getElementsByTagName(Constants.ELEMENT_NAME_STEREOTYPE));
		return asso;
	}

	private List<String> parseStereotypes(NodeList stereotypeNodeList) {
		List<String> stereotypes = new ArrayList<String>();
		for (int i = 0; i< stereotypeNodeList.getLength(); i++) {
			String st = ((Element) stereotypeNodeList.item(i)).getFirstChild().getNodeValue();
			if (st != null && !st.isEmpty())
				stereotypes.add(st);
		}
		return stereotypes;
	}

	private Attribute parseAttribute(Element element) {
		AttributeImpl attr = new AttributeImpl();
		String name = element.getAttribute(Constants.ATTRIBUTE_NAME_NAME);
		if (!isEmpty(name))
			attr.name = name;
		String defaultValue = element
				.getAttribute(Constants.ATTRIBUTE_NAME_DEFAULT);
		if (!isEmpty(defaultValue))
			attr.defaultValue = defaultValue;
		String readOnly = element
				.getAttribute(Constants.ATTRIBUTE_NAME_READONLY);
		if (!isEmpty(readOnly) && Boolean.valueOf(readOnly))
			attr.readOnly = true;
		String unique = element.getAttribute(Constants.ATTRIBUTE_NAME_UNIQUE);
		if (!isEmpty(unique) && Boolean.valueOf(unique))
			attr.unique = true;
		for (Element node : childElements(element, Constants.ELEMENT_NAME_TYPE)) {
			Type type = parseType(node);
			attr.type = type;
		}
		return attr;
	}

	private Type parseType(Element element) {
		TypeImpl type = new TypeImpl();
		String name = element.getAttribute(Constants.ATTRIBUTE_NAME_NAME);
		if (!isEmpty(name))
			type.name = name;
		type.dataType = DataType.valueOf(element
				.getAttribute(Constants.ATTRIBUTE_NAME_DATATYPE));
		String interfName = element
				.getAttribute(Constants.ATTRIBUTE_NAME_INTERFACE);
		if (!isEmpty(interfName)) {
			Interface interf = lookupInterface(interfName);
			if (interf == null)
				pendingTypes.put(type, interfName);
			else
				type.interf = interf;
		}
		return type;
	}

	private Interface lookupInterface(String name) {
		for (Interface interf : cache) {
			if (interf.getName().equals(name))
				return interf;
		}
		return null;
	}

	private boolean isEmpty(String name) {
		return (name == null) ? true : (name.length() == 0 ? true : false);
	}

	private List<Element> childElements(Element element, String... names) {
		List<Element> elems = new ArrayList<Element>();
		NodeList children = element.getChildNodes();
		for (int i = 0; i < children.getLength(); i++) {
			Node node = children.item(i);
			if (node.getNodeType() == Node.ELEMENT_NODE
					&& Arrays.asList(names).contains(node.getLocalName())) {
				elems.add((Element) node);
			}
		}
		return elems;
	}

	private static class ModelImpl implements Model {
		List<Module> modules = new ArrayList<Module>();
		String name;

		@Override
		public List<Module> getModules() {
			return modules;
		}

		@Override
		public String getName() {
			return name;
		}
	}

	private static class ModuleImpl implements Module {
		List<Interface> interfaces = new ArrayList<Interface>();
		List<Enum> enums = new ArrayList<Enum>();
		String name;

		@Override
		public List<Interface> getInterfaces() {
			return interfaces;
		}

		@Override
		public String getName() {
			return name;
		}

		@Override
		public List<Enum> getEnums() {
			return enums;
		}
	}

	private static class InterfaceImpl implements Interface {
		List<Association> associations = new ArrayList<Association>();
		List<Attribute> attributes = new ArrayList<Attribute>();
		String name;
		String fullName;
		String pkg;
		Interface parent;

		@Override
		public List<Association> getAssociations() {
			return associations;
		}

		@Override
		public List<Attribute> getAttributes() {
			return attributes;
		}

		@Override
		public String getName() {
			return name;
		}

		@Override
		public Interface getParent() {
			return parent;
		}

		@Override
		public String getFullName() {
			return fullName;
		}

		@Override
		public String getPackage() {
			return pkg;
		}
	}

	private static class EnumImpl implements Enum {
		List<String> items = new ArrayList<String>();
		String name;

		@Override
		public List<String> getEnumValues() {
			return items;
		}

		@Override
		public String getName() {
			return name;
		}

	}

	private static class AttributeImpl implements Attribute {
		String defaultValue;
		String name;
		Type type;
		boolean readOnly;
		boolean unique;

		@Override
		public String getDefault() {
			return defaultValue;
		}

		@Override
		public String getName() {
			return name;
		}

		@Override
		public Type getType() {
			return type;
		}

		@Override
		public boolean isReadOnly() {
			return readOnly;
		}

		@Override
		public boolean isUnique() {
			return unique;
		}

	}

	private static class TypeImpl implements Type {
		DataType dataType;
		Interface interf;
		String name;

		@Override
		public DataType getDataType() {
			return dataType;
		}

		@Override
		public Interface getInterface() {
			return interf;
		}

		@Override
		public String getName() {
			return name;
		}

	}

	private static class AssociationImpl implements Association {
		Association end;
		Interface interf;
		com.lazyloris.modeler.Association.Type type;
		String name;
		List<String> stereotypes = new ArrayList<String>();

		@Override
		public Association getEnd() {
			return end;
		}

		@Override
		public Interface getInterface() {
			return interf;
		}

		@Override
		public com.lazyloris.modeler.Association.Type getType() {
			return type;
		}

		@Override
		public String getName() {
			return name;
		}

		@Override
		public List<String> getStereotypes() {
			return stereotypes;
		}

	}
}
