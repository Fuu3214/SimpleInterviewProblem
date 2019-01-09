import java.util.BitSet;
import java.util.Vector;

/**
 * This class contains:
 * 1.A Graph G = (V,E),
 * 2.Three BitSet recording nodes that from "avoid", "peggy", "sam"
 * 3.Two BitSet recording visited node by forward and backward DFS
 * 4.A method for adding source and sink nodes
 * 5.DFS search
 * 6.DFS_T, which is a DFS on the transpose of adjacent matrix. 
 * 	 It is equivalent to Running DFS on G' = (V,E'), where each edge (u,v)\in E iff (v,u)\in E'
 * 7.Some methods for outputting results
 * 
 * @author Shang
 *
 */
public class Problem{
	private final static String SOURCE = "__SR@2333__";//Don't want this to show as input
	private final static String SINK = "__T@233__";//Don't want this to show as input 
	
	private int maxSize = Graph.MAX_SIZE;//max_number of V
	private Graph G;
	private Nodes V;
	
	private BitSet visitedByP;//mark if a node is visited by forward DFS
	private BitSet visitedByS;//mark if a node is visited by backward DFS
	
	private BitSet avoid;//store the node that should be avoided
	private BitSet peggy;//store possible starting locations for node peggy
	private BitSet sam;//store possible starting locations for node sam
	
	private int N = 0;//actual size of vertices
		
	Problem(){
		create(maxSize);
	}
	Problem(int maxSize){
		this.maxSize = maxSize;
		create(maxSize);
	}
	private void create(int maxSize){
		G = new Graph(maxSize);
		V = G.getV();
		
		avoid = new BitSet(maxSize);
		peggy = new BitSet(maxSize);
		sam = new BitSet(maxSize);
		
		visitedByP = new BitSet(maxSize);
		visitedByS = new BitSet(maxSize);
	}
		
	
	/**
	 * Adding u,v to graph
	 * @param u
	 * @param v
	 */
	public void addToGraph(String u, String v) {
		G.add(u, v);
		N = V.getSize();
	}
	public void addToGraph(int u, int v) {
		G.add(u, v);
		N = V.getSize();
	}
	
	
	
	/**
	 * Add a number of vertices in to three different BitSets, Used when building problem
	 * @param setName
	 * @param vertices
	 */
	public void addToSet(String setName, String[] vertices) {
		BitSet bitset = null;
		switch(setName){
			case "avoid":
				bitset = avoid;
				break;
			case "peggy":
				bitset = peggy;
				break;
			case "sam":
				bitset = sam;
				break;
		}
		if(bitset != null) {
			for(String v : vertices) {
				int tmp = V.getIndex(v);
				if(tmp == -1)
					return;
				bitset.set(tmp);
			}					
		}
		else {
			System.out.println("something goes wrong");
			System.exit(0);
		}
	}	
	
	
	
	/**
	 * Check if a node is in avoid list
	 * @param u
	 * @return
	 */
	public boolean shouldAvoid(int u) {
		return avoid.get(u);
	}
	
	
	
	/**
	 * Functions for visited nodes, used by DFS and DFS_T
	 * @param Set
	 * @param u
	 */
	public void visit(BitSet Set, int u) {
		Set.set(u);
	}
	public Boolean isVisited(BitSet Set, int u) {
		return Set.get(u);
	}
	
	
	
	/**
	 * Standard DFS to find reachable nodes starting from an arbitrary node and:
	 * 1.disregard visited nodes for avoiding cycles
	 * 2.disregard nodes in "avoid"
	 * 
	 * @param node
	 */
	public void DFS(int node) {
		if(isVisited(visitedByP, node) || shouldAvoid(node)) 
			return;
		visit(visitedByP, node);
		for(int child = 0; child < N; child++) {
			if(G.isEdge(node, child)) {
				DFS(child);
			}
		}
	}
	
	/**
	 * A slightly modified version of above
	 * it is equivalent to running DFS on the transpose of original adjacent matrix
	 * except that it doesn't actually take the transpose of adjacent matrix
	 * 
	 * @param node
	 */
	public void DFS_T(int node) {
		if(isVisited(visitedByS, node) || shouldAvoid(node)) 
			return;
		visit(visitedByS, node);
		for(int parent = 0; parent < N; parent++) {
			if(G.isEdge(parent, node)) {
				DFS_T(parent);
			}
		}
	}
	
	
	
	/**
	 * Add a pair of source node and sink node with edges:
	 *	1.From source node to all possible starting node for Peggy
	 *	2.From all possible starting node for Sam to sink node
	 * By this way we only run DFS twice
	 */
	public void addSourceSink() {
		int source = V.add(SOURCE);
		int sink = V.add(SINK);
		for(int i = 0; i < N; i++) {
			if(peggy.get(i))
				addToGraph(source, i);;
		}
		for(int i = 0; i < N; i++) {
			if(sam.get(i))
				addToGraph(i, sink);;
		}
	}
	
	
	/**
	 * Method is limited to "forward", "backward". Will call DFS or DFS_T accordingly
	 * Two BitSet stores visited node by each search. They are the returning value.
	 * 
	 * @param method 
	 * @return BitSet 
	 */
	public BitSet solve(String method) {

		switch(method) {
		case "forward":
			DFS(V.getIndex(SOURCE));
			return visitedByP;
		case "backward":
			DFS_T(V.getIndex(SINK));
			return visitedByS;
		default:
			System.out.println("Something went wrong");
			return null;
		}		
	}
	
	
	/**
	 * Convert final result to String vector for output
	 * @param bitset
	 * @return
	 */
	public Vector<String> getNamesFrom(BitSet bitset) {
		Vector<String> ret = new Vector<String>();
		for(int i = 0; i < N; i++) {
			String tmp = V.getName(i);
			if(bitset.get(i) && !tmp.equals(SOURCE) && !tmp.equals(SINK))
				ret.addElement(V.getName(i));
		}
		return ret;
	}	
	
	
	/**
	 * The following methods are very useful when testing, but they are not necessary for getting answers
	 * 	 */
	public String[] contentOfSet(String setName) {
		BitSet bitset = null;
		String[] arr = new String[maxSize];
		switch(setName){
			case "avoid":
				bitset = avoid;
				break;
			case "peggy":
				bitset = peggy;
				break;
			case "sam":
				bitset = sam;
				break;
			default:
				System.out.println("Error");
				System.exit(0);
				break;
		}
		int j = 0;
		for(int i = 0; i < maxSize; i++) {
			if(bitset.get(i) == true) {
				arr[j] = V.getName(i);
				j++;
			}
		}
		return arr;
	}
	
	public void printSet(String setName) {
		String[] arr = contentOfSet(setName);
		for(String str : arr) {
			if(str != null) {
				System.out.println(str);
			}
		}
	}
	
	@Override
	public String toString() {
		return G.toString();
	}
	

}
