package com.lazyloris.model.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Date;
import java.util.Map;

import org.junit.Test;

import com.lazyloris.model.Meta;
import com.lazyloris.model.impl.MetaImpl;

public class MetaTest {

    @SuppressWarnings("rawtypes")
	@Test
    public void test() {

    	
        Meta meta = new MetaImpl();

        Date now = new Date();
        meta.put("now", now);
        meta.put("is", true);
        meta.put("array", new String[] { "a", "b" });
        
        Map map = meta.getMap();
        assertEquals(3, map.size());
        assertEquals("true", map.get("is"));

        Meta other = meta.copy();
        assertEquals(other.getDate("now"), now);
        assertTrue(other.getBoolean("is"));
        String[] array = other.getStrings("array");
        assertEquals(array.length, 2);
        assertEquals(array[0], "a");
        assertEquals(array[1], "b");
    }

    @Test(expected = NullPointerException.class)
    public void testNull() {
        Meta meta = new MetaImpl();
        meta.put("null", (String) null);
    }

}
