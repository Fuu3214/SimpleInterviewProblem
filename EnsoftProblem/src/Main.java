import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.BitSet;
import java.util.Collections;
import java.util.Random;
import java.util.Scanner;
import java.util.Vector;
import java.util.concurrent.Callable;

/**
 *To solve this problem:
 *	1.Add a source node, with edges from source to all possible starting node for Peggy
 *	2.Add a sink node, with edges from all possible starting node for Sam to sink
 *	3.Run a DFS starting from source node
 *	4.Reverse all edges, run DFS starting from sink node
 * 	5.Output intersection of visited nodes of both DFS search
 * 
 * Optimization:
 *  1.Use HashMap to convert String nodes to integer. Time for adding node: O(1);
 *  2.Use BitSet to store adjacent matrix. Memory use is reduced significantly.
 *  3.Use BitSet to store visited list and avoid list, DFS check for them each iteration. Time complexity for each check: O(1);
 *  4.Created source and sink nodes so that we only run DFS twice
 *  5.Instead of reversing all edges, we modify DFS so that we actually run DFS on transpose of adjacent matrix(But we didn't actually take transpose). 
 *  6.Create 2 threads for each DFS search so that we get result for 2 search concurrently.
 *  7.Intersection is computed by "and" operation between 2 BitSets. Time complexity O(1);
 *  
 *  Time complexity for search: O(2N) = O(N). Since we run DFS twice and for each node we visit it only once
 *  Total time complexity: O(M)<=O(N^2). It is because to build the graph we need to take every edge as input
 *  
 *  Running time on my test case(My CPU: Core i7-7820HQ): 
 *  --Less than 30s on a 10000 nodes complete graph
 *  --Less than 5s on a 1000 nodes complete graph
 *  
 * @author Shang
 * 
 */




/**
 * Use multi-threading to solve this problem.
 * The problem is solvable by multi-threading because:
 * --forward and backward DFS does not rely on each other.
 * --forward and backward DFS does not make any changes to graph and
 *   each uses a different set to record if a node is visited, therefore they don't interfere with each other.
 *   
 * @author Shang
 *
 */
class Thread implements Callable<BitSet> {
	Problem P;
	String method;
	Thread(Problem P, String method){
		this.P = P;
		this.method = method;
	}

	@Override
	public BitSet call() throws Exception {
		return P.solve(method);
	}
}

/**
 * Contain methods for reading from text file and method for printing answers
 * @author Shang
 *
 */
public class Main {
	
	private final static int MAX_SIZE = Graph.MAX_SIZE;//max size of nodes, this only affect size of BitSets
	
	
	/**
	 * Split input string by ' '
	 * @param str
	 * @return
	 */
	private static String[] split(String str) {
		String [] arr = str.split("\\s+");
		return arr;
	}
	
	/**
	 * Add each line of input to each different data structure in P based on mode
	 * 
	 * @param P
	 * @param line
	 * @param mode
	 */
	public static void processLine(Problem P, String line, String mode) {
    	if(mode != null) {
			String[] v;;
			v = split(line);
        	switch(mode) {
        		case"Map:" :
        			if(v[0].isEmpty())
        				return;
        			P.addToGraph(v[0], v[1]);
        			break;
        		case"Avoid:" :
        			P.addToSet("avoid", v);
        			break;
        		case"Peggy:" :
        			P.addToSet("peggy", v);
        			break;
        		case"Sam:" :
        			P.addToSet("sam", v);
        			break;
    			default:
    				break;
        	}
    	}
    	else {
    		System.out.println("Input format error!");
    		System.exit(0);
		}
    }
	
	/**
	 * Read from standard input
	 * @param P
	 */
	public static void buildFromStdin(Problem P) {
		Scanner scanner = new Scanner(System.in);
		String line = null;
        String mode = null;
		while((line = scanner.nextLine()) != null) {
			if(line.equals("Map:") || line.equals("Avoid:") ||  line.equals("Peggy:") ||  line.equals("Sam:")) {
	    		//Very simple input check, can't guarantee to work against bad input
	        	mode = line;
	    	}
			else {
				processLine(P, line, mode);
				if(mode.equals("Sam:")) {
					//If mode is "Sam:" then end reading after reading 1 line
					break;
				}
			}
		}
		scanner.close();
	}
	
