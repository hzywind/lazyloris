package com.lazyloris.modeler.impl;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import com.lazyloris.modeler.Env;

import freemarker.cache.URLTemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

public class FreemarkerAdaptor {

	private String outputPath;
	private String packageName;
	private String packagePath;
	private String fileName;
	private String templateName;
	private Map<String, Object> parameter = new HashMap<String, Object>();

	public String getPackageName() {
		return packageName;
	}

	public String getPackagePath() {
		return packagePath;
	}

	public void setPackageName(String packageName) {
		this.packageName = packageName;
		this.packagePath = packageName.replace('.', File.separatorChar);
	}

	public void setOutputPath(String outputPath) {
		this.outputPath = outputPath;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public void setTemplateName(String templateName) {
		this.templateName = templateName;
	}

	public void addParameter(String name, Object value) {
		parameter.put(name, value);
	}

	public void render() {
		Configuration config = new Configuration();
		config.setTemplateLoader(new ResourceTemplateLoader());
		try {
			Template t = config.getTemplate(templateName);
			File dir = new File(Env.getInstance().getOutputDirectory(),
					outputPath);
			if (!dir.exists())
				dir.mkdirs();
			File outputFile = new File(dir, fileName);
			PrintWriter writer = new PrintWriter(outputFile, "UTF-8");
			t.process(parameter, writer);
			writer.flush();
			writer.close();
		} catch (IOException e) {
			throw new RuntimeException(e);
		} catch (TemplateException e) {
			throw new RuntimeException(e);
		}
	}

	private class ResourceTemplateLoader extends URLTemplateLoader {
		@Override
		protected URL getURL(String name) {
			URL url;
			ClassLoader cl = Thread.currentThread().getContextClassLoader();
			if (cl != null) {
				url = cl.getResource(name);
				if (url != null) {
					return url;
				}
			}
			url = FreemarkerAdaptor.class.getClassLoader().getResource(name);
			if (url != null) {
				return url;
			}

			url = ClassLoader.getSystemClassLoader().getResource(name);
			if (url != null) {
				return url;
			}
			return null;
		}
	}
}
