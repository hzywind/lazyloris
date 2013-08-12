/**
 * 
 */
package com.lazyloris.modeler.data;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.lazyloris.model.Meta;
import com.lazyloris.model.PersistenceRepository;

/**
 * @author wind hong
 * 
 */
public class ImporterImpl<T extends PersistenceRepository> implements Importer<T> {
    protected T repository;
    private ThreadLocal<Map<String, Object>> entities = new ThreadLocal<Map<String, Object>>();
    private Class<?>[] basicTypes = new Class<?>[] { Boolean.class, Boolean.TYPE, Integer.class, Integer.TYPE,
            Double.class, Double.TYPE, Long.class, Long.TYPE, Date.class, String.class };
    protected SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");

    public void setRepository(T repository) {
        this.repository = repository;
    }

    public void doImport(InputStream is) {
        entities.set(new HashMap<String, Object>());
        try {
            Document doc = load(is);
            Element docElem = doc.getDocumentElement();
            NodeList objElems = docElem.getElementsByTagName("object");
            for (int i = 0; i < objElems.getLength(); i++) {
                handleObject((Element) objElems.item(i));
            }
            for (Object entity : entities.get().values()) {
                repository.persist(entity);
            }
            for (int i = 0; i < objElems.getLength(); i++) {
                Element objElem = (Element) objElems.item(i);
                Object entity = entities.get().get(objElem.getAttribute("id"));
                NodeList propElems = objElem.getElementsByTagName("property");
                for (int j = 0; j < propElems.getLength(); j++) {
                    handleProperty(entity, (Element) propElems.item(j));
                }
                NodeList collElems = objElem.getElementsByTagName("collection");
                for (int j = 0; j < collElems.getLength(); j++) {
                    handleCollection(entity, (Element) collElems.item(j));
                }
                NodeList metaElems = objElem.getElementsByTagName("meta");
                for (int j = 0; j < metaElems.getLength(); j++) {
                    handleMeta(entity, (Element) metaElems.item(j));
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            entities.set(null);
        }
    }

    private void handleObject(Element objElem) throws Exception {
        String type = objElem.getAttribute("type");
        String id = objElem.getAttribute("id");
        Method creator = repository.getClass().getMethod("create" + type);
        Object entity = creator.invoke(repository);
        entities.get().put(id, entity);
    }

    private void handleProperty(Object entity, Element propElem) throws Exception {
        String name = propElem.getAttribute("name");
        String ref = propElem.getAttribute("ref");
        Node valueElem = propElem.getFirstChild();
        String methodName = "set" + capFirst(name);
        if (valueElem != null && valueElem.getNodeValue() != null && !valueElem.getNodeValue().isEmpty()) {
            Method setter = findMethod(entity, methodName, basicTypes);
            setter.invoke(entity, convert(valueElem.getNodeValue(), setter.getParameterTypes()[0]));
        } else if (ref != null && !ref.isEmpty()) {
            Object refEntity = entities.get().get(ref);
            if (refEntity == null) {
                throw new RuntimeException(String.format("No entity with id %s found", ref));
            } else {
                Method setter = findMethod(entity, methodName, refEntity.getClass().getInterfaces());
                setter.invoke(entity, refEntity);
            }
        }
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    private void handleCollection(Object entity, Element collElem) throws Exception {
        String name = collElem.getAttribute("name");
        Method getter = findMethod(entity, "get" + capFirst(name));
        Collection coll = (Collection) getter.invoke(entity);
        NodeList itemElems = collElem.getElementsByTagName("item");
        for (int i = 0; i < itemElems.getLength(); i++) {
            Element itemElem = (Element) itemElems.item(i);
            String ref = itemElem.getAttribute("ref");
            Object refEntity = entities.get().get(ref);
            if (refEntity == null) {
                throw new RuntimeException(String.format("No entity with id %s found", ref));
            } else {
                coll.add(refEntity);
            }
        }
    }
    
    private void handleMeta(Object entity, Element metaElem) throws Exception {
        String name = metaElem.getAttribute("name");
        Method getter = findMethod(entity, "get" + capFirst(name));
        Meta meta = (Meta) getter.invoke(entity);
        NodeList entryElems = metaElem.getElementsByTagName("entry");
        for (int i = 0;i<entryElems.getLength();i++) {
            Element entryElem = (Element) entryElems.item(i);
            String key = entryElem.getAttribute("key");
            String value = entryElem.getFirstChild().getNodeValue();
            meta.put(key, value);
        }
    }

    private Method findMethod(Object entity, String name, Class<?>... types) throws Exception {
        Class<?> clz = entity.getClass();
        Method method = null;
        if (types.length == 0) {
            return clz.getMethod(name);
        }
        for (Class<?> c : types) {
            try {
                method = clz.getMethod(name, c);
                break;
            } catch (NoSuchMethodException e) {
            }
        }
        for (Method m : clz.getMethods()) {
            if (m.getName().equals(name)) {
                if (m.getParameterTypes().length > 0 && m.getParameterTypes()[0].isEnum()) {
                    method = m;
                    break;
                }
            }
        }
        if (method == null)
            throw new RuntimeException(String.format("Method %s is not declared for entity class %s", name,
                    clz.getName()));
        return method;
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    private Object convert(String value, Class<?> toClass) throws Exception {
        if (toClass.equals(Integer.class) || toClass.equals(Integer.TYPE)) {
            return Integer.valueOf(value);
        } else if (toClass.equals(Double.class) || toClass.equals(Double.TYPE)) {
            return Double.valueOf(value);
        } else if (toClass.equals(Long.class) || toClass.equals(Long.TYPE)) {
            return Long.valueOf(value);
        } else if (toClass.equals(Date.class)) {
            return dateFormat.parse(value);
        } else if (toClass.equals(Boolean.class) || toClass.equals(Boolean.TYPE)) {
            return Boolean.valueOf(value);
        } else if (toClass.isEnum()) {
            return Enum.valueOf((Class) toClass, value);
        }
        return value;
    }

    private String capFirst(String str) {
        if (str.length() <= 1)
            return str.toUpperCase();
        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }

    private Document load(InputStream is) throws ParserConfigurationException, SAXException, IOException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setIgnoringElementContentWhitespace(true);
        factory.setValidating(false);
        factory.setNamespaceAware(true);
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document doc = builder.parse(is);
        return doc;
    }
}