	/**
	 * Print answer alphabetically
	 * @param answer
	 */
	public static void printInOrder(Vector<String> answer) {
		Collections.sort(answer, String.CASE_INSENSITIVE_ORDER);
		for(String str : answer) {
			System.out.println(str);
		}
	}
	/**
	 * Create 2 threads and run them simultaneously for forward and backward DFS
	 * After they are finished, compute intersection of visited nodes by an and operation
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		
		Problem P = new Problem(MAX_SIZE);

//		===============================================================
//		Code for testing:
		createTestCase(10000); //took less than 30s to run on a 10000 nodes(10^8 edges) test case
		createTestCase(1000); //took less than 5s to run on a 1000 nodes test case
		createTestCase(100); //totally OK with 100 nodes test case
		createTestCase(10); 
		buildFromFile(P); //construct problem by reading input file
//		===============================================================
		
//		buildFromStdin(P);//construct problem by standard input
		P.addSourceSink();
		Thread T1 = new Thread(P, "forward");
		Thread T2 = new Thread(P, "backward");
		BitSet ans = null, visitedByS = null;
		try {
			ans = T1.call();
			visitedByS = T2.call();
		} catch (Exception e) {
			e.printStackTrace();
		}
		if(ans != null && visitedByS != null)
			ans.and(visitedByS);//for BitSet, intersection is equivalent to "and" operation
		printInOrder(P.getNamesFrom(ans));
	}
	

	
//Below is very useful for testing, but not necessary for getting answers
	
	private final static String INPUT_FILE = "src\\input.txt";
	/**
	 * Build problem by reading from file, this is very useful for testing
	 * 
	 * @param P
	 */
	private static void buildFromFile(Problem P) {
		String filePath = INPUT_FILE;
		try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            String mode = null;
            while ((line = br.readLine()) != null) {
            	if(line.equals("Map:") || line.equals("Avoid:") ||  line.equals("Peggy:") ||  line.equals("Sam:")) {
            		//Very simple input check, can't guarantee to work against bad input
                	mode = line;
            	}
            	else
            		processLine(P, line, mode);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
	}

	/**
	 * Create a test case of a complete graph with number of nodes = size
	 * This will output a input file for testing
	 * Randomly pick a number of random nodes to add to Avoid, Peggy, Sam
	 * Duplicated nodes in Avoid, Peggy, Sam are possible, but
	 * Methods in Nodes.class resolve this
	 * 
	 * @param size
	 */
	public static void createTestCase(int size) {
		PrintWriter writer = null;
		try {
			//Map nodes will be from a1 to an
			writer = new PrintWriter(INPUT_FILE, "UTF-8");
			writer.println("Map:");
			for(int i = 0; i < size; i++) {
				for(int j = 0; j < size; j++) {
					writer.println("a" + i + " " + "a" + j);
				}
			}
	        Random random = new Random();
	        
	        //avoid nodes
			writer.println("Avoid:");
	        int count = random.nextInt(size);
	        for(int i = 0; i < count; i++) {
	        	int ran = random.nextInt(size);
	        	writer.print("a" + ran + " ");
	        }
	        writer.print("\n");
	        
	        //Peggy's starting nodes
			writer.println("Peggy:");
	        count = random.nextInt(size);
	        for(int i = 0; i < count; i++) {
	        	int ran = random.nextInt(size);
	        	writer.print("a" + ran + " ");
	        }
	        writer.print("\n");
	        
	        //Sam's starting nodes
			writer.println("Sam:");
	        count = random.nextInt(size);
	        for(int i = 0; i < count; i++) {
	        	int ran = random.nextInt(size);
	        	writer.print("a" + ran + " ");
	        }
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		writer.close();
	}
}
