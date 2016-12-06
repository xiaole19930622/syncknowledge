/**
 * Copyright (c) 2005-2010 springside.org.cn
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * 
 * $Id: PropertiesUtils.java 1211 2010-09-10 16:20:45Z calvinxiu $
 */
package com.xuebang.o2o.utils;

import org.apache.log4j.Logger;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.util.DefaultPropertiesPersister;
import org.springframework.util.PropertiesPersister;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Properties;

/**
 * Properties Util函数.
 * 
 * @author calvin
 */
public class PropertiesUtils {

	private static final String DEFAULT_ENCODING = "UTF-8";

	private static Logger logger = Logger.getLogger(PropertiesUtils.class);

	private static PropertiesPersister propertiesPersister = new DefaultPropertiesPersister();
	private static ResourceLoader resourceLoader = new DefaultResourceLoader();
	
	private static Properties keyValue;
	
	static {
		try {
			keyValue = loadProperties("oss.properties","application.properties");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static String getStringValue(String key) {
		return keyValue.getProperty(key) == null ? "" : keyValue.getProperty(key);
	}
	
	public static int getIntValue(String key) {
		return Integer.parseInt(keyValue.getProperty(key) == null ? "0" : keyValue.getProperty(key));
	}
	
	/**
	 * 载入多个properties文件, 相同的属性在最后载入的文件中的值将会覆盖之前的载入.
	 * 文件路径使用Spring Resource格式, 文件编码使用UTF-8.
	 * 
	 * @see org.springframework.beans.factory.config.PropertyPlaceholderConfigurer
	 */
	public static Properties loadProperties(String... resourcesPaths) throws IOException {
		Properties props = new Properties();

		for (String location : resourcesPaths) {

			logger.debug("Loading properties file from:" + location);

			InputStream is = null;
			try {
				Resource resource = resourceLoader.getResource(location);
				is = resource.getInputStream();
				propertiesPersister.load(props, new InputStreamReader(is, DEFAULT_ENCODING));
			} catch (IOException ex) {
				logger.info("Could not load properties from classpath:" + location + ": " + ex.getMessage());
			} finally {
				if (is != null) {
					is.close();
				}
			}
		}
		return props;
	}
}
