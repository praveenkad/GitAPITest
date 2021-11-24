package com.fileutils;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import org.apache.log4j.Logger;

public class FileReader {

	static Logger logger = Logger.getLogger(FileReader.class);

	/**
	 * Reusable method to get property value from either of properties files Query.properties, Constants.properties for given key
	 * @param key a {@code String} object used to perform getProperty(String key) function
	 * @throws IOException IOException during an input-output operation
	 * @author Praveen Kadambari
	 */
	public static String getProperty(String key) {

		Properties property = new Properties();

		FileInputStream inputStream = null;
		try {
			if (!key.contains("_")) {
				inputStream = new FileInputStream(
						System.getProperty("user.dir") + "\\TestDataConfig\\Query.properties");
			} else {
				inputStream = new FileInputStream(
						System.getProperty("user.dir") + "\\TestDataConfig\\Constants.properties");

			}
			property.load(inputStream);

			
		} catch (IOException e) {
			e.printStackTrace();
		}

		return property.getProperty(key);

	}

	/**
	 * Reusable method to get property value from either of properties files Query.properties, Constants.properties for given queryKey
	 * @param key a {@code String} object used to perform getProperty(String queryKey) function
	 * @author Praveen Kadambari
	 */
	public String getQuery(String queryKey) {

		return getProperty(queryKey);

	}
}
