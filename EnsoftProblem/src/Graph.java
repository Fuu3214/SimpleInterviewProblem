import java.util.BitSet;

/**
 * A graph G = (V,E), where V is stored as Nodes and 
 * E is stored as an adjacent matrix in a BitSet
 * 
 * @author Shang
 *
 */
public class Graph {
	public final static int MAX_SIZE = 65536;//max_number of V, default 65536. Only affect BitSets
	private int maxSize = MAX_SIZE;
	
	private Nodes V;//stores vertices in a hash map 
 	private BitSet E;//stores E as an adjacent matrix in a BitSet, it can reach relatively larger
 		
	Graph(){
		create(maxSize);
	}
	Graph(int maxSize){
		this.maxSize = maxSize;
		create(this.maxSize);
	}
	/**
	 * create an empty graph with N vertices
	 * N only influence size of adjacent matrix, which is a BitSet
	 * @param maxSize
	 */
	private void create(int maxSize) {
		E = new BitSet(maxSize * maxSize);
		V = new Nodes();
	}
	
	/**
	 * Add an edge to adjacent matrix.
	 * This method assume that u,v already exist.
	 * It set BitSet adjMatrix according to index
	 * @param u
	 * @param v
	 */
	public void add(int u, int v) {
		setElementAt(u, v);
	}
	/**
	 * Add an edge to adjacent matrix.
	 * It first add this node to HashMap.
	 * Then set BitSet adjMatrix according to index
	 * @param u
	 * @param v
	 */
	public void add(String u, String v) {
		//first add to Nodes, then add to adjMatrix
		//Nodes.add will check if node already exists
		int row = V.add(u);
		int col = V.add(v);
		add(row, col);
	}
	
	/**
	 * Checking if an edge exist by referring to BitSet is simple.
	 * @param u
	 * @param v
	 * @return
	 */
	public boolean isEdge(int u, int v) {
		return E.get(u * maxSize + v);
	}
	public boolean isEdge(String u, String v) {
		int row = V.getIndex(u);//Convert node name to integer
		int col = V.getIndex(v);
		return isEdge(row, col);
	}
	
	/**
	 * Operating adjacent matrix
	 * @param row
	 * @param col
	 * @return
	 */
	public int getElementAt(int row, int col) {
		boolean flag = E.get(row * maxSize + col);
		return  (flag == true? 1:0);
	}
	public void setElementAt(int row, int col) {
		E.set(row * maxSize + col);
	}
	
	
	public Nodes getV() {
		return V;
	}
	public BitSet getE() {
		return E;
	}
	
	
	public int getNumberOfV() {
		return V.getSize();
	}
	public int getNumberOfE() {
		return E.cardinality();
	}
	
	@Override
	public String toString() {
		//print node name and adjacent matrix nicely
		StringBuilder builder = new StringBuilder();
		builder.append("\t");
		int size = V.getSize();
		for (int i = 0; i < size; i++) {
			builder.append(V.getName(i)).append("\t");
		}
		builder.append("\n");
		for (int row = 0; row < size; row++) {
			builder.append(V.getName(row)).append("\t");
			for (int col = 0; col < size; col++) {
				builder.append(getElementAt(row, col)).append("\t");
			}
			builder.append("\n");
		}
		return builder.toString();
	}

	
}
