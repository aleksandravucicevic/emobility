package emobility.utility;

import java.util.*;
import java.io.*;

/**
 * A utility class for reading properties from a properties file.
 * 
 * @author Aleksandra Vucicevic
 * @version 1.0
 */
public class PropertiesReader{
	/** The properties loaded from the file. */
	protected Properties properties;
	
	/**
	 * Constructs a {@code PropertiesReader} object and loads the properties from the specified file.
	 * @param propertiesFile file path of the properties file
	 */
	public PropertiesReader(String propertiesFile){
		properties = new Properties();
		try(InputStream input = new FileInputStream(propertiesFile)){
			properties.load(input);
		} catch(IOException e){
			e.printStackTrace();
		}
	}
	
	/**
	 * Retrieves a property value as a string.
	 * @param key property key
	 * @return value associated with the key, or {@code null} if the key is not found
	 */
	public String getProperty(String key){
		return properties.getProperty(key);
	}
}