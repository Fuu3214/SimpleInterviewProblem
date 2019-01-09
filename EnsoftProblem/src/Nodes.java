import java.util.HashMap;
import java.util.Vector;
/**
 * Data Structure for nodes. Each node should be a pair of 
 * <String, Integer>, where String is node name, integer\in {1,2...N}
 * It is implemented using HashMap
 * 
 * @author Shang
 *
 */
public class Nodes {
	
	private HashMap<String, Integer> nodes;//a HashMap: nodeName->{1,2...N}
	Vector<String> names;//a vector: {1,2...N}->nodeName
	
	Nodes(){
		nodes = new HashMap<String, Integer>();
		names = new Vector<String>();
	}
	
	/**
	 * If a key is not in HashMap, then:
	 * --add it to HashMap, set index to size of HashMap
	 * --record key in names  
	 * @param key
	 * @return index of key whether or not a node is in HashMap
	 */
	public int add(String key) {	
		if(nodes.get(key) == null) {
			int index = nodes.size();
			nodes.put(key, index);
			names.addElement(key);
		}
		return nodes.get(key);
	}
	
	/**
	 * node name -> index
	 * @param key
	 * @return index of key
	 */
	public int getIndex(String key) {
		if(nodes.get(key) == null)
			return -1;
		return nodes.get(key);
	}
	
	/**
	 * index -> node name
	 * @param index
	 * @return key
	 */
	public String getName(int index) {
		return names.get(index);
	}
	
	/**
	 * @return number if nodes added
	 */
	public int getSize() {
		return nodes.size();
	}
	
}
