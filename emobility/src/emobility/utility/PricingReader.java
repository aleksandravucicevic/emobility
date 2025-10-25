package emobility.utility;

/**
 * A specialized {@link PropertiesReader} that reads pricing-realted properties from a properties file.
 * 
 * @author Aleksandra Vucicevic
 * @version 1.0
 */
public class PricingReader extends PropertiesReader{
	
	/**
	 * Constructs a {@code PricingReader} object and loads the properties from the specified file.
	 * @param propertiesFilePath file path of the properties file
	 */
	public PricingReader(String propertiesFilePath){
		super(propertiesFilePath);
	}
	
	/**
	 * Retrieves a property value as a {@code Double} object
	 * @param key property key
	 * @return value associated with the key, parsed as a {@code Double}, or {@code 0.0} if the value cannot be parsed
	 */
	public Double getDoubleProperty(String key){
		try{
			return Double.parseDouble(properties.getProperty(key));
		} catch(NumberFormatException e){
			System.err.println("Error: Unable to parse property '" + key + "' as a double. Returning default value: 0.0.");
			return 0.0;
		}
	}
}