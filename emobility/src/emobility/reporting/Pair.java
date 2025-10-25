package emobility.reporting;

import java.io.Serializable;

/**
 * A generic class representing a simple key-value pair, currently only used to store data in deserialization process.
 * 
 * <p><b>Note:</b> If you plan to store instances of this class in a collection that relies on 
 * the equality of objects (e.g. {@link java.util.HashMap}, {@link java.util.HashSet}), 
 * you should consider overriding the {@link #equals(Object)} and {@link #hashCode()} methods.</p>
 * 
 * @param <K> the type of the key
 * @param <V> the type of the value
 * 
 * @author Aleksandra Vucicevic
 * @version 1.0
 */
public class Pair<K,V> implements Serializable{
	private static final long serialVersionUID = 1L;
	
	/** The key of the pair. */
	private final K key;
	
	/** The value of the pair. */
	private final V value;
	
	/**
	 * Constructs a new {@code Pair} object with the specified key and value.
	 * @param key the key of the pair
	 * @param value the value of the pair
	 */
	public Pair(K key, V value){
		this.key = key;
		this.value = value;
	}
	
	/**
	 * Gets pair's key.
	 * @return the key
	 */
	public K getKey(){
		return key;
	}
	
	/**
	 * Gets the pair's value.
	 * @return the value
	 */
	public V getValue(){
		return value;
	}
}
